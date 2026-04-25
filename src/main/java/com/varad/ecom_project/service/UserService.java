package com.varad.ecom_project.service;

import com.varad.ecom_project.model.User;
import com.varad.ecom_project.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User register(User user) {
        user.setRole("USER"); // default role
        return userRepo.save(user);
    }

    public User login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }
}