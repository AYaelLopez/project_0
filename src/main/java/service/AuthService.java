package service;

import DAO.DBDAO;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

import static DAO.DBDAO.*;

public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    public AuthService(DBDAO userDAOMock) {
    }

    public static int registerService(User user){
        int checkResult;
        if (user.getUsername() == null || user.getPassword() == null) {
            checkResult = 0;
            logger.warning("Username or password are empty");
        }else if (userExists(user.getUsername())) {
            // Check if user already exists
            checkResult = 1;
            logger.warning("Username already exists");
        }else {
            checkResult = 2;
            createUserInDB(user);
            logger.info("User created");
        }
        return checkResult;
    }

    public static int loginService(User user) {
        User dbUser = getUserFromDB(user.getUsername());
        int checkResult;

        if (user.getUsername() == null | user.getPassword() == null) {
            checkResult = 0;
            logger.warning("Username or password are empty");
        }else if (dbUser == null) {
            // no registered user
            checkResult = 1;
            logger.warning("Username not exists");
        }else if (!(dbUser.getPassword().equals(user.getPassword()))) {
            // Compare password, triggers if wrong
            checkResult = 2;
            logger.warning("Wrong password");
        }else {
            checkResult = 3;
            logger.info("User logged in");
        }
        return checkResult;
    }
}
