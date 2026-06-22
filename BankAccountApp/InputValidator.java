import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class InputValidator {
    private List<String> errors;
    
    public InputValidator() {
        errors = new ArrayList<>();
    }
    
    public boolean validateAll(String firstName, String lastName, String nin, 
                               String email, String confirmEmail, String phone, 
                               String pin, String confirmPin, int year, int month, 
                               int day, String accountType, String branch, 
                               String depositStr, String secondNin) {
        errors.clear();
        
        // Validate First Name
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First Name is required");
        } else if (!firstName.trim().matches("[a-zA-Z]{2,30}")) {
            errors.add("First Name must be 2-30 letters only");
        }
        
        // Validate Last Name
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last Name is required");
        } else if (!lastName.trim().matches("[a-zA-Z]{2,30}")) {
            errors.add("Last Name must be 2-30 letters only");
        }
        
        // Validate NIN
        if (nin == null || nin.trim().isEmpty()) {
            errors.add("National ID (NIN) is required");
        } else if (!nin.trim().matches("[A-Z0-9]{14}")) {
            errors.add("NIN must be exactly 14 UPPERCASE alphanumeric characters");
        }
        
        // Validate Email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!email.trim().matches(emailRegex)) {
            errors.add("Invalid email format");
        } else if (!email.trim().equals(confirmEmail != null ? confirmEmail.trim() : "")) {
            errors.add("Email and Confirm Email must match");
        }
        
        // Validate Phone (+256XXXXXXXXX format)
        if (phone == null || phone.trim().isEmpty()) {
            errors.add("Phone Number is required");
        } else if (!phone.trim().matches("\\+256[0-9]{9}")) {
            errors.add("Phone must be in format +256XXXXXXXXX (12 digits after +256)");
        }
        
        // Validate PIN
        if (pin == null || pin.trim().isEmpty()) {
            errors.add("PIN is required");
        } else if (!pin.trim().matches("[0-9]{4,6}")) {
            errors.add("PIN must be 4-6 digits");
        } else if (!pin.trim().equals(confirmPin != null ? confirmPin.trim() : "")) {
            errors.add("PIN and Confirm PIN must match");
        } else if (isAllSameDigits(pin.trim())) {
            errors.add("PIN cannot be all identical digits (e.g., 0000)");
        }
        
        // Validate Date of Birth
        int age = 0;
        if (year == 0 || month == 0 || day == 0) {
            errors.add("Complete Date of Birth is required");
        } else {
            try {
                LocalDate dob = LocalDate.of(year, month, day);
                age = calculateAge(dob);
                
                if (age < 18 || age > 75) {
                    errors.add("Age must be between 18 and 75 (calculated age: " + age + ")");
                }
                
                // Special Rule for Student account age
                if ("Student".equals(accountType) && (age < 18 || age > 25)) {
                    errors.add("Student account requires age between 18 and 25");
                }
            } catch (Exception e) {
                errors.add("Invalid date: " + e.getMessage());
            }
        }
        
        // Validate Account Type Selection
        if (accountType == null || accountType.isEmpty() || accountType.equals("Select Account Type")) {
            errors.add("Account Type is required");
        } else if ("Joint".equals(accountType)) {
            // Explicit Special Rule Validation: Requires a Second NIN string parameter check
            if (secondNin == null || secondNin.trim().isEmpty()) {
                errors.add("Joint accounts require a Co-Applicant National ID (Second NIN)");
            } else if (!secondNin.trim().matches("[A-Z0-9]{14}")) {
                errors.add("Second NIN must be exactly 14 UPPERCASE alphanumeric characters");
            } else if (secondNin.trim().equalsIgnoreCase(nin)) {
                errors.add("Primary NIN and Second NIN cannot be identical");
            }
        }
        
        // Validate Branch
        if (branch == null || branch.isEmpty() || branch.equals("Select Branch")) {
            errors.add("Branch is required");
        }
        
        // Validate Opening Deposit & Minimum Deposit Tiers By Account Type
        if (depositStr == null || depositStr.trim().isEmpty()) {
            errors.add("Opening Deposit is required");
        } else {
            try {
                double deposit = Double.parseDouble(depositStr.trim());
                double minimum = getMinimumDeposit(accountType);
                
                if (deposit < minimum) {
                    errors.add(String.format("Minimum deposit for %s account is %,d UGX (entered: %,.0f)", 
                                           accountType, (int)minimum, deposit));
                }
            } catch (NumberFormatException e) {
                errors.add("Opening Deposit must be a valid number");
            }
        }
        
        return errors.isEmpty();
    }
    
    private boolean isAllSameDigits(String str) {
        if (str == null || str.isEmpty()) return false;
        char first = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != first) return false;
        }
        return true;
    }
    
    private int calculateAge(LocalDate dob) {
        LocalDate now = LocalDate.now();
        int age = now.getYear() - dob.getYear();
        if (now.getDayOfYear() < dob.getDayOfYear()) {
            age--;
        }
        return age;
    }
    
    private double getMinimumDeposit(String accountType) {
        if (accountType == null) return 0;
        switch (accountType) {
            case "Savings": return 50000.0;       
            case "Current": return 200000.0;      
            case "Fixed Deposit": return 1000000.0; 
            case "Student": return 10000.0;       
            case "Joint": return 100000.0;        
            default: return 0;
        }
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    public static int getDaysInMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }
}