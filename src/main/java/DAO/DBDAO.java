package DAO;

import model.User;
import model.Loan;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DBDAO {

    private static final Logger logger = Logger.getLogger(DBDAO.class.getName()); //line 13

    /**
     db connection
     **/

    public static Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/dbp0";
            String user = "postgres";
            String password = "123artCONT";
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return conn;
    }

    /**
     Creates a new user in the DB, returns true if successful.
     **/
    public static void createUserInDB(User user) {

        Connection conn = getConnection(); // Assuming you have a getConnection() method
        try {
            // 1. Insert into account table (using PreparedStatement is better)
            String insertAccount = "INSERT INTO account (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmtAccount = conn.prepareStatement(insertAccount)) {
                pstmtAccount.setString(1, user.getUsername());
                pstmtAccount.setString(2, user.getPassword());
                pstmtAccount.executeUpdate();
            }

            // 2. Get the generated account_id (using PreparedStatement is better)
            String selectAccountId = "SELECT account_id FROM account WHERE username = ?";
            int accountId;
            try (PreparedStatement pstmtSelect = conn.prepareStatement(selectAccountId)) {
                pstmtSelect.setString(1, user.getUsername());
                try (ResultSet rs = pstmtSelect.executeQuery()) {
                    if (rs.next()) {
                        accountId = rs.getInt("account_id");
                    } else {
                        logger.info("Account  ID not found");
                        //System.err.println("Account ID not found for username: " + user.getUsername());
                        return; // Exit the method if account_id is not found
                    }
                }
            }

            // 3. Insert into users table (using PreparedStatement)
            String insertUsers = "INSERT INTO users (name, last_name, role, account_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtUsers = conn.prepareStatement(insertUsers)) {
                pstmtUsers.setString(1, user.getName());
                pstmtUsers.setString(2, user.getLastName());
                pstmtUsers.setInt(3, user.getRole());
                pstmtUsers.setInt(4, accountId);
                pstmtUsers.executeUpdate();
            }
            logger.info("Registered user account successfully");
            //System.out.println("Registered user account successfully");

        } catch (SQLException e) {
            logger.warning("Database error");
            //System.err.println("Database error: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    /**
     * Checks if a username already exists.
     */
    public static boolean userExists(String username) {
        Connection conn = getConnection();
        try{
            Statement s = conn.createStatement();
            String select = "select * from account where username = '" + username + "'";
            ResultSet row = s.executeQuery(select);
            return row.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     Retrieves a user by username or returns null if not found.
     **/

    public static User getUserFromDB(String username) {

        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            String select = "select * from account where username = '" + username + "'";



            ResultSet row = stmt.executeQuery(select);

            if (row.next()) {
                User user = new User();
                user.setId(row.getInt("account_id"));
                user.setUsername(row.getString("username"));
                user.setPassword(row.getString("password"));

                String select2 = "select * from users where account_id='" + user.getId() + "'";
                Statement ps2 = conn.createStatement();
                ResultSet row2 = ps2.executeQuery(select2);
                row2.next();

                user.setName(row2.getString("name"));
                user.setLastName(row2.getString("last_name"));
                user.setRole(row2.getInt("role"));
                System.out.println(user);
                return user;
            } else return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void createLoan (Loan loan, User user) {
        Connection conn = getConnection();

        try {
            String newLoan = "INSERT INTO Loans (applicant, amount_request, status) values (?, ?, ?)";

            try(PreparedStatement ptmsLoan = conn.prepareStatement(newLoan)){
                ptmsLoan.setInt(1, user.getId());
                ptmsLoan.setDouble(2, loan.getAmount());
                ptmsLoan.setString(3, loan.getStatus());
                ptmsLoan.executeUpdate();
            }
            logger.info("Created loan successfully");
            //System.out.println("New loan added successfully");

        } catch (SQLException e) {
            System.out.println("Database error " + e.getMessage());

        }
    }

    public static ArrayList<Loan> allLoans (){
        Connection conn = getConnection();

        try {
            String select = "select * from Loans";
            PreparedStatement pstmt = conn.prepareStatement(select);
            ResultSet rows = pstmt.executeQuery();
            ArrayList<Loan> loans = new ArrayList<>();
            while (rows.next()) {
                loans.add(new Loan(
                        rows.getInt("loan_aplication"),
                        rows.getInt("applicant"),
                        rows.getDouble("amount_request"),
                        rows.getString("status")
                ));
            }
            return loans;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Loan getLoan (int loanId) {
        Connection conn = getConnection();

        try {
            Loan loan = new Loan();
            String selectLoan = "select * from Loans where applicant='" + loanId + "'";
            Statement pstmt = conn.createStatement();
            ResultSet row = pstmt.executeQuery(selectLoan);
            if (row.next()) {
                loan.setId(row.getInt("loan_aplication"));
                loan.setAplicantId(row.getInt("applicant"));
                loan.setAmount(row.getDouble("amount_request"));
                loan.setStatus(row.getString("status"));
            }
            return loan;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void updateLoanInDB (double amount, int userId) {
        Connection conn = getConnection();

        try{
            String update = "UPDATE Loans SET amount_request=? WHERE applicant=?";
            try (PreparedStatement ptms = conn.prepareStatement(update)){
                ptms.setDouble(1,amount);
                ptms.setInt(2, userId);
                ptms.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static void approveLoanInDB (int loanId) {
        Connection conn = getConnection();
        try {
            String update = "UPDATE Loans SET status='approved' WHERE applicant=?";
            try (PreparedStatement ptms = conn.prepareStatement(update)){
                ptms.setInt(1, loanId);
                ptms.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void rejectLoanInDB (int loanId) {
        Connection conn = getConnection();

        try {
            String reject = "UPDATE Loans SET status='rejected' WHERE applicant=?";
            try (PreparedStatement ptms = conn.prepareStatement(reject)){
                ptms.setInt(1, loanId);
                ptms.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void updateUserInDB (User user, int userId) {
        Connection conn = getConnection();

        try {
            String update = "UPDATE Users SET name=?, last_name=? WHERE id=?";
            try (PreparedStatement ptms = conn.prepareStatement(update)){
                ptms.setString(1, user.getName());
                ptms.setString(2, user.getLastName());
                ptms.setInt(3, userId);
                ptms.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
