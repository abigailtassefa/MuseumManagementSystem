package com.museum.dao;

import com.museum.model.Transaction;
import com.museum.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public class TransactionDAO {

    public boolean saveTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (museum_id, visitor_name, visitor_email, " +
                     "visitor_phone, visitor_type, quantity, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getMuseumId());
            pstmt.setString(2, transaction.getVisitorName());
            pstmt.setString(3, transaction.getVisitorEmail());
            pstmt.setString(4, transaction.getVisitorPhone());
            pstmt.setString(5, transaction.getVisitorType());
            pstmt.setInt(6, transaction.getQuantity());
            pstmt.setBigDecimal(7, transaction.getTotalPrice());
            return pstmt.executeUpdate() > 0;
        }
    }

    public int getTotalTicketsSold(int museumId) throws SQLException {
        String sql = "SELECT SUM(quantity) FROM transactions WHERE museum_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, museumId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public BigDecimal getTotalRevenue(int museumId) throws SQLException {
        String sql = "SELECT SUM(total_price) FROM transactions WHERE museum_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, museumId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }
} 