package com.aurafarming.dao;

import com.aurafarming.model.User;
import com.aurafarming.util.Constants;

import java.util.List;
import java.util.Optional;

public class UserDAO extends BaseObjectDAO<User> {
    public UserDAO() {
        super(Constants.USERS_FILE);
    }

    public List<User> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<User> users) {
        writeAllInternal(users);
    }

    public void save(User user) {
        List<User> users = findAll();
        users.add(user);
        saveAll(users);
    }

    public Optional<User> findByEmail(String email) {
        return findAll().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public Optional<User> findById(String userId) {
        return findAll().stream().filter(u -> u.getUserId().equalsIgnoreCase(userId)).findFirst();
    }
}
