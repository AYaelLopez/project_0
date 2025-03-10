package Controllers;

import model.Loan;
import jakarta.servlet.http.HttpSession;
import io.javalin.http.Context;
import model.User;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.logging.Logger;

import static DAO.DBDAO.*;

public class LoanController {

    private static final Logger logger = Logger.getLogger(LoanController.class.getName()); //line 13

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
            logger.info("User logged in");
        }else {
            createLoan(requestedLoan, user);
            ctx.status(201).json("New loan has been requested \n" + requestedLoan);
            logger.info("New loan has been requested \n" + requestedLoan);
        }

    }

    public static void getAllLoans(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");
            logger.warning("User not logged in");

        }else if (user.getRole() == 2){
            ArrayList<Loan> loans = allLoans();
            ctx.json(loans);
            logger.info("All loans has been requested");
        }else if (user.getRole() == 1){
            ctx.status(403).json("Unauthorized");
            logger.warning("Unauthorized user");
        }
    }


    public static void getLoanbyId(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");
            logger.warning("User not logged in");
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
            logger.warning("User not logged in");
        } else if (user.getRole() == 1) {
            updateLoanInDB(requestedLoan.getAmount(), user.getId());
            ctx.status(200).json("Loan updated");
            logger.info("Loan updated");
        }else if (user.getRole() == 2) {
            User recivedUser = ctx.bodyAsClass(User.class);
            Loan receivedLoan = ctx.bodyAsClass(Loan.class);
            updateLoanInDB(receivedLoan.getAmount(), recivedUser.getId());
            ctx.status(200).json("Loan updated");
            logger.info("Loan updated");
        }
    }

    public static void approveLoan(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(403).json("You are not logged in");
            logger.warning("User not logged in");
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
            logger.warning("User not logged in");
        } else if (user.getRole() == 2) {
            User recivedUser = ctx.bodyAsClass(User.class);
            rejectLoanInDB(recivedUser.getId());
        }
    }
}
