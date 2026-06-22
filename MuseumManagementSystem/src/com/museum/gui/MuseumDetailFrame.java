package com.museum.gui;

import com.museum.model.Museum;
import com.museum.model.User;

import javax.swing.*;
import java.awt.*;

public class MuseumDetailFrame extends JFrame {
    private Museum museum;
    private User currentUser;
    private JFrame parent;

    public MuseumDetailFrame(JFrame parent, Museum museum, User currentUser) {
        this.parent = parent;
        this.museum = museum;
        this.currentUser = currentUser;
        
        setTitle("🏛️ " + museum.getName());
        setSize(700, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back to Museums");
        backButton.addActionListener(e -> dispose());
        topPanel.add(backButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Content panel with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String path = museum.getImagePath();
        if (path != null && !path.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0) {
                    Image img = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                }
            } catch (Exception e) {
                imageLabel.setText("🖼️ Image not available");
            }
        } else {
            imageLabel.setText("🖼️ No image available");
        }
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Museum Name
        JLabel nameLabel = new JLabel("🏛️ " + museum.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nameLabel);

        // Location
        JLabel locationLabel = new JLabel("📍 " + museum.getCity().getCityName() + ", Ethiopia");
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        locationLabel.setForeground(Color.GRAY);
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(locationLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Description section
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(BorderFactory.createTitledBorder("📖 About"));
        JTextArea descArea = new JTextArea(museum.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setRows(5);
        descPanel.add(new JScrollPane(descArea), BorderLayout.CENTER);
        contentPanel.add(descPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Opening hours
        JPanel hoursPanel = new JPanel(new GridLayout(1, 2));
        hoursPanel.setBorder(BorderFactory.createTitledBorder("⏰ Opening Hours"));
        hoursPanel.add(new JLabel("Opening: " + museum.getOpeningTime()));
        hoursPanel.add(new JLabel("Closing: " + museum.getClosingTime()));
        contentPanel.add(hoursPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Ticket Prices
        JPanel pricePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        pricePanel.setBorder(BorderFactory.createTitledBorder("💰 Ticket Prices"));

        JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(BorderFactory.createLineBorder(new Color(0x0A7E8C), 2));
        localPanel.add(new JLabel("👤 Local Visitors", SwingConstants.CENTER), BorderLayout.NORTH);
        JLabel localPriceLabel = new JLabel(museum.getLocalPrice() + " ETB", SwingConstants.CENTER);
        localPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        localPriceLabel.setForeground(new Color(0x0A7E8C));
        localPanel.add(localPriceLabel, BorderLayout.CENTER);
        JButton buyLocalButton = new JButton("🎫 Buy Ticket");
        buyLocalButton.addActionListener(e -> openPaymentDialog("Local"));
        localPanel.add(buyLocalButton, BorderLayout.SOUTH);
        pricePanel.add(localPanel);

        JPanel foreignPanel = new JPanel(new BorderLayout());
        foreignPanel.setBorder(BorderFactory.createLineBorder(new Color(0xD62828), 2));
        foreignPanel.add(new JLabel("🌍 Foreign Visitors", SwingConstants.CENTER), BorderLayout.NORTH);
        JLabel foreignPriceLabel = new JLabel(museum.getForeignPrice() + " ETB", SwingConstants.CENTER);
        foreignPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        foreignPriceLabel.setForeground(new Color(0xD62828));
        foreignPanel.add(foreignPriceLabel, BorderLayout.CENTER);
        JButton buyForeignButton = new JButton("🎫 Buy Ticket");
        buyForeignButton.addActionListener(e -> openPaymentDialog("Foreign"));
        foreignPanel.add(buyForeignButton, BorderLayout.SOUTH);
        pricePanel.add(foreignPanel);

        contentPanel.add(pricePanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Cultural Info
        JPanel culturalPanel = new JPanel(new BorderLayout());
        culturalPanel.setBorder(BorderFactory.createTitledBorder("📜 Cultural Information"));
        JTextArea culturalArea = new JTextArea(museum.getCulturalInfo());
        culturalArea.setEditable(false);
        culturalArea.setLineWrap(true);
        culturalArea.setWrapStyleWord(true);
        culturalArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        culturalArea.setRows(4);
        culturalPanel.add(new JScrollPane(culturalArea), BorderLayout.CENTER);
        contentPanel.add(culturalPanel);

        // Scroll pane for content
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }
/* 
    private void openPaymentDialog(String visitorType) {
        PaymentDialog dialog = new PaymentDialog(this, museum, visitorType, currentUser);
        dialog.setVisible(true);
    }
} */
}
