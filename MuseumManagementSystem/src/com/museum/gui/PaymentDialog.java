package com.museum.gui;

import com.museum.dao.TransactionDAO;
import com.museum.model.Museum;
import com.museum.model.Transaction;
import com.museum.model.User;
import com.museum.model.Transaction;
import com.museum.dao.TransactionDAO;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class PaymentDialog extends JDialog {
    private Museum museum;
    private String visitorType;
    private User currentUser;
    
    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> typeCombo;
    private JSpinner quantitySpinner;
    private JLabel priceLabel, totalLabel;
    private JButton confirmButton, cancelButton;
    private boolean paymentSuccessful = false;

    public PaymentDialog(JFrame parent, Museum museum, String visitorType, User currentUser) {
        super(parent, "🎫 Purchase Tickets - " + museum.getName(), true);
        this.museum = museum;
        this.visitorType = visitorType;
        this.currentUser = currentUser;
        
        setSize(450, 450);
        setLocationRelativeTo(parent);
        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Title
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("🎫 Purchase Tickets");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, gbc);

        // Museum name
        gbc.gridy = row++;
        JLabel museumLabel = new JLabel("🏛️ " + museum.getName());
        museumLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        museumLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(museumLabel, gbc);

        // Separator
        gbc.gridy = row++;
        mainPanel.add(new JSeparator(), gbc);

        // User Information section
        gbc.gridy = row++;
        JLabel infoLabel = new JLabel("👤 Your Information");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(infoLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = row++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Full Name:*"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        if (currentUser != null) {
            nameField.setText(currentUser.getUsername());
        }
        mainPanel.add(nameField, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:*"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        mainPanel.add(emailField, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Phone:*"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        mainPanel.add(phoneField, gbc);

        // Separator
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        mainPanel.add(new JSeparator(), gbc);

        // Ticket details
        gbc.gridy = row++;
        JLabel ticketLabel = new JLabel("🎟️ Ticket Details");
        ticketLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(ticketLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = row++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Visitor Type:"), gbc);
        gbc.gridx = 1;
        String[] types = {"Local", "Foreign"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setSelectedItem(visitorType);
        typeCombo.addActionListener(e -> updateTotal());
        mainPanel.add(typeCombo, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantitySpinner.addChangeListener(e -> updateTotal());
        mainPanel.add(quantitySpinner, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Price per ticket:"), gbc);
        gbc.gridx = 1;
        priceLabel = new JLabel();
        mainPanel.add(priceLabel, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        JLabel totalPriceLabel = new JLabel("Total:");
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(totalPriceLabel, gbc);
        gbc.gridx = 1;
        totalLabel = new JLabel();
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(0x0A7E8C));
        mainPanel.add(totalLabel, gbc);

        // Buttons
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        confirmButton = new JButton("✅ Confirm Purchase");
        confirmButton.setBackground(new Color(0x0A7E8C));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.addActionListener(e -> processPayment());
        buttonPanel.add(confirmButton);

        cancelButton = new JButton("❌ Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        // Initial update
        updateTotal();

        add(mainPanel);
        pack();
    }

    private void updateTotal() {
        String type = (String) typeCombo.getSelectedItem();
        int qty = (int) quantitySpinner.getValue();
        
        BigDecimal price = type.equals("Local") ? museum.getLocalPrice() : museum.getForeignPrice();
        BigDecimal total = price.multiply(BigDecimal.valueOf(qty));
        
        priceLabel.setText(price + " ETB");
        totalLabel.setText(total + " ETB");
    }

    private void processPayment() {
    // Validate user info
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String phone = phoneField.getText().trim();

    if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "⚠️ Please fill in all required fields (Name, Email, Phone).",
            "Incomplete Information", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!email.contains("@") || !email.contains(".")) {
        JOptionPane.showMessageDialog(this,
            "⚠️ Please enter a valid email address.",
            "Invalid Email", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String type = (String) typeCombo.getSelectedItem();
    int qty = (int) quantitySpinner.getValue();
    BigDecimal price = type.equals("Local") ? museum.getLocalPrice() : museum.getForeignPrice();
    BigDecimal total = price.multiply(BigDecimal.valueOf(qty));

    // Show confirmation
    int confirm = JOptionPane.showConfirmDialog(this,
        "🎫 Confirm Purchase\n\n" +
        "Museum: " + museum.getName() + "\n" +
        "Name: " + name + "\n" +
        "Email: " + email + "\n" +
        "Phone: " + phone + "\n" +
        "Type: " + type + "\n" +
        "Quantity: " + qty + "\n" +
        "Total: " + total + " ETB\n\n" +
        "Thank you for supporting Ethiopian culture!",
        "Confirm Purchase", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Save transaction to database
            Transaction transaction = new Transaction(
                museum.getMuseumId(),
                name, email, phone,
                type, qty, total
            );
            
            TransactionDAO dao = new TransactionDAO();
            boolean saved = dao.saveTransaction(transaction);
            
            if (saved) {
                paymentSuccessful = true;
                JOptionPane.showMessageDialog(this,
                    "✅ Payment Successful!\n\n" +
                    "🎟️ " + qty + " ticket(s) purchased for " + museum.getName() + "\n" +
                    "📧 Confirmation sent to: " + email + "\n\n" +
                    "Thank you for visiting Ethiopian museums! 🙏",
                    "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ Payment failed. Please try again.",
                    "Payment Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Unexpected error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    public boolean isPaymentSuccessful() { return paymentSuccessful; }
} 
