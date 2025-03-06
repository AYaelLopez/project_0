package Controllers;

import model.Loan;
import jakarta.servlet.http.HttpSession;
import io.javalin.http.Context;
import model.User;
import java.util.ArrayList;

import static DAO.DBDAO.*;

public class LoanController {

    /**
        POST /loans
        status -> pending
        {
            "amount_request": "16400"
            "status": "pending"
        }
     */

    public static void newLoan(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        Loan requestedLoan = ctx.bodyAsClass(Loan.class);

        if (user == null) {
            ctx.status(403).json("You are not logged in");
        }else {
            createLoan(requestedLoan, user);
            ctx.status(201).json("New loan has been requested \n" + requestedLoan);
        }

    }

    public static void getAllLoans(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");

        }else if (user.getRole() == 2){
            ArrayList<Loan> loans = allLoans();
            ctx.json(loans);
        }else {
            System.out.println("You are not logged in");
        }
    }


    public static void getLoanbyId(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");
        }else if (user.getRole() == 1){
            Loan loan = getLoan(user.getId());
            ctx.status(200).json(loan);
        } else if (user.getRole() == 2) {
            /**
             * {
             *     "id": 1
             * }
             */
            User recivedUser = ctx.bodyAsClass(User.class);
            Loan loan = getLoan(recivedUser.getId());
            ctx.status(200).json(loan);

        }
    }

    public static void updateLoan(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        Loan requestedLoan = ctx.bodyAsClass(Loan.class);
        if (user == null) {
            ctx.status(403).json("You are not logged in");
        } else if (user.getRole() == 1) {
            updateLoanInDB(requestedLoan.getAmount(), user.getId());
            ctx.status(200).json("Loan updated");
        }else if (user.getRole() == 2) {
            User recivedUser = ctx.bodyAsClass(User.class);
            Loan receivedLoan = ctx.bodyAsClass(Loan.class);
            updateLoanInDB(receivedLoan.getAmount(), recivedUser.getId());
            ctx.status(200).json("Loan updated");
        }
    }

    public static void approveLoan(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");
        }else if (user.getRole() == 2) {
            User recivedUser = ctx.bodyAsClass(User.class);
            approveLoanInDB(recivedUser.getId());
        }
    }

    public static void rejectLoan(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");
        } else if (user.getRole() == 2) {
            User recivedUser = ctx.bodyAsClass(User.class);
            rejectLoanInDB(recivedUser.getId());
        }
    }
}
