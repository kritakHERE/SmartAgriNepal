package com.aurafarming.dao;

import com.aurafarming.model.MarketPrice;
import com.aurafarming.util.Constants;

import java.util.List;

public class MarketPriceDAO extends BaseObjectDAO<MarketPrice> {
    public MarketPriceDAO() {
        super(Constants.MARKET_PRICE_FILE);
    }

    public List<MarketPrice> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<MarketPrice> prices) {
        writeAllInternal(prices);
    }
}
