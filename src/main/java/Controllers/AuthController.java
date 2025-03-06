package Controllers;

import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;
import model.User;
import static DAO.DBDAO.*;
import static service.AuthService.*;

public class AuthController {

    /**
        POST /register
        role -> 1: regular, 2: manager;
        JSON body:
        {
           "username": "alice",
           "password": "secret",
           "name": "Alice",
           "lastname": "Mac",
           "role": "1"
        }
     **/
    public static void register(Context ctx) {
        User requestUser = ctx.bodyAsClass(User.class);


        int reg = registerService(requestUser);

        // Basic fiels validation
        if (reg == 0) {
            ctx.status(400).json("{\"error\":\"Missing username or password\"}");
        }else if (reg == 1){
            // Check if user already exists
            ctx.status(409).json("{\"error\":\"Username already taken\"}");
        }else{
            // Insert new user then
            ctx.status(200).json("Registered user account successfully");
        }
    }

    /**
        POST /login
        JSON body:
        {
            "username": "alice",
            "password": "secret"
        }
    **/
    public static void login(Context ctx) {
        User requestUser = ctx.bodyAsClass(User.class);
        User dbUser = getUserFromDB(requestUser.getUsername());
        int reg = loginService(requestUser);

        // Basic fiels validation
        if (reg == 0) {
            ctx.status(400).json("{\"error\":\"Missing username or password\"}");
        }else if (reg == 1) {
            // no registered user
            ctx.status(401).json("{\"error\":\"User not found\"}");
        }else if (reg == 2) {
            // Compare password, triggers if wrong
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");
            return;
        }else {
            // Start a session if the credentials are ok
            HttpSession session = ctx.req().getSession();
            session.setAttribute("user", dbUser);
            ctx.status(201).json("{\"message\":\"Login successful\"}");
        }
    }

    /**
        POST /logout
    **/
    public static void logout(Context ctx) {
        HttpSession session = ctx.req().getSession();
        session.invalidate();
        ctx.status(200).json("{\"message\":\"Logout successful\"}");
    }

    /**
        GET /check for login checking
    **/
    public static void check(Context ctx) {
        HttpSession session = ctx.req().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(205).json("{\"error\":\"User logout\"}");
        }else {
            ctx.status(400).json("{\"message\":\"Not logged out\"}");
        }
    }


}
