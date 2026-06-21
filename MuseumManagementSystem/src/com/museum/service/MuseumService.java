package com.museum.service;

import com.museum.dao.MuseumDAO;
import com.museum.dao.CityDAO;
import com.museum.model.Museum;
import com.museum.model.City;
import java.sql.SQLException;
import java.util.List;

public class MuseumService {
    private MuseumDAO museumDAO = new MuseumDAO();
    private CityDAO cityDAO = new CityDAO();

    public List<City> getAllCities() throws SQLException {
        return cityDAO.getAllCities();
    }

    public List<Museum> getAllMuseums() throws SQLException {
        return museumDAO.getAllMuseums();
    }

    public List<Museum> getMuseumsByCity(int cityId) throws SQLException {
        return museumDAO.getMuseumsByCity(cityId);
    }

    public List<Museum> searchMuseums(String keyword) throws SQLException {
        return museumDAO.searchMuseums(keyword);
    }

    public boolean addMuseum(Museum museum) throws SQLException {
        validateMuseum(museum);
        return museumDAO.insertMuseum(museum);
    }

    public boolean updateMuseum(Museum museum) throws SQLException {
        validateMuseum(museum);
        return museumDAO.updateMuseum(museum);
    }

    public boolean deleteMuseum(int museumId) throws SQLException {
        return museumDAO.deleteMuseum(museumId);
    }

    private void validateMuseum(Museum m) {
        if (m.getName() == null || m.getName().trim().isEmpty())
            throw new IllegalArgumentException("Museum name is required.");
        if (m.getCity() == null)
            throw new IllegalArgumentException("City must be selected.");
        if (m.getOpeningTime() == null || m.getClosingTime() == null)
            throw new IllegalArgumentException("Opening and closing times are required.");
        // Add more validations as needed
    }
} 
