package com.aurafarming.util;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public final class Constants {
    private Constants() {
    }

    public static final String DEFAULT_ADMIN_EMAIL = "admin@control.com";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    public static final String DEFAULT_ADMIN_NAME = "System Admin";

    public static final Path DATA_DIR = Path.of("data storage");
    public static final Path EXPORT_DIR = Path.of("export results");

    public static final String USERS_FILE = "users.dat";
    public static final String FARMS_FILE = "farms.dat";
    public static final String PLOTS_FILE = "plots.dat";
    public static final String CROP_PLAN_FILE = "crop_plans.dat";
    public static final String MARKET_PRICE_FILE = "market_prices.dat";
    public static final String WEATHER_ALERT_FILE = "weather_alerts.dat";
    public static final String YIELD_LOG_FILE = "yield_logs.dat";
    public static final String AUDIT_LOG_FILE = "audit_logs.dat";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
