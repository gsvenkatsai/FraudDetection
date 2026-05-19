package com.frauddetection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class FraudDetectionApp extends JFrame {

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final UserDAO userDAO = new UserDAO();
    private final AlertDAO alertDAO = new AlertDAO();
    private final FraudEngine fraudEngine = new FraudEngine();

    private JLabel highCountLabel;
    private JLabel mediumCountLabel;
    private JLabel lowCountLabel;

    private DefaultTableModel transactionModel;
    private DefaultTableModel userModel;
    private DefaultTableModel alertModel;
    private JComboBox<String> severityFilter;

    public FraudDetectionApp() {
        setTitle("Fraud Detection System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Transactions", createTransactionsPanel());
        tabbedPane.addTab("Users", createUsersPanel());
        tabbedPane.addTab("Alerts", createAlertsPanel());

        // Automatically refresh data when switching tabs
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            switch (index) {
                case 0: refreshSummary(); break;
                case 1: refreshTransactions(transactionModel); break;
                case 2: refreshUsers(userModel); break;
                case 3: 
                    severityFilter.setSelectedItem("All");
                    refreshAlerts(alertModel, "All"); 
                    break;
            }
        });

        add(tabbedPane);
        refreshSummary();
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Fraud Detection System Dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout());
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Alert Summary"));
        
        highCountLabel = createCountLabel("High Severity", Color.RED);
        mediumCountLabel = createCountLabel("Medium Severity", new Color(200, 150, 0));
        lowCountLabel = createCountLabel("Low Severity", new Color(0, 150, 0));
        
        summaryPanel.add(highCountLabel);
        summaryPanel.add(mediumCountLabel);
        summaryPanel.add(lowCountLabel);
        
        mainContent.add(summaryPanel, BorderLayout.NORTH);

        // Action Panel
        JPanel actionPanel = new JPanel(new GridBagLayout());
        JButton runFraudBtn = new JButton("Run Fraud Detection Rules");
        runFraudBtn.setPreferredSize(new Dimension(300, 60));
        runFraudBtn.setBackground(new Color(220, 20, 60));
        runFraudBtn.setForeground(Color.WHITE);
        runFraudBtn.setFont(new Font("Arial", Font.BOLD, 16));
        
        runFraudBtn.addActionListener(e -> {
            try {
                int count = 0;
                List<List<Alert>> results = List.of(
                    fraudEngine.detectSpendingSpikes(),
                    fraudEngine.detectHighFrequency(),
                    fraudEngine.detectImpossibleTravel(),
                    fraudEngine.detectNewDeviceTransactions(),
                    fraudEngine.detectNightActivity(),
                    fraudEngine.detectFailedLoginClusters()
                );
                
                for (List<Alert> alerts : results) {
                    for (Alert a : alerts) {
                        alertDAO.insertAlert(a);
                        count++;
                    }
                }
                refreshSummary();
                JOptionPane.showMessageDialog(this, "Detection cycle complete. Logged " + count + " new alerts.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        actionPanel.add(runFraudBtn);
        mainContent.add(actionPanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        JButton initDbBtn = new JButton("Initialize/Reset Database");
        initDbBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "This will reset all data. Continue?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseInitializer.initialize();
                refreshSummary();
                JOptionPane.showMessageDialog(this, "Database initialized.");
            }
        });
        panel.add(initDbBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createCountLabel(String title, Color color) {
        JLabel label = new JLabel("<html><center>" + title + "<br/><font size='6'>0</font></center></html>", SwingConstants.CENTER);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return label;
    }

    private void refreshSummary() {
        Map<String, Integer> counts = alertDAO.getAlertCounts();
        highCountLabel.setText("<html><center>High Severity<br/><font size='6'>" + counts.get("high") + "</font></center></html>");
        mediumCountLabel.setText("<html><center>Medium Severity<br/><font size='6'>" + counts.get("medium") + "</font></center></html>");
        lowCountLabel.setText("<html><center>Low Severity<br/><font size='6'>" + counts.get("low") + "</font></center></html>");
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "User ID", "Amount", "Time", "Merchant", "Status"};
        transactionModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(transactionModel);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTransactions(transactionModel));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        refreshTransactions(transactionModel);
        return panel;
    }

    private void refreshTransactions(DefaultTableModel model) {
        if (model == null) return;
        model.setRowCount(0);
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                    t.getTransactionId(), t.getUserId(), t.getAmount(),
                    t.getTransactionTime(), t.getMerchant(), t.getStatus()
            });
        }
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Signup Date", "Country", "Status"};
        userModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(userModel);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshUsers(userModel));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        refreshUsers(userModel);
        return panel;
    }

    private void refreshUsers(DefaultTableModel model) {
        if (model == null) return;
        model.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            model.addRow(new Object[]{
                    u.getUserId(), u.getSignupDate(), u.getCountry(), u.getStatus()
            });
        }
    }

    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Severity: "));
        severityFilter = new JComboBox<>(new String[]{"All", "High", "Medium", "Low"});
        severityFilter.setSelectedItem("All"); // Explicit default
        filterPanel.add(severityFilter);
        
        panel.add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "User ID", "Type", "Time", "Severity"};
        alertModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(alertModel);
        
        // Row Coloring Renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object val = table.getModel().getValueAt(row, 4);
                String severity = (val != null) ? val.toString().toLowerCase() : "";
                
                if (!isSelected) {
                    switch (severity) {
                        case "high": c.setBackground(new Color(255, 200, 200)); break;
                        case "medium": c.setBackground(new Color(255, 255, 200)); break;
                        case "low": c.setBackground(new Color(200, 255, 200)); break;
                        default: c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        JButton refreshBtn = new JButton("Refresh Alerts");
        refreshBtn.addActionListener(e -> refreshAlerts(alertModel, severityFilter.getSelectedItem().toString()));
        severityFilter.addActionListener(e -> refreshAlerts(alertModel, severityFilter.getSelectedItem().toString()));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        refreshAlerts(alertModel, "All");
        return panel;
    }

    private void refreshAlerts(DefaultTableModel model, String severity) {
        if (model == null) return;
        model.setRowCount(0);
        List<Alert> alerts = alertDAO.getAlertsBySeverity(severity);
        for (Alert a : alerts) {
            model.addRow(new Object[]{
                    a.getAlertId(), a.getUserId(), a.getAlertType(),
                    a.getAlertTime(), a.getSeverity()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FraudDetectionApp().setVisible(true);
        });
    }
}
