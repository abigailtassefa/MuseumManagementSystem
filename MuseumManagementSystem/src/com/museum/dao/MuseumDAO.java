package com.museum.dao;

import com.museum.model.Museum;
import com.museum.model.City;
import com.museum.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MuseumDAO {

    // Get all museums (with city object)
    public List<Museum> getAllMuseums() throws SQLException {
        List<Museum> museums = new ArrayList<>();
        String sql = "SELECT m.*, c.city_name FROM museums m JOIN cities c ON m.city_id = c.city_id ORDER BY m.name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                museums.add(extractMuseumFromResultSet(rs));
            }
        }
        return museums;
    }

    // Get museums by city ID
    public List<Museum> getMuseumsByCity(int cityId) throws SQLException {
        List<Museum> museums = new ArrayList<>();
        String sql = "SELECT m.*, c.city_name FROM museums m JOIN cities c ON m.city_id = c.city_id WHERE m.city_id = ? ORDER BY m.name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cityId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                museums.add(extractMuseumFromResultSet(rs));
            }
        }
        return museums;
    }

    // Search by name or city (case-insensitive)
    public List<Museum> searchMuseums(String keyword) throws SQLException {
        List<Museum> museums = new ArrayList<>();
        String sql = "SELECT m.*, c.city_name FROM museums m JOIN cities c ON m.city_id = c.city_id " +
                     "WHERE m.name LIKE ? OR c.city_name LIKE ? ORDER BY m.name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                museums.add(extractMuseumFromResultSet(rs));
            }
        }
        return museums;
    }

    // Insert a new museum
    public boolean insertMuseum(Museum museum) throws SQLException {
        String sql = "INSERT INTO museums (name, description, city_id, opening_time, closing_time, " +
                     "local_price, foreign_price, student_price, cultural_info, image_path) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setMuseumParameters(pstmt, museum);
            int affected = pstmt.executeUpdate();
            if (affected == 0) return false;
            // Retrieve generated museum_id
            try (ResultSet generated = pstmt.getGeneratedKeys()) {
                if (generated.next()) {
                    museum.setMuseumId(generated.getInt(1));
                }
            }
            return true;
        }
    }

    // Update a museum
    public boolean updateMuseum(Museum museum) throws SQLException {
        String sql = "UPDATE museums SET name=?, description=?, city_id=?, opening_time=?, closing_time=?, " +
                     "local_price=?, foreign_price=?, student_price=?, cultural_info=?, image_path=? " +
                     "WHERE museum_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setMuseumParameters(pstmt, museum);
            pstmt.setInt(11, museum.getMuseumId());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete a museum by ID
    public boolean deleteMuseum(int museumId) throws SQLException {
        String sql = "DELETE FROM museums WHERE museum_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, museumId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Helper: extract Museum from ResultSet
    private Museum extractMuseumFromResultSet(ResultSet rs) throws SQLException {
        City city = new City(rs.getInt("city_id"), rs.getString("city_name"));
        Museum m = new Museum();
        m.setMuseumId(rs.getInt("museum_id"));
        m.setName(rs.getString("name"));
        m.setDescription(rs.getString("description"));
        m.setCity(city);
        m.setOpeningTime(rs.getTime("opening_time"));
        m.setClosingTime(rs.getTime("closing_time"));
        m.setLocalPrice(rs.getBigDecimal("local_price"));
        m.setForeignPrice(rs.getBigDecimal("foreign_price"));
        m.setStudentPrice(rs.getBigDecimal("student_price"));
        m.setCulturalInfo(rs.getString("cultural_info"));
        m.setImagePath(rs.getString("image_path"));
        return m;
    }

    // Helper: set parameters for INSERT/UPDATE (excluding museum_id)
    private void setMuseumParameters(PreparedStatement pstmt, Museum m) throws SQLException {
        pstmt.setString(1, m.getName());
        pstmt.setString(2, m.getDescription());
        pstmt.setInt(3, m.getCity().getCityId());
        pstmt.setTime(4, m.getOpeningTime());
        pstmt.setTime(5, m.getClosingTime());
        pstmt.setBigDecimal(6, m.getLocalPrice());
        pstmt.setBigDecimal(7, m.getForeignPrice());
        pstmt.setBigDecimal(8, m.getStudentPrice());
        pstmt.setString(9, m.getCulturalInfo());
        pstmt.setString(10, m.getImagePath());
    }
}