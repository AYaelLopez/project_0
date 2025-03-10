
import DAO.DBDAO;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.AuthService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthTest {
    @Mock
    private DBDAO userDAOMock;
    private AuthService authServiceMock;


    AuthTest() {
    }

    @BeforeEach
    void setUp() {
        this.userDAOMock = (DBDAO) Mockito.mock(DBDAO.class);
        this.authServiceMock = new AuthService(this.userDAOMock);
    }

    @Test // Manager login
    void testLogin() {
        String username = "jayce";
        String password = "secret";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        int success = this.authServiceMock.loginService(user);
        Assertions.assertEquals(3, success, "Login successful when username and password are correct expected 3");
    }

    @Test // test register the same user
    void registerUser_ShouldReturnNull_WhenUsernameAlreadyExists() {
        String username = "alice";
        String password = "password";
        String name = "Alice";
        String lastName = "Miller";
        int role = 1;
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword(password);
        existingUser.setName(name);
        existingUser.setLastName(lastName);
        existingUser.setRole(role);
        int result = this.authServiceMock.registerService(existingUser);
        Assertions.assertTrue(result == 1, "Expected 1 when username already exists");
    }
}