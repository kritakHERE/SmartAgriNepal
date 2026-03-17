package com.aurafarming.service;

import com.aurafarming.model.User;

public final class SessionContext {
    private static User currentUser;

    private SessionContext() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
