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
    // Services
    private MuseumService museumService = new MuseumService();
    private AuthenticationService authService = new AuthenticationService();

    // GUI components
    private JList<City> cityList;
    private DefaultListModel<City> cityListModel;
    private JList<Museum> museumList;
    private DefaultListModel<Museum> museumListModel;
    private JTextArea detailArea;
    private JLabel imageLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton, editButton, deleteButton;
    private JLabel statusLabel;
    private User currentUser = null;   // null means guest

    public MainFrame() {
        setTitle("Ethiopian Museum Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Set up the menu bar
        setupMenuBar();

        // Create main panels
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // North: Search bar
        JPanel northPanel = createNorthPanel();
        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Center: Split pane with city list, museum list, and detail panel
        JSplitPane splitPane = createCenterSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // South: Status bar
        statusLabel = new JLabel("Welcome! Select a city or search.");
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Load initial data
        refreshCityList();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Admin menu
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.addActionListener(e -> showLoginDialog());
        adminMenu.add(loginItem);

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        adminMenu.add(logoutItem);

        menuBar.add(adminMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Museum Management System\nVersion 1.0\nDeveloped for educational purposes."));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        panel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMuseums());
        panel.add(searchButton);

        // Add admin buttons (initially invisible)
        addButton = new JButton("Add");
        addButton.setEnabled(false);
        editButton = new JButton("Edit");
        editButton.setEnabled(false);
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        return panel;
    }

    private JSplitPane createCenterSplitPane() {
        // Left: city list
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
        cityScroll.setBorder(BorderFactory.createTitledBorder("Cities"));

        // Center-Left: museum list
        museumListModel = new DefaultListModel<>();
        museumList = new JList<>(museumListModel);
        museumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        museumList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Museum selected = museumList.getSelectedValue();
                if (selected != null) {
                    showMuseumDetail(selected);
                    enableAdminButtons(true);
                } else {
                    clearDetail();
                    enableAdminButtons(false);
                }
            }
        });
        JScrollPane museumScroll = new JScrollPane(museumList);
        museumScroll.setPreferredSize(new Dimension(200, 0));
        museumScroll.setBorder(BorderFactory.createTitledBorder("Museums"));

        // Right: detail panel
        JPanel detailPanel = new JPanel(new BorderLayout(5, 5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Museum Details"));

        imageLabel = new JLabel("No image", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        JScrollPane detailScroll = new JScrollPane(detailArea);

        // Combine image and text
        JPanel topDetail = new JPanel(new BorderLayout());
        topDetail.add(imageLabel, BorderLayout.WEST);
        topDetail.add(detailScroll, BorderLayout.CENTER);
        detailPanel.add(topDetail, BorderLayout.CENTER);

        // Split the two lists
        JSplitPane listSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                cityScroll, museumScroll);
        listSplit.setDividerLocation(150);

        // Split list split and detail panel
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listSplit, detailPanel);
        mainSplit.setDividerLocation(350);

        return mainSplit;
    }

    // ---------- Data Loading Methods ----------
    private void refreshCityList() {
        cityListModel.clear();
        try {
            List<City> cities = museumService.getAllCities();
            for (City c : cities) {
                cityListModel.addElement(c);
            }
            if (cities.size() > 0) {
                cityList.setSelectedIndex(0); // triggers load of first city's museums
            }
        } catch (SQLException ex) {
            showError("Failed to load cities: " + ex.getMessage());
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
                statusLabel.setText("No museums found for this city.");
            } else {
                statusLabel.setText("Loaded " + museums.size() + " museums.");
            }
        } catch (SQLException ex) {
            showError("Failed to load museums: " + ex.getMessage());
        }
    }

    private void searchMuseums() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            // Reload selected city
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
            statusLabel.setText("Search results: " + results.size() + " found.");
        } catch (SQLException ex) {
            showError("Search failed: " + ex.getMessage());
        }
    }

    // ---------- Display Detail ----------
    private void showMuseumDetail(Museum m) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(m.getName()).append("\n");
        sb.append("City: ").append(m.getCity().getCityName()).append("\n");
        sb.append("Description: ").append(m.getDescription()).append("\n");
        sb.append("Opening: ").append(m.getOpeningTime()).append("\n");
        sb.append("Closing: ").append(m.getClosingTime()).append("\n");
        sb.append("Ticket Prices:\n");
        sb.append("  Local: ").append(m.getLocalPrice()).append(" ETB\n");
        sb.append("  Foreign: ").append(m.getForeignPrice()).append(" ETB\n");
        sb.append("  Student: ").append(m.getStudentPrice()).append(" ETB\n");
        sb.append("Cultural Info:\n").append(m.getCulturalInfo());
        detailArea.setText(sb.toString());

        // Load image if exists
        String path = m.getImagePath();
        if (path != null && !path.isEmpty()) {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getIconWidth() > 0) {
                // Scale if too large
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Image not found");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("No image");
        }
    }

    private void clearDetail() {
        detailArea.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("No selection");
    }

    // ---------- Admin Button Controls ----------
    private void enableAdminButtons(boolean enabled) {
        boolean admin = (currentUser != null);
        addButton.setEnabled(admin);
        editButton.setEnabled(admin && enabled);
        deleteButton.setEnabled(admin && enabled);
    }

    // ---------- Login / Logout ----------
    private void showLoginDialog() {
        LoginDialog dialog = new LoginDialog(this, authService);
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            currentUser = dialog.getUser();
            statusLabel.setText("Logged in as " + currentUser.getUsername());
            enableAdminButtons(true);
        }
    }

    private void logout() {
        currentUser = null;
        statusLabel.setText("Logged out.");
        enableAdminButtons(false);
    }

    // ---------- Error handling ----------
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
} 
