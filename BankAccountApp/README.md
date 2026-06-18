# First Bank Uganda - New Account Opening Application
Developed by Group J members
1. WANUME Timothy VU-BIT-2507-0915-EVE
2. Nakamya Resty
3. 

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

