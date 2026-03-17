package com.aurafarming.dao;

import com.aurafarming.model.WeatherAlert;
import com.aurafarming.util.Constants;

import java.util.List;

public class WeatherAlertDAO extends BaseObjectDAO<WeatherAlert> {
    public WeatherAlertDAO() {
        super(Constants.WEATHER_ALERT_FILE);
    }

    public List<WeatherAlert> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<WeatherAlert> alerts) {
        writeAllInternal(alerts);
    }
}
