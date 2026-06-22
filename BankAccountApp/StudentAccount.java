import java.time.LocalDate;

public class StudentAccount extends Account {
    
    public StudentAccount() {
        super();
    }
    
    public StudentAccount(String firstName, String lastName, String nin, String email,
                          String phoneNumber, LocalDate dateOfBirth, String branch,
                          double openingDeposit, String pin) {
        super(firstName, lastName, nin, email, phoneNumber, dateOfBirth,
              "Student", branch, openingDeposit, pin);
    }
    
    @Override
    public double minimumDeposit() {
        return 10000.0; // 10,000 UGX
    }
    
    @Override
    public String getAccountCode() {
        return "STD";
    }
    
    // Override getAge to add student-specific validation
    public boolean isValidStudentAge() {
        int age = getAge();
        return age >= 18 && age <= 25;
    }
}