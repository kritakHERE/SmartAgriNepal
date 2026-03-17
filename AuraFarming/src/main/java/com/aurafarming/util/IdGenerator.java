package com.aurafarming.util;

import java.util.UUID;

public final class IdGenerator {
    private IdGenerator() {
    }

    public static String next(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
