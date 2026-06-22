package com.museum.gui;

import java.awt.*;
import javax.swing.*;

public class WelcomeFrame extends JFrame {
    private JButton guestButton, adminButton;

    public WelcomeFrame() {
        setTitle("Ethiopian Museum Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(0x078930), // Ethiopian green
                    0, getHeight(), new Color(0xFCDD09) // Ethiopian yellow
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("🏛️ ETHIOPIAN MUSEUM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("MANAGEMENT SYSTEM");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        subtitleLabel.setForeground(Color.WHITE);
        mainPanel.add(subtitleLabel, gbc);

        // Logo/Image placeholder
        gbc.gridy = 2;
        JLabel imageLabel = new JLabel("🏛️", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        mainPanel.add(imageLabel, gbc);

        // Tagline
        gbc.gridy = 3;
        JLabel tagline = new JLabel("Discover Ethiopia's Rich Cultural Heritage");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        tagline.setForeground(Color.WHITE);
        mainPanel.add(tagline, gbc);

        // Buttons panel
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        // Guest Button
        guestButton = new JButton("👤 Enter as Guest");
        guestButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guestButton.setBackground(new Color(0x0A7E8C));
        guestButton.setForeground(Color.WHITE);
        guestButton.setFocusPainted(false);
        guestButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        guestButton.addActionListener(e -> openGuestMode());
        buttonPanel.add(guestButton);

        // Admin Button
        adminButton = new JButton("🔐 Admin Login");
        adminButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        adminButton.setBackground(new Color(0xD62828));
        adminButton.setForeground(Color.WHITE);
        adminButton.setFocusPainted(false);
        adminButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        adminButton.addActionListener(e -> openAdminLogin());
        buttonPanel.add(adminButton);

        mainPanel.add(buttonPanel, gbc);

        // Version
        gbc.gridy = 5;
        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(Color.WHITE);
        mainPanel.add(versionLabel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void openGuestMode() {
        new MainFrame(null); // null means guest
        dispose();
    }

    private void openAdminLogin() {
        AdminLoginDialog dialog = new AdminLoginDialog(this);
        dialog.setVisible(true);
        if (dialog.isAuthenticated()) {
            new MainFrame(dialog.getUser());
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame());
    }
}
