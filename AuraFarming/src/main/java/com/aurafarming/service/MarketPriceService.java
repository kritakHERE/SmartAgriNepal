package com.aurafarming.service;

import com.aurafarming.dao.MarketPriceDAO;
import com.aurafarming.model.District;
import com.aurafarming.model.MarketPrice;
import com.aurafarming.model.User;
import com.aurafarming.util.IdGenerator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MarketPriceService {
    private final MarketPriceDAO marketPriceDAO = new MarketPriceDAO();
    private final AuditService auditService = new AuditService();

    public MarketPrice save(User actor, District district, String cropType, double pricePerKg) {
        MarketPrice price = new MarketPrice(IdGenerator.next("MRK"), district, cropType, pricePerKg, LocalDate.now());
        List<MarketPrice> all = marketPriceDAO.findAll();
        all.add(price);
        marketPriceDAO.saveAll(all);
        auditService.log(actor, "SAVE_MARKET_PRICE", "MarketPrice", price.getPriceId(), "Market price saved.");
        return price;
    }

    public List<MarketPrice> findAll() {
        return marketPriceDAO.findAll();
    }

    public List<MarketPrice> findHistory(LocalDate from, LocalDate to, String cropType) {
        return findAll().stream().filter(p -> (from == null || !p.getDate().isBefore(from)) &&
                (to == null || !p.getDate().isAfter(to)) &&
                (cropType == null || cropType.isBlank()
                        || p.getCropType().toLowerCase(Locale.ROOT).contains(cropType.toLowerCase(Locale.ROOT))))
                .sorted(Comparator.comparing(MarketPrice::getDate).reversed()).collect(Collectors.toList());
    }

    public String trendHint(District district, String cropType) {
        List<MarketPrice> prices = findAll().stream()
                .filter(p -> p.getDistrict() == district && p.getCropType().equalsIgnoreCase(cropType))
                .sorted(Comparator.comparing(MarketPrice::getDate)).toList();
        if (prices.size() < 2) {
            return "No trend yet";
        }
        double latest = prices.get(prices.size() - 1).getPricePerKg();
        double previous = prices.get(prices.size() - 2).getPricePerKg();
        if (latest > previous) {
            return "Trend: Up";
        }
        if (latest < previous) {
            return "Trend: Down";
        }
        return "Trend: Stable";
    }
}
