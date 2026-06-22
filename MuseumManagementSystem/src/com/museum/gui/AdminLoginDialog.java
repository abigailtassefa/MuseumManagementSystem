package com.museum.gui;

import com.museum.model.User;
import com.museum.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton;
    private boolean authenticated = false;
    private User user = null;
    private AuthenticationService authService = new AuthenticationService();

    public AdminLoginDialog(JFrame parent) {
        super(parent, "🔐 Admin Login", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Title
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Admin Authentication");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = row++;
        gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Password
        gbc.gridy = row++;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Buttons
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("🔓 Login");
        cancelButton = new JButton("❌ Cancel");
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        // Forgot password hint
        gbc.gridy = row++;
        JLabel forgotLabel = new JLabel("⚠️ Forgot Password? Contact System Administrator");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        forgotLabel.setForeground(Color.RED);
        add(forgotLabel, gbc);

        // Actions
        loginButton.addActionListener(e -> doLogin());
        cancelButton.addActionListener(e -> dispose());

        // Enter key to login
        getRootPane().setDefaultButton(loginButton);

        pack();
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password.", 
                "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User u = authService.login(username, password);
            if (u != null) {
                user = u;
                authenticated = true;
                JOptionPane.showMessageDialog(this, 
                    "✅ Welcome " + u.getUsername() + "!", 
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Invalid username or password. Please try again.", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Unexpected error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAuthenticated() { return authenticated; }
    public User getUser() { return user; }
} 
