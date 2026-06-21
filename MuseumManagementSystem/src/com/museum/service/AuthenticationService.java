package com.museum.service;

import com.museum.dao.UserDAO;
import com.museum.model.User;
import java.sql.SQLException;

public class AuthenticationService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws SQLException {
        return userDAO.authenticate(username, password);
    }
} 
