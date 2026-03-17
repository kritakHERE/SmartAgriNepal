package com.aurafarming.service;

import com.aurafarming.dao.FarmDAO;
import com.aurafarming.dao.PlotDAO;
import com.aurafarming.exception.ValidationException;
import com.aurafarming.model.*;
import com.aurafarming.util.IdGenerator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FarmPlotService {
    private final FarmDAO farmDAO = new FarmDAO();
    private final PlotDAO plotDAO = new PlotDAO();
    private final AuditService auditService = new AuditService();

    public List<Farm> getFarmsForUser(User user) {
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.OFFICER) {
            return farmDAO.findAll();
        }
        return farmDAO.findAll().stream().filter(f -> f.getFarmerId().equalsIgnoreCase(user.getUserId()))
                .collect(Collectors.toList());
    }

    public List<Plot> getPlotsForFarm(String farmId) {
        return plotDAO.findAll().stream().filter(p -> p.getFarmId().equalsIgnoreCase(farmId))
                .collect(Collectors.toList());
    }

    public List<Plot> getPlotsForUser(User user) {
        Set<String> farmIds = getFarmsForUser(user).stream().map(Farm::getFarmId).collect(Collectors.toSet());
        return plotDAO.findAll().stream().filter(p -> farmIds.contains(p.getFarmId())).collect(Collectors.toList());
    }

    public Farm getFarmById(String farmId) {
        return farmDAO.findAll().stream().filter(f -> f.getFarmId().equalsIgnoreCase(farmId)).findFirst().orElse(null);
    }

    public Farm createFarm(User actor, String farmerId, District district, String farmTag, String unit, double area) {
        if (district == null || unit == null || unit.isBlank() || area <= 0) {
            throw new ValidationException("District, unit and area are required.");
        }
        if (actor.getRole() == Role.FARMER && !actor.getUserId().equalsIgnoreCase(farmerId)) {
            throw new ValidationException("You can only create your own farm.");
        }
        Farm farm = new Farm(IdGenerator.next("FRM"), farmerId, district,
                farmTag == null ? "" : farmTag.trim(), unit, area);
        List<Farm> farms = farmDAO.findAll();
        farms.add(farm);
        farmDAO.saveAll(farms);
        auditService.log(actor, "CREATE_FARM", "Farm", farm.getFarmId(), "Farm created.");
        return farm;
    }

    public Plot createPlot(User actor, String farmId, double area) {
        if (farmId == null || farmId.isBlank()) {
            throw new ValidationException("Please select a farm before creating a plot.");
        }
        if (area <= 0) {
            throw new ValidationException("Plot area must be positive.");
        }
        List<Plot> plots = plotDAO.findAll();
        long farmPlotCount = plots.stream().filter(p -> p.getFarmId().equalsIgnoreCase(farmId)).count() + 1;
        String plotCode = "F" + farmId.replace("FRM-", "") + "_P" + farmPlotCount;
        Plot plot = new Plot(IdGenerator.next("PLT"), farmId, plotCode, area);
        plots.add(plot);
        plotDAO.saveAll(plots);
        auditService.log(actor, "CREATE_PLOT", "Plot", plot.getPlotId(), "Plot created.");
        return plot;
    }

    public void deleteFarm(User actor, String farmId) {
        if (farmId == null || farmId.isBlank()) {
            throw new ValidationException("Please select a farm to delete.");
        }
        List<Farm> farms = farmDAO.findAll();
        boolean removed = farms.removeIf(f -> f.getFarmId().equalsIgnoreCase(farmId));
        if (removed) {
            farmDAO.saveAll(farms);
            List<Plot> plots = plotDAO.findAll();
            plots.removeIf(p -> p.getFarmId().equalsIgnoreCase(farmId));
            plotDAO.saveAll(plots);
            auditService.log(actor, "DELETE_FARM", "Farm", farmId, "Farm removed.");
        }
    }
}
