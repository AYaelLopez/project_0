import Controllers.LoanController;
import Controllers.UserController;
import io.javalin.Javalin;
import Controllers.AuthController;

/* This class manages all routes so it's the "app class" */
public class Router {

    public static void main(String[] args) {

                var app = Javalin.create(/*config*/)
                        .start();

                app.post("/register", AuthController::register);

                app.post("/login", AuthController::login);

                app.post("/logout", AuthController::logout);

                app.get("/check", AuthController::check);

                app.get("/users", UserController::getUser);

                app.post("/loans", LoanController::newLoan);

                app.get("/loans", LoanController::getAllLoans);

                app.get("/loan", LoanController::getLoanbyId);

                app.put("/loans", LoanController::updateLoan);

                app.put("/loans/approve", LoanController::approveLoan);

                app.put("/loans/reject", LoanController::rejectLoan);

                app.put("/users", UserController::updateUser);
            }
}
