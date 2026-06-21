import java.time.LocalDate;

public class CurrentAccount extends Account {
    
    public CurrentAccount() {
        super();
    }
    
    public CurrentAccount(String firstName, String lastName, String nin, String email,
                          String phoneNumber, LocalDate dateOfBirth, String branch,
                          double openingDeposit, String pin) {
        super(firstName, lastName, nin, email, phoneNumber, dateOfBirth,
              "Current", branch, openingDeposit, pin);
    }
    
    @Override
    public double minimumDeposit() {
        return 200000.0;
    }
    
    @Override
    public String getAccountCode() {
        return "CUR";
    }
}