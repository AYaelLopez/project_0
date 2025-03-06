package service;

import jakarta.servlet.http.HttpSession;
import model.User;

import static DAO.DBDAO.*;

public class AuthService {

    public static int registerService(User user){
        int checkResult;
        if (user.getUsername() == null || user.getPassword() == null) {
            checkResult = 0;
        }else if (userExists(user.getUsername())) {
            // Check if user already exists
            checkResult = 1;
        }else {
            checkResult = 2;
            createUserInDB(user);
        }
        return checkResult;

        
    }

    public static int loginService(User user) {
        User dbUser = getUserFromDB(user.getUsername());
        int checkResult;

        if (user.getUsername() == null | user.getPassword() == null) {
            checkResult = 0;
        }else if (dbUser == null) {
            // no registered user
            checkResult = 1;
        }else if (!(dbUser.getPassword().equals(user.getPassword()))) {
            // Compare password, triggers if wrong
            checkResult = 2;
        }else {
            checkResult = 3;
        }
        return checkResult;
    }
}
