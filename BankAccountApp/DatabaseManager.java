import java.sql.*; 
import java.io.File;

public class DatabaseManager {
    private Connection connection;
    
    public DatabaseManager() {
        try {
            File dbFile = new File("FirstBankUganda.accdb");
            String dbPath = dbFile.getAbsolutePath();
            
            System.out.println("Database Connection Setup");
            System.out.println("Database path: " + dbPath);
            System.out.println("File exists: " + dbFile.exists());
            System.out.println("File size: " + dbFile.length() + " bytes");
            
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("Driver loaded successfully");
            
            String url = "jdbc:ucanaccess://" + dbPath.replace("\\", "/");
            System.out.println("Connection URL: " + url);
            
            connection = DriverManager.getConnection(url);
            System.out.println("Database connected successfully!");
            
            createTableIfNeeded();
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: UCanAccess driver not found");
            System.err.println("Make sure lib folder contains all required JAR files");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR: Database connection failed");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createTableIfNeeded() {
        if (connection == null) return;
        
        // SecondNIN column (Allows Null entries for single applicant profiles)
        String createSQL = 
            "CREATE TABLE Accounts (" +
            "AccountNumber TEXT(20) PRIMARY KEY, " +
            "FirstName TEXT(30) NOT NULL, " +
            "LastName TEXT(30) NOT NULL, " +
            "NIN TEXT(14) NOT NULL, " +
            "SecondNIN TEXT(14), " + // <-- Crucial Addition for Joint Accounts
            "Email TEXT(50) NOT NULL, " +
            "PhoneNumber TEXT(15) NOT NULL, " +
            "DateOfBirth DATE NOT NULL, " +
            "AccountType TEXT(20) NOT NULL, " +
            "Branch TEXT(20) NOT NULL, " +
            "OpeningDeposit CURRENCY NOT NULL, " +
            "PIN TEXT(6) NOT NULL, " +
            "RegistrationDate DATE NOT NULL)";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createSQL);
            System.out.println("Accounts table verified/instantiated successfully.");
        } catch (SQLException e) {
            // Defensive architecture catch statement in case the MS Access schema already exists
            if (e.getMessage().contains("already exists") || e.getErrorCode() == -22) {
                System.out.println("Accounts table structure exists and is ready.");
            } else {
                System.err.println("Error creating table: " + e.getMessage());
            }
        }
    }
    
    public String getNextAccountNumber(String branchCode) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No database connection");
        }
        
        int year = java.time.LocalDate.now().getYear();
        String prefix = branchCode + "-" + year + "-";
        
        String sql = "SELECT MAX(AccountNumber) FROM Accounts WHERE AccountNumber LIKE ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, prefix + "%");
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String maxNum = rs.getString(1);
                if (maxNum != null && !maxNum.isEmpty()) {
                    String lastSix = maxNum.substring(maxNum.lastIndexOf("-") + 1);
                    int nextNum = Integer.parseInt(lastSix) + 1;
                    return prefix + String.format("%06d", nextNum);
                }
            }
        }
        
        return prefix + "000001";
    }
    
    public void saveAccount(Account account) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No database connection. Make sure database is not open in MS Access.");
        }
        
        String branchCode = getBranchCode(account.getBranch());
        String accountNumber = getNextAccountNumber(branchCode);
        account.setAccountNumber(accountNumber);
        
        // query string schema blueprint to accommodate SecondNIN placeholder
        String sql = "INSERT INTO Accounts (AccountNumber, FirstName, LastName, NIN, SecondNIN, " +
                     "Email, PhoneNumber, DateOfBirth, AccountType, Branch, " +
                     "OpeningDeposit, PIN, RegistrationDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, account.getFirstName());
            pstmt.setString(3, account.getLastName());
            pstmt.setString(4, account.getNin());
            
            // POLYMORPHIC CHECK: If object type matches JointAccount, extract the secondary key
            if (account instanceof JointAccount) {
                pstmt.setString(5, ((JointAccount) account).getSecondNIN());
            } else {
                pstmt.setNull(5, Types.VARCHAR); // Inject SQL Null value for other account options
            }
            
            pstmt.setString(6, account.getEmail());
            pstmt.setString(7, account.getPhoneNumber());
            pstmt.setDate(8, Date.valueOf(account.getDateOfBirth()));
            pstmt.setString(9, account.getAccountType());
            pstmt.setString(10, account.getBranch());
            pstmt.setDouble(11, account.getOpeningDeposit());
            pstmt.setString(12, account.getPin());
            pstmt.setDate(13, Date.valueOf(java.time.LocalDate.now()));
            
            int rows = pstmt.executeUpdate();
            System.out.println("✓ Account saved! Number: " + accountNumber + " Rows: " + rows);
            
        } catch (SQLException e) {
            System.err.println("Error saving account: " + e.getMessage());
            throw e;
        }
    }
    
    private String getBranchCode(String branch) {
        if (branch == null) return "KLA";
        switch(branch) {
            case "Kampala": return "KLA";
            case "Gulu": return "GUL";
            case "Mbarara": return "MBR";
            case "Jinja": return "JIN";
            case "Mbale": return "MBL";
            default: return "KLA";
        }
    }
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}