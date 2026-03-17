package com.aurafarming.service;

import com.aurafarming.dto.ExportRequestDTO;
import com.aurafarming.dao.FarmDAO;
import com.aurafarming.dao.PlotDAO;
import com.aurafarming.exception.ValidationException;
import com.aurafarming.model.*;
import com.aurafarming.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExportService {
    private final AuditService auditService = new AuditService();
    private final FarmDAO farmDAO = new FarmDAO();
    private final PlotDAO plotDAO = new PlotDAO();
    private final YieldLogService yieldLogService = new YieldLogService();
    private final MarketPriceService marketPriceService = new MarketPriceService();
    private final CropPlanService cropPlanService = new CropPlanService();

    public Path export(User actor, ExportRequestDTO request) {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
        String fileName = request.reportType() + "_" + timestamp + ".csv";
        Path output = FileUtil.resolveExportFile(fileName);

        List<String> lines = buildExportLines(actor, request);

        try {
            Files.write(output, lines);
        } catch (IOException e) {
            throw new RuntimeException("Unable to export file.", e);
        }

        auditService.log(actor, "EXPORT_REPORT", "Export", output.getFileName().toString(), "Export generated.");
        return output;
    }

    public List<String> preview(User actor, ExportRequestDTO request, int maxRows) {
        List<String> lines = buildExportLines(actor, request);
        int safeMax = Math.max(maxRows, 2);
        if (lines.size() <= safeMax) {
            return lines;
        }
        List<String> preview = new ArrayList<>(lines.subList(0, safeMax));
        preview.add("... " + (lines.size() - safeMax) + " more rows");
        return preview;
    }

    private List<String> buildExportLines(User actor, ExportRequestDTO request) {
        String requestedType = request.reportType() == null ? "" : request.reportType().trim().toLowerCase();
        String effectiveFarmerId = request.farmerId();
        if (actor.getRole() == Role.FARMER) {
            if (!requestedType.equals("plot") && !requestedType.equals("crop-plan")) {
                throw new ValidationException("Farmers can export only Plot and Crop Plan reports.");
            }
            if (effectiveFarmerId != null && !effectiveFarmerId.isBlank()
                    && !effectiveFarmerId.equalsIgnoreCase(actor.getUserId())) {
                throw new ValidationException("Farmers can export only their own records.");
            }
            effectiveFarmerId = actor.getUserId();
        }

        return switch (requestedType) {
            case "audit" -> exportAudit(request.fromDate(), request.toDate(), actor.getRole());
            case "yield" -> exportYield(actor);
            case "market" -> exportMarket(request.fromDate(), request.toDate());
            case "plot" -> exportPlot(effectiveFarmerId);
            default -> exportCropPlan(effectiveFarmerId);
        };
    }

    private List<String> exportAudit(LocalDate from, LocalDate to, Role role) {
        List<String> lines = new ArrayList<>();
        lines.add("logId,actorId,role,action,targetType,targetId,timestamp,details");
        for (AuditLog log : auditService.filter("", "ALL", from, to, role)) {
            lines.add(String.join(",", log.getLogId(), log.getActorId(), log.getActorRole().name(), log.getAction(),
                    log.getTargetType(), log.getTargetId(), log.getTimestamp().toString(), safe(log.getDetails())));
        }
        return lines;
    }

    private List<String> exportYield(User actor) {
        List<String> lines = new ArrayList<>();
        lines.add("yieldId,farmerId,plotId,district,cropType,estimatedKg,actualKg,harvestDate");
        for (YieldLog y : yieldLogService.findVisible(actor)) {
            lines.add(String.join(",", y.getYieldId(), y.getFarmerId(), y.getPlotId(), y.getDistrict().name(),
                    y.getCropType(), String.valueOf(y.getEstimatedKg()), String.valueOf(y.getActualKg()),
                    y.getHarvestDate().toString()));
        }
        return lines;
    }

    private List<String> exportMarket(LocalDate from, LocalDate to) {
        List<String> lines = new ArrayList<>();
        lines.add("priceId,district,cropType,pricePerKg,date");
        for (MarketPrice p : marketPriceService.findHistory(from, to, "")) {
            lines.add(String.join(",", p.getPriceId(), p.getDistrict().name(), p.getCropType(),
                    String.valueOf(p.getPricePerKg()), p.getDate().toString()));
        }
        return lines;
    }

    private List<String> exportCropPlan(String farmerId) {
        List<String> lines = new ArrayList<>();
        lines.add("planId,farmerId,plotId,cropType,season,recommendationScore,startDate,expectedHarvestDate");
        List<CropPlan> visiblePlans = cropPlanService.findAll().stream()
                .filter(p -> farmerId == null || farmerId.isBlank() || p.getFarmerId().equalsIgnoreCase(farmerId))
                .collect(Collectors.toList());
        for (CropPlan p : visiblePlans) {
            lines.add(String.join(",", p.getPlanId(), p.getFarmerId(), p.getPlotId(), p.getCropType(),
                    p.getSeason().toString(),
                    String.valueOf(p.getRecommendationScore()), p.getStartDate().toString(),
                    p.getExpectedHarvestDate().toString()));
        }
        return lines;
    }

    private List<String> exportPlot(String farmerId) {
        List<String> lines = new ArrayList<>();
        lines.add("farmId,farmerId,district,farmTag,unit,farmArea,plotId,plotCode,plotArea");

        List<Farm> farms = farmDAO.findAll().stream()
                .filter(f -> farmerId == null || farmerId.isBlank() || f.getFarmerId().equalsIgnoreCase(farmerId))
                .collect(Collectors.toList());

        for (Farm farm : farms) {
            List<Plot> farmPlots = plotDAO.findAll().stream()
                    .filter(p -> p.getFarmId().equalsIgnoreCase(farm.getFarmId()))
                    .collect(Collectors.toList());
            if (farmPlots.isEmpty()) {
                lines.add(String.join(",", farm.getFarmId(), farm.getFarmerId(), farm.getDistrict().name(),
                        safe(farm.getFarmTag()), safe(farm.getMeasurementUnit()), String.valueOf(farm.getTotalArea()),
                        "", "", ""));
                continue;
            }
            for (Plot plot : farmPlots) {
                lines.add(String.join(",", farm.getFarmId(), farm.getFarmerId(), farm.getDistrict().name(),
                        safe(farm.getFarmTag()), safe(farm.getMeasurementUnit()), String.valueOf(farm.getTotalArea()),
                        plot.getPlotId(), safe(plot.getPlotCode()), String.valueOf(plot.getArea())));
            }
        }

        return lines;
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
