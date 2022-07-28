package org.endofusion.endoserver.service;
import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.exception.domain.EmailExistException;
import org.endofusion.endoserver.exception.domain.UsernameExistException;
import org.endofusion.endoserver.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private User user;

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.user}")
    private String sqlAddUser;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddUser);
    }

    @Test
    @DisplayName("Test if register throws UsernameExistException")
    public void registerUsernameExistTest()  {
        Exception exception = assertThrows(UsernameExistException.class, () -> userService.register("Mike", "Smith", "RobyUsername", "eric.test@luv2code_school.com"));
        String expectedMessage = "Username already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test if register throws EmailExistException")
    public void registerEmailExistTest()  {
        Exception exception = assertThrows(EmailExistException.class, () -> userService.register("Mike", "Smith", "Roby", "eric.roby@luv2code_school.com"));
        String expectedMessage = "Email already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUser);
    }
}
