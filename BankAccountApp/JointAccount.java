import java.time.LocalDate;

public class JointAccount extends Account {
    private String secondNIN;
    
    public JointAccount() {
        super();
    }
    
    public JointAccount(String firstName, String lastName, String nin, String email,
                        String phoneNumber, LocalDate dateOfBirth, String branch,
                        double openingDeposit, String pin) {
        super(firstName, lastName, nin, email, phoneNumber, dateOfBirth,
              "Joint", branch, openingDeposit, pin);
    }
    
    @Override
    public double minimumDeposit() {
        return 100000.0; // 100,000 UGX
    }
    
    @Override
    public String getAccountCode() {
        return "JNT";
    }
    
    public String getSecondNIN() { 
        return secondNIN; 
    }
    
    public void setSecondNIN(String secondNIN) { 
        this.secondNIN = secondNIN; 
    }
}