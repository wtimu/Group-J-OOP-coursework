import java.time.LocalDate;

public class FixedDepositAccount extends Account {
    
    public FixedDepositAccount() {
        super();
    }
    
    public FixedDepositAccount(String firstName, String lastName, String nin, String email,
                               String phoneNumber, LocalDate dateOfBirth, String branch,
                               double openingDeposit, String pin) {
        super(firstName, lastName, nin, email, phoneNumber, dateOfBirth,
              "Fixed Deposit", branch, openingDeposit, pin);
    }
    
    @Override
    public double minimumDeposit() {
        return 1000000.0; // 1,000,000 UGX
    }
    
    @Override
    public String getAccountCode() {
        return "FIX";
    }
}