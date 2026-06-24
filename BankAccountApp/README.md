# First Bank Uganda - New Account Opening Application

Developed by Group J members
S/N	NAME	REG NUMBER
1	WANUME TIMOTHY	VU-BIT-2507-0915-EVE
2	MUSINGUZI HAPPY BENSON	VU-DIT-2507-1805-EVE
3	AITA TOM	VU-BCS-2507-3178-EVE
4	WELISHE DAN	VU-BCS-2507-3672-EVE 
5	MUSHIME GRACE	VU-BIT-2507-2455-EVE
6	MAGAWA SOLOMON	VU-BCS-2507-2085-EVE 
7	MUZUNGU DIFASI	VU-BIT-2507-0432-EVE
8	NANDUTU JOYCE	VU-BSF-2507-1209-EVE
9	NAKAMYA RESTY	VU-DIT-2507-1180 EVE
10	BIRUNGI SANDRA	VU-DIT-2507-1783-EVE 


## Project Overview
A Java Swing desktop application for opening new bank accounts at First Bank Uganda. This application demonstrates Object-Oriented Programming principles, input validation, and database persistence using MS Access.

## Coursework Requirements Fulfilled

### (a) Layout and Behavior 
- Text fields: First Name, Last Name, NIN, Email, Confirm Email, Phone, PIN, Confirm PIN
- Three combo boxes for Date of Birth (Year, Month, Day) with auto-updating days and leap year support
- Single-select Account Type: Savings, Current, Fixed Deposit, Student, Joint
- Single-select Branch: Kampala, Gulu, Mbarara, Jinja, Mbale
- Numeric field for Opening Deposit (UGX)
- Submit and Reset buttons
- Read-only summary area: "Account Summary is Below:"

### (b) Minimum Opening Deposit by Account Type 
| Account Type | Minimum Deposit (UGX) | Special Rule |
|-------------|----------------------|--------------|
| Savings | 50,000 | Earns interest, no overdraft |
| Current | 200,000 | Overdraft allowed, no interest |
| Fixed Deposit | 1,000,000 | Locked term, highest interest |
| Student | 10,000 | Applicant age must be 18-25 |
| Joint | 100,000 | Requires a second NIN |

### (c) Validation Rules 
- **First/Last Name**: Required, letters only, 2-30 characters
- **National ID (NIN)**: Required, exactly 14 alphanumeric characters, uppercase
- **Email**: Valid format, must match confirmation
- **Phone Number**: Ugandan format +256XXXXXXXXX
- **PIN**: 4-6 digits, must match confirmation, not all identical digits
- **Date of Birth**: Auto-updates days based on month and leap year
- **Age**: 18-75 years (18-25 for Student accounts)
- **Deposit**: Meets minimum requirement based on account type

### (d) Account Number Generation 
Format: BRANCHCODE-YYYY-XXXXXX
- KLA for Kampala
- GUL for Gulu
- MBR for Mbarara
- JIN for Jinja
- MBL for Mbale

### (e) Object-Oriented Design 
- Abstract `Account` class with abstract `minimumDeposit()` method
- Concrete subclasses: SavingsAccount, CurrentAccount, FixedDepositAccount, StudentAccount, JointAccount
- Polymorphism for deposit validation

## Prerequisites for setting up and running our Java program
1.	Java Development Kit (JDK) 8 or higher
Download it from https://www.oracle.com/java/technologies/downloads/
Verify installation by running `java -version` in the CMD or PowerShell
2.	Microsoft Access 2010 or higher
Required to create and view the database
3.	UCanAccess JDBC Driver
Download from https://sourceforge.net/projects/ucanaccess/
Enables Java to connect to MS Access databases

## Installation and Setup

### Step 1: 
Collect all the files
Our folder is called `BankAccountApp`; on GitHub it is in the repository `Group-J-OOP-coursework`, see the link https://github.com/wtimu/Group-J-OOP-coursework
Make sure all the files are present
Ensure the following are present:
•	All .java files in the main directory
•	lib folder with all JAR files
•	FirstBankUganda.accdb database file
BankAccountApp/
Account.java (Abstract base class)
SavingsAccount.java (Savings account implementation)
CurrentAccount.java (Current account implementation)
FixedDepositAccount.java (Fixed deposit account implementation)
StudentAccount.java (Student account implementation)
JointAccount.java (Joint account implementation)
InputValidator.java (Input validation logic)
DatabaseManager.java (Database operations)
BankApplicationUI.java (Main GUI application (Swing))
FirstBankUganda.accdb (MS Access database)

lib/ (Required libraries)
ucanaccess-5.0.1.jar
hsqldb-2.5.0.jar
jackcess-3.0.1.jar
commons-lang3-3.8.1.jar
commons-logging-1.2.jar

### Step 2
Database Setup
1.	Open Microsoft Access
2.	Open FirstBankUganda.accdb
3.	Verify the Accounts table exists with the following structure:
Field Name	Data Type	Field Size
AccountNumber	Short Text	20 (Primary Key)
FirstName	Short Text	30
LastName	Short Text	30
NIN	Short Text	14
Email	Short Text	50
PhoneNumber	Short Text	15
DateOfBirth	Date/Time	-
AccountType	Short Text	20
Branch	Short Text	20
OpeningDeposit	Currency	-
PIN	Short Text	6
RegistrationDate	Date/Time	-
4.	IMPORTANT: Close MS Access before running the Java application
### Step 3: 
Compile the Application
# Windows PowerShell
`PS C:\Users\HP\Desktop\OOP\BankAccountApp> javac -cp ".;lib\*" *.java`

### Step 5: 
Run the Application
# Windows PowerShell
`PS C:\Users\HP\Desktop\OOP\BankAccountApp> java -cp ".;lib\*" BankApplicationUI`

How to Use the Application
Opening a New Account
1.	Launch the application as in steps 4 and 5 above
2.	Fill in all required fields (marked with *)
3.	Select Date of Birth using the three dropdown menus
Select Year first, then Month, then Day
Days automatically update based on the month and leap year
4.	Choose Account Type from the dropdown
5.	Select preferred Branch
6.	Enter the Opening Deposit amount in UGX
7.	Click Submit Application 
Form Validation
•	All fields are validated in real-time
•	Error messages appear next to invalid fields
•	A summary dialog shows all validation errors
•	The form cannot be submitted until all fields are valid
Account Types
•	Savings: Minimum 50,000 UGX, earns interest
•	Current: Minimum 200,000 UGX, overdraft allowed
•	Fixed Deposit: Minimum 1,000,000 UGX, highest interest
•	Student: Minimum 10,000 UGX, age must be 18-25
•	Joint: Minimum 100,000 UGX, requires second NIN
Viewing Account Summary
•	After successful submission, the account details appear in the "Account Summary is Below" area
•	The summary includes account number, personal details, and deposit information
Resetting the Form
•	Click Reset Form to clear all fields and start over






