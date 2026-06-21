import java.time.LocalDate;

public abstract class Account {
    protected String accountNumber;
    protected String firstName;
    protected String lastName;
    protected String nin;
    protected String email;
    protected String phoneNumber;
    protected LocalDate dateOfBirth;
    protected String accountType;
    protected String branch;
    protected double openingDeposit;
    protected String pin;
    
    public Account() {
    }
    
    public Account(String firstName, String lastName, String nin, String email, 
                   String phoneNumber, LocalDate dateOfBirth, String accountType, 
                   String branch, double openingDeposit, String pin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.accountType = accountType;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
        this.pin = pin;
    }
    
    // Abstract method that each account type must implement
    public abstract double minimumDeposit();
    public abstract String getAccountCode();
    
    public String getAccountNumber() { 
        return accountNumber; 
    }
    
    public void setAccountNumber(String accountNumber) { 
        this.accountNumber = accountNumber; 
    }
    
    public String getFirstName() { 
        return firstName; 
    }
    
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }
    
    public String getLastName() { 
        return lastName; 
    }
    
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }
    
    public String getNin() { 
        return nin; 
    }
    
    public void setNin(String nin) { 
        this.nin = nin; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    
    public LocalDate getDateOfBirth() { 
        return dateOfBirth; 
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) { 
        this.dateOfBirth = dateOfBirth; 
    }
    
    public String getAccountType() { 
        return accountType; 
    }
    
    public void setAccountType(String accountType) { 
        this.accountType = accountType; 
    }
    
    public String getBranch() { 
        return branch; 
    }
    
    public void setBranch(String branch) { 
        this.branch = branch; 
    }
    
    public double getOpeningDeposit() { 
        return openingDeposit; 
    }
    
    public void setOpeningDeposit(double openingDeposit) { 
        this.openingDeposit = openingDeposit; 
    }
    
    public String getPin() { 
        return pin; 
    }
    
    public void setPin(String pin) { 
        this.pin = pin; 
    }
    
    public int getAge() {
        LocalDate now = LocalDate.now();
        int age = now.getYear() - dateOfBirth.getYear();
        if (now.getDayOfYear() < dateOfBirth.getDayOfYear()) {
            age--;
        }
        return age;
    }
    
    @Override
    public String toString() {
        return String.format("ACC: %s | %s %s | %s | %s | DOB %s (Age: %d) | %s | Deposit %,.0f | Email: %s",
                accountNumber, firstName, lastName, accountType, branch,
                dateOfBirth.toString(), getAge(), phoneNumber, openingDeposit, email);
    }
}