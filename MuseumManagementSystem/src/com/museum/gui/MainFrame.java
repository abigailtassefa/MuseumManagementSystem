package com.museum.gui;

import com.museum.model.Museum;
import com.museum.model.City;
import com.museum.model.User;
import com.museum.service.MuseumService;
import com.museum.service.AuthenticationService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private MuseumService museumService = new MuseumService();
    private AuthenticationService authService = new AuthenticationService();

    private JList<City> cityList;
    private DefaultListModel<City> cityListModel;
    private JList<Museum> museumList;
    private DefaultListModel<Museum> museumListModel;
    private JTextArea previewArea;
    private JLabel previewImageLabel;
    private JTextField searchField;
    private JButton searchButton, refreshButton;
    private JButton addButton, editButton, deleteButton;
    private JLabel statusLabel, userLabel;
    private User currentUser = null;
    private String guestName = "Guest";

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Ethiopian Museum Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        setupMenuBar();
        setupMainUI();

        refreshCityList();
        setVisible(true);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", 
                "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Admin menu
        JMenu adminMenu = new JMenu("Admin");
        if (currentUser == null) {
            JMenuItem loginItem = new JMenuItem("Login");
            loginItem.addActionListener(e -> showAdminLogin());
            adminMenu.add(loginItem);
        } else {
            JMenuItem logoutItem = new JMenuItem("Logout");
            logoutItem.addActionListener(e -> logout());
            adminMenu.add(logoutItem);
        }
        menuBar.add(adminMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void setupMainUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // North panel
        JPanel northPanel = createNorthPanel();
        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Center split pane
        JSplitPane splitPane = createCenterSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // South panel (status)
        JPanel southPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Welcome! Select a city to explore.");
        southPanel.add(statusLabel, BorderLayout.WEST);
        
        userLabel = new JLabel("👤 " + (currentUser != null ? currentUser.getUsername() : "Guest"));
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        southPanel.add(userLabel, BorderLayout.EAST);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("🔍 Search:"));
        searchField = new JTextField(20);
        panel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMuseums());
        panel.add(searchButton);

        refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> refreshCityList());
        panel.add(refreshButton);

        // Admin buttons
        addButton = new JButton("➕ Add");
        addButton.setEnabled(currentUser != null);
        editButton = new JButton("✏️ Edit");
        editButton.setEnabled(false);
        deleteButton = new JButton("🗑️ Delete");
        deleteButton.setEnabled(false);

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        // Admin button actions
        addButton.addActionListener(e -> showAddMuseumDialog());
        editButton.addActionListener(e -> showEditMuseumDialog());
        deleteButton.addActionListener(e -> deleteMuseum());

        // Search on Enter key
        searchField.addActionListener(e -> searchMuseums());

        return panel;
    }

    private JSplitPane createCenterSplitPane() {
        // City list
        cityListModel = new DefaultListModel<>();
        cityList = new JList<>(cityListModel);
        cityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cityList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                City selected = cityList.getSelectedValue();
                if (selected != null) {
                    loadMuseumsByCity(selected.getCityId());
                }
            }
        });
        JScrollPane cityScroll = new JScrollPane(cityList);
        cityScroll.setPreferredSize(new Dimension(150, 0));
        cityScroll.setBorder(BorderFactory.createTitledBorder("📍 Cities"));

        // Museum list
        museumListModel = new DefaultListModel<>();
        museumList = new JList<>(museumListModel);
        museumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        museumList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Museum selected = museumList.getSelectedValue();
                if (selected != null) {
                    showMuseumPreview(selected);
                    enableAdminButtons(true);
                } else {
                    clearPreview();
                    enableAdminButtons(false);
                }
            }
        });
        JScrollPane museumScroll = new JScrollPane(museumList);
        museumScroll.setPreferredSize(new Dimension(200, 0));
        museumScroll.setBorder(BorderFactory.createTitledBorder("🏛️ Museums"));

        // Preview panel
        JPanel previewPanel = createPreviewPanel();

        // Split panes
        JSplitPane leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cityScroll, museumScroll);
        leftSplit.setDividerLocation(150);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, previewPanel);
        mainSplit.setDividerLocation(350);

        return mainSplit;
    }

    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("📋 Quick Preview"));

        // Image
        previewImageLabel = new JLabel("No image", SwingConstants.CENTER);
        previewImageLabel.setPreferredSize(new Dimension(180, 150));
        previewImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Text area
        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        previewArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane textScroll = new JScrollPane(previewArea);

        // View Details button
        JButton viewDetailsButton = new JButton("📖 View Full Details →");
        viewDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewDetailsButton.setBackground(new Color(0x0A7E8C));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.addActionListener(e -> {
            Museum selected = museumList.getSelectedValue();
            if (selected != null) {
                new MuseumDetailFrame(this, selected, currentUser);
            }
        });

        // Combine
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(previewImageLabel, BorderLayout.CENTER);
        topPanel.add(viewDetailsButton, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(textScroll, BorderLayout.CENTER);

        return panel;
    }

    private void showMuseumPreview(Museum m) {
        StringBuilder sb = new StringBuilder();
        sb.append("🏛️ ").append(m.getName()).append("\n");
        sb.append("📍 ").append(m.getCity().getCityName()).append("\n");
        sb.append("⏰ ").append(m.getOpeningTime()).append(" - ").append(m.getClosingTime()).append("\n");
        sb.append("💰 Local: ").append(m.getLocalPrice()).append(" ETB\n");
        sb.append("💰 Foreign: ").append(m.getForeignPrice()).append(" ETB\n");
        sb.append("\n📖 ").append(m.getDescription());
        previewArea.setText(sb.toString());

        // Load image
        String path = m.getImagePath();
        if (path != null && !path.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0) {
                    Image img = icon.getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
                    previewImageLabel.setIcon(new ImageIcon(img));
                    previewImageLabel.setText("");
                } else {
                    previewImageLabel.setIcon(null);
                    previewImageLabel.setText("🖼️ No Image");
                }
            } catch (Exception e) {
                previewImageLabel.setIcon(null);
                previewImageLabel.setText("🖼️ No Image");
            }
        } else {
            previewImageLabel.setIcon(null);
            previewImageLabel.setText("🖼️ No Image");
        }
    }

    private void clearPreview() {
        previewArea.setText("");
        previewImageLabel.setIcon(null);
        previewImageLabel.setText("No selection");
    }

    private void enableAdminButtons(boolean enabled) {
        boolean admin = (currentUser != null);
        addButton.setEnabled(admin);
        editButton.setEnabled(admin && enabled);
        deleteButton.setEnabled(admin && enabled);
    }

    // ---------- Data Loading ----------
    private void refreshCityList() {
        cityListModel.clear();
        try {
            List<City> cities = museumService.getAllCities();
            for (City c : cities) {
                cityListModel.addElement(c);
            }
            if (cities.size() > 0) {
                cityList.setSelectedIndex(0);
            }
            statusLabel.setText("✅ Loaded " + cities.size() + " cities.");
        } catch (SQLException ex) {
            showError("Failed to load cities: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    private void loadMuseumsByCity(int cityId) {
        museumListModel.clear();
        try {
            List<Museum> museums = museumService.getMuseumsByCity(cityId);
            for (Museum m : museums) {
                museumListModel.addElement(m);
            }
            if (museums.isEmpty()) {
                statusLabel.setText("ℹ️ No museums found for this city.");
            } else {
                statusLabel.setText("✅ Loaded " + museums.size() + " museums.");
            }
        } catch (SQLException ex) {
            showError("Failed to load museums: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    private void searchMuseums() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            City selected = cityList.getSelectedValue();
            if (selected != null) loadMuseumsByCity(selected.getCityId());
            return;
        }
        museumListModel.clear();
        try {
            List<Museum> results = museumService.searchMuseums(keyword);
            for (Museum m : results) {
                museumListModel.addElement(m);
            }
            statusLabel.setText("🔍 Search results: " + results.size() + " found.");
        } catch (SQLException ex) {
            showError("Search failed: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    // ---------- Admin Actions ----------
    private void showAddMuseumDialog() {
        try {
            MuseumDialog dialog = new MuseumDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshCityList();
                JOptionPane.showMessageDialog(this, 
                    "✅ Museum added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            showError("Error opening dialog: " + ex.getMessage());
        }
    }

    private void showEditMuseumDialog() {
        Museum selected = museumList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a museum to edit.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            MuseumDialog dialog = new MuseumDialog(this, selected);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshCityList();
                JOptionPane.showMessageDialog(this, 
                    "✅ Museum updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            showError("Error opening dialog: " + ex.getMessage());
        }
    }

    private void deleteMuseum() {
        Museum selected = museumList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a museum to delete.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete '" + selected.getName() + "'?\nThis action cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = museumService.deleteMuseum(selected.getMuseumId());
                if (deleted) {
                    refreshCityList();
                    JOptionPane.showMessageDialog(this, 
                        "✅ Museum deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("Failed to delete museum.");
                }
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            } catch (Exception ex) {
                showError("Unexpected error: " + ex.getMessage());
            }
        }
    }

    // ---------- Login/Logout ----------
    private void showAdminLogin() {
        AdminLoginDialog dialog = new AdminLoginDialog(this);
        dialog.setVisible(true);
        if (dialog.isAuthenticated()) {
            currentUser = dialog.getUser();
            userLabel.setText("👤 " + currentUser.getUsername());
            enableAdminButtons(true);
            statusLabel.setText("✅ Logged in as " + currentUser.getUsername());
            JOptionPane.showMessageDialog(this, 
                "✅ Welcome " + currentUser.getUsername() + "!", 
                "Login Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            currentUser = null;
            userLabel.setText("👤 Guest");
            enableAdminButtons(false);
            statusLabel.setText("✅ Logged out successfully.");
            JOptionPane.showMessageDialog(this, 
                "You have been logged out.", 
                "Logout Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------- Utility ----------
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "🏛️ Ethiopian Museum Management System\n" +
            "Version 1.0\n" +
            "Developed for educational purposes.\n\n" +
            "Discover Ethiopia's rich cultural heritage.",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, 
            "❌ " + msg, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
} 