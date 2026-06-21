package com.museum.dao;

import com.museum.model.City;
import com.museum.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {

    public List<City> getAllCities() throws SQLException {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities ORDER BY city_name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                City city = new City();
                city.setCityId(rs.getInt("city_id"));
                city.setCityName(rs.getString("city_name"));
                cities.add(city);
            }
        }
        return cities;
    }

    public City getCityById(int id) throws SQLException {
        String sql = "SELECT * FROM cities WHERE city_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new City(rs.getInt("city_id"), rs.getString("city_name"));
            }
        }
        return null;
    }
}