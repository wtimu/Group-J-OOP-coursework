import java.time.LocalDate;

public class SavingsAccount extends Account {
    
    public SavingsAccount() {
        super();
    }
    
    public SavingsAccount(String firstName, String lastName, String nin, String email,
                          String phoneNumber, LocalDate dateOfBirth, String branch,
                          double openingDeposit, String pin) {
        super(firstName, lastName, nin, email, phoneNumber, dateOfBirth, 
              "Savings", branch, openingDeposit, pin);
    }
    
    @Override
    public double minimumDeposit() {
        return 50000.0; // 50,000 UGX
    }
    
    @Override
    public String getAccountCode() {
        return "SAV";
    }
}