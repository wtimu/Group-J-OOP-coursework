import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BankApplicationUI extends JFrame {
    
    // Form Fields
    private JTextField firstNameField, lastNameField, ninField, emailField, 
                       confirmEmailField, phoneField, depositField;
    private JPasswordField pinField, confirmPinField;
    private JComboBox<Integer> yearCombo, dayCombo;
    private JComboBox<String> monthCombo, accountTypeCombo, branchCombo;
    private JTextArea summaryArea;
    private JLabel globalErrorLabel, ageDisplayLabel;
    private JPanel dobPanel;
    private DatabaseManager dbManager;
    
    // Special Rule Variable for Joint Accounts
    private String coApplicantNin = ""; 
    
    // Map to link each UI component to its dedicated side-aligned error label
    private Map<JComponent, JLabel> fieldErrorMap = new HashMap<>();
    
    public BankApplicationUI() {
        setTitle("First Bank Uganda - New Account Opening Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 780); // Width comfortably accommodates inline errors
        setLocationRelativeTo(null);
        
        dbManager = new DatabaseManager();
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        JLabel headerLabel = new JLabel("FIRST BANK UGANDA - NEW ACCOUNT OPENING FORM");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 51, 102));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel formPanel = createFormPanel();
        
        globalErrorLabel = new JLabel();
        globalErrorLabel.setForeground(Color.RED);
        globalErrorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        globalErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitButton = new JButton("Submit Application");
        submitButton.setBackground(Color.white);
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
        
        JButton resetButton = new JButton("Reset Form");
        resetButton.setBackground(Color.WHITE);
        resetButton.setForeground(Color.RED);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFocusPainted(false);
        
        buttonPanel.add(submitButton);
        buttonPanel.add(resetButton);
        
        JLabel summaryHeader = new JLabel("Account Summary is Below:");
        summaryHeader.setFont(new Font("Arial", Font.BOLD, 14));
        summaryHeader.setForeground(new Color(0, 51, 102));
        summaryHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        summaryArea = new JTextArea(5, 50);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        summaryArea.setBackground(new Color(240, 248, 255));
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(globalErrorLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(summaryHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(summaryScroll);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
        
        submitButton.addActionListener(e -> handleSubmit());
        resetButton.addActionListener(e -> handleReset());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (dbManager != null) {
                    dbManager.close();
                }
            }
        });
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 51, 102), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        ninField = new JTextField(20);
        emailField = new JTextField(20);
        confirmEmailField = new JTextField(20);
        phoneField = new JTextField(20);
        pinField = new JPasswordField(20);
        confirmPinField = new JPasswordField(20);
        depositField = new JTextField(20);
        
        yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedIndex(-1);
        
        monthCombo = new JComboBox<>(new String[]{
            "Select Month", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        
        dayCombo = new JComboBox<>();
        dayCombo.setEnabled(false);
        
        accountTypeCombo = new JComboBox<>(new String[]{
            "Select Account Type", "Savings", "Current", "Fixed Deposit", "Student", "Joint"
        });
        
        branchCombo = new JComboBox<>(new String[]{
            "Select Branch", "Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"
        });
        
        yearCombo.addActionListener(e -> {
            updateDays();
            updateAgeDisplay();
        });
        monthCombo.addActionListener(e -> {
            updateDays();
            updateAgeDisplay();
        });
        dayCombo.addActionListener(e -> updateAgeDisplay());
        
        // Dynamic event hook handling MS Access requirements for Joint profiles
        accountTypeCombo.addActionListener(e -> {
            if ("Joint".equals(accountTypeCombo.getSelectedItem())) {
                String input = JOptionPane.showInputDialog(this, 
                    "Enter Co-Applicant National ID (Second NIN):", 
                    "Joint Account Requirement", 
                    JOptionPane.QUESTION_MESSAGE);
                this.coApplicantNin = (input != null) ? input.trim().toUpperCase() : "";
            } else {
                this.coApplicantNin = ""; 
            }
        });
        
        int row = 0;
        addFormField(panel, gbc, row++, "First Name:*", firstNameField);
        addFormField(panel, gbc, row++, "Last Name:*", lastNameField);
        addFormField(panel, gbc, row++, "National ID (NIN):*", ninField);
        addFormField(panel, gbc, row++, "Email:*", emailField);
        addFormField(panel, gbc, row++, "Confirm Email:*", confirmEmailField);
        addFormField(panel, gbc, row++, "Phone Number:*", phoneField);
        addFormField(panel, gbc, row++, "PIN:*", pinField);
        addFormField(panel, gbc, row++, "Confirm PIN:*", confirmPinField);
        
        dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dobPanel.setBackground(Color.WHITE);
        dobPanel.add(yearCombo);
        dobPanel.add(monthCombo);
        dobPanel.add(dayCombo);
        
        ageDisplayLabel = new JLabel("Age: --");
        ageDisplayLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        ageDisplayLabel.setForeground(new Color(0, 51, 102));
        dobPanel.add(Box.createHorizontalStrut(20));
        dobPanel.add(ageDisplayLabel);
        
        addFormField(panel, gbc, row++, "Date of Birth:*", dobPanel);
        
        addFormField(panel, gbc, row++, "Account Type:*", accountTypeCombo);
        addFormField(panel, gbc, row++, "Branch:*", branchCombo);
        addFormField(panel, gbc, row++, "Opening Deposit (UGX):*", depositField);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
                              String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.2;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        panel.add(field, gbc);

        gbc.gridx = 2; gbc.weightx = 0.5;
        JLabel fieldSpecificError = new JLabel("");
        fieldSpecificError.setForeground(Color.RED);
        fieldSpecificError.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(fieldSpecificError, gbc);

        fieldErrorMap.put(field, fieldSpecificError);
    }
    
    private void updateDays() {
        dayCombo.removeAllItems();
        dayCombo.setEnabled(false);
        if (yearCombo.getSelectedIndex() >= 0 && monthCombo.getSelectedIndex() > 0) {
            int year = (Integer) yearCombo.getSelectedItem();
            int month = monthCombo.getSelectedIndex(); 
            int daysInMonth = InputValidator.getDaysInMonth(year, month);
            for (int i = 1; i <= daysInMonth; i++) {
                dayCombo.addItem(i);
            }
            dayCombo.setEnabled(true);
        }
    }
    
    private void updateAgeDisplay() {
        if (yearCombo.getSelectedIndex() < 0 || monthCombo.getSelectedIndex() <= 0 || dayCombo.getSelectedIndex() < 0) {
            ageDisplayLabel.setText("Age: --");
            return;
        }
        
        int year = (Integer) yearCombo.getSelectedItem();
        int month = monthCombo.getSelectedIndex();
        int day = (Integer) dayCombo.getSelectedItem();
        
        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();
        
        int age = today.getYear() - birthDate.getYear();
        if (today.getMonthValue() < birthDate.getMonthValue() || 
            (today.getMonthValue() == birthDate.getMonthValue() && today.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }
        
        ageDisplayLabel.setText("Age: " + age + " years");
    }
    
    private void clearAllErrors() {
        globalErrorLabel.setText("");
        for (JLabel errLabel : fieldErrorMap.values()) {
            errLabel.setText("");
        }
    }

    private void handleSubmit() {
        clearAllErrors();
        
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String nin = ninField.getText().toUpperCase();
        String email = emailField.getText();
        String confirmEmail = confirmEmailField.getText();
        String phone = phoneField.getText();
        String pin = new String(pinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());
        String depositStr = depositField.getText();
        
        int year = yearCombo.getSelectedIndex() >= 0 ? (Integer) yearCombo.getSelectedItem() : 0;
        int month = monthCombo.getSelectedIndex();
        int day = dayCombo.getSelectedItem() != null ? (Integer) dayCombo.getSelectedItem() : 0;
        
        String accountType = accountTypeCombo.getSelectedIndex() > 0 ? 
                           (String) accountTypeCombo.getSelectedItem() : null;
        String branch = branchCombo.getSelectedIndex() > 0 ? 
                       (String) branchCombo.getSelectedItem() : null;
        
        InputValidator validator = new InputValidator();
        boolean isValid = validator.validateAll(firstName, lastName, nin, email, 
                                               confirmEmail, phone, pin, confirmPin,
                                               year, month, day, accountType, branch, 
                                               depositStr, this.coApplicantNin);
        
        if (!isValid) {
            List<String> errors = validator.getErrors();
            
            for (String error : errors) {
                String lowerError = error.toLowerCase();
                
                if (lowerError.contains("deposit")) {
                    fieldErrorMap.get(depositField).setText(error);
                } else if (lowerError.contains("first name")) {
                    fieldErrorMap.get(firstNameField).setText(error);
                } else if (lowerError.contains("last name")) {
                    fieldErrorMap.get(lastNameField).setText(error);
                } else if (lowerError.contains("co-applicant") || lowerError.contains("second nin")) {
                    fieldErrorMap.get(accountTypeCombo).setText(error);
                } else if (lowerError.contains("nin") || lowerError.contains("national id")) {
                    fieldErrorMap.get(ninField).setText(error);
                } else if (lowerError.contains("confirm email") || lowerError.contains("must match") && lowerError.contains("email")) {
                    fieldErrorMap.get(confirmEmailField).setText(error);
                } else if (lowerError.contains("email")) {
                    fieldErrorMap.get(emailField).setText(error);
                } else if (lowerError.contains("phone")) {
                    fieldErrorMap.get(phoneField).setText(error);
                } else if (lowerError.contains("confirm pin") || lowerError.contains("must match") && lowerError.contains("pin")) {
                    fieldErrorMap.get(confirmPinField).setText(error);
                } else if (lowerError.contains("pin")) {
                    fieldErrorMap.get(pinField).setText(error);
                } else if (lowerError.contains("birth") || lowerError.contains("age") || lowerError.contains("date")) {
                    fieldErrorMap.get(dobPanel).setText(error);
                } else if (lowerError.contains("account type")) {
                    fieldErrorMap.get(accountTypeCombo).setText(error);
                } else if (lowerError.contains("branch")) {
                    fieldErrorMap.get(branchCombo).setText(error);
                }
            }
            
            globalErrorLabel.setText("Please fix the highlighted fields above.");
            
            StringBuilder dialogMessage = new StringBuilder("Please fix the following problems:\n\n");
            for (String error : errors) {
                dialogMessage.append("• ").append(error).append("\n");
            }
            
            JOptionPane.showMessageDialog(this, dialogMessage.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            
        } else {
            try {
                Account account = createAccount(firstName.trim(), lastName.trim(), 
                                               nin.trim(), email.trim(), phone.trim(),
                                               year, month, day, accountType, branch,
                                               Double.parseDouble(depositStr.trim()), pin,
                                               this.coApplicantNin);
                
                dbManager.saveAccount(account);
                summaryArea.setText(account.toString());
                
                JOptionPane.showMessageDialog(this,
                    "Account Number: " + account.getAccountNumber() + "\n\n" + account.toString(),
                    "Account Created Successfully", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                globalErrorLabel.setText("Error: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Account createAccount(String firstName, String lastName, String nin,
                                 String email, String phone, int year, int month, 
                                 int day, String accountType, String branch,
                                 double deposit, String pin, String coApplicantNin) {
        LocalDate dob = LocalDate.of(year, month, day);
        switch (accountType) {
            case "Savings": return new SavingsAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, pin);
            case "Current": return new CurrentAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, pin);
            case "Fixed Deposit": return new FixedDepositAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, pin);
            case "Student": return new StudentAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, pin);
            case "Joint": 
                return new JointAccount(firstName, lastName, nin, coApplicantNin, email, phone, dob, branch, deposit, pin);
            default: return null;
        }
    }
    
    private void handleReset() {
        firstNameField.setText("");
        lastNameField.setText("");
        ninField.setText("");
        emailField.setText("");
        confirmEmailField.setText("");
        phoneField.setText("");
        pinField.setText("");
        confirmPinField.setText("");
        depositField.setText("");
        yearCombo.setSelectedIndex(-1);
        monthCombo.setSelectedIndex(0);
        dayCombo.removeAllItems();
        dayCombo.setEnabled(false);
        accountTypeCombo.setSelectedIndex(0);
        branchCombo.setSelectedIndex(0);
        summaryArea.setText("");
        this.coApplicantNin = ""; 
        clearAllErrors();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new BankApplicationUI().setVisible(true);
        });
    }
}