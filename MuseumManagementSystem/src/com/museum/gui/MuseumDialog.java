package com.museum.gui;

import com.museum.model.Museum;
import com.museum.model.City;
import com.museum.service.MuseumService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;

public class MuseumDialog extends JDialog {
    private MuseumService service = new MuseumService();
    private Museum museum;
    private JTextField nameField;
    private JComboBox<City> cityCombo;
    private JTextArea descArea;
    private JTextField openField, closeField;
    private JTextField localField, foreignField;
    private JTextArea culturalArea;
    private JTextField imagePathField;
    private JButton saveButton, cancelButton;
    private boolean saved = false;

    public MuseumDialog(JFrame parent, Museum museumToEdit) {
        super(parent, (museumToEdit == null ? "Add" : "Edit") + " Museum", true);
        this.museum = museumToEdit;
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        cityCombo = new JComboBox<>();
        loadCities();
        add(cityCombo, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descArea = new JTextArea(3, 20);
        add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Opening (HH:MM):"), gbc);
        gbc.gridx = 1;
        openField = new JTextField(10);
        add(openField, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Closing (HH:MM):"), gbc);
        gbc.gridx = 1;
        closeField = new JTextField(10);
        add(closeField, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Local Price (ETB):"), gbc);
        gbc.gridx = 1;
        localField = new JTextField(10);
        add(localField, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Foreign Price (ETB):"), gbc);
        gbc.gridx = 1;
        foreignField = new JTextField(10);
        add(foreignField, gbc);

        // Student price field removed

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Cultural Info:"), gbc);
        gbc.gridx = 1;
        culturalArea = new JTextArea(4, 20);
        add(new JScrollPane(culturalArea), gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Image Path:"), gbc);
        gbc.gridx = 1;
        imagePathField = new JTextField(20);
        add(imagePathField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = row++;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        if (museum != null) {
            nameField.setText(museum.getName());
            cityCombo.setSelectedItem(museum.getCity());
            descArea.setText(museum.getDescription());
            openField.setText(museum.getOpeningTime().toString());
            closeField.setText(museum.getClosingTime().toString());
            localField.setText(museum.getLocalPrice().toString());
            foreignField.setText(museum.getForeignPrice().toString());
            culturalArea.setText(museum.getCulturalInfo());
            imagePathField.setText(museum.getImagePath());
        }

        saveButton.addActionListener(e -> saveMuseum());
        cancelButton.addActionListener(e -> dispose());

        pack();
    }

    private void loadCities() {
        try {
            List<City> cities = service.getAllCities();
            cityCombo.removeAllItems();
            for (City c : cities) {
                cityCombo.addItem(c);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading cities: " + ex.getMessage());
        }
    }

    private void saveMuseum() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Name is required.");
            City city = (City) cityCombo.getSelectedItem();
            if (city == null) throw new IllegalArgumentException("Please select a city.");
            String desc = descArea.getText();
            Time open = Time.valueOf(openField.getText() + ":00");
            Time close = Time.valueOf(closeField.getText() + ":00");
            BigDecimal local = new BigDecimal(localField.getText());
            BigDecimal foreign = new BigDecimal(foreignField.getText());
            String cultural = culturalArea.getText();
            String imagePath = imagePathField.getText().trim();

            Museum m = new Museum();
            m.setName(name);
            m.setDescription(desc);
            m.setCity(city);
            m.setOpeningTime(open);
            m.setClosingTime(close);
            m.setLocalPrice(local);
            m.setForeignPrice(foreign);
            m.setCulturalInfo(cultural);
            m.setImagePath(imagePath);

            if (museum == null) {
                service.addMuseum(m);
            } else {
                m.setMuseumId(museum.getMuseumId());
                service.updateMuseum(m);
            }
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Input error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }
} 