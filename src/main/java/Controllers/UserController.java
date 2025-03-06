package Controllers;

import model.User;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

import static DAO.DBDAO.*;

public class UserController {
    public static void getUser (Context ctx) {
        HttpSession session = ctx.req().getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            ctx.status(404).json("{\"error\":\"You're not logged in\"}");

        }
        else {
            ctx.json(user);
        }

        System.out.println(user.toString());
    }

    public static void updateUser (Context ctx) {
        HttpSession session = ctx.req().getSession(true);
        User user = (User) session.getAttribute("user");
        User recivedUser = ctx.bodyAsClass(User.class);
        if (user == null) {
            ctx.status(404).json("{\"error\":\"You're not logged in\"}");
        } else if (user.getRole() == 1) {
            updateUserInDB(recivedUser, user.getId());
            ctx.json("User updated \n" + user);

        } else if (user.getRole() == 2) {
            updateUserInDB(recivedUser, recivedUser.getId());
        }
    }
}
