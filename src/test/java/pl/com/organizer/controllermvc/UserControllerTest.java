package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.enums.RoleEnum;
import pl.com.organizer.model.ChangePassword;
import pl.com.organizer.model.Role;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.RoleRepository;
import pl.com.organizer.repository.UserRepository;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO - add test for delete method
//@RunWith(SpringRunner.class)
@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return "test@mail.com";
        }
    };

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
        if (userRole == null) {
            Role roleUserRole = new Role();
            roleUserRole.setRole(RoleEnum.ROLE_USER);
            roleRepository.save(roleUserRole);
        }
    }

    @Test
    public void testLoginPage() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login-page"));
    }

    @Test
    public void testLogin() throws Exception {
        this.mockMvc.perform(get("/logged"))
                .andExpect(status().isOk())
                .andExpect(view().name("logged-page"));
    }

    @Test
    public void testRegister() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-page"));
    }

    @Test
    public void testAddUserWithSuccess() throws Exception {
        userRepository.findByEmail("test@mail.com").ifPresent(user -> userRepository.delete(user));

        User correctUser = createCorrectUser();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/register/add-user", correctUser))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("register-success-page"));
    }

    @Test
    public void testAddUserWithErrorWhereSecondTimeIsEnteredTheSameEmail() throws Exception {
        User correctUser = createCorrectUser();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/register/add-user", correctUser))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("register-page"));
    }

    @Test
    public void testAddUserWithErrorWhereEmailIsIncorrect() throws Exception {
        User userWithIncorrectEmail = createUserWhereEmailIsIncorrect();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/register/add-user", userWithIncorrectEmail))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("register-page"));
    }

    @Test
    public void testAddUserWithErrorWherePasswordAndConfirmPasswordAreNotMatch() throws Exception {
        User userWithIncorrectPasswords = createUserWherePasswordAndConfirmPasswordAreNotMatch();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/register/add-user", userWithIncorrectPasswords))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("register-page"));
    }

    @Test
    public void testChangePasswordPage() throws Exception {
        this.mockMvc
                .perform(get("/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    public void testChangePasswordSuccess() throws Exception {
        ChangePassword changePassword = correctChangePassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/change-password/confirm", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("success-password-change-page"));
    }

    @Test
    public void testChangePasswordWithErrorWhereOldPasswordIsIncorrect() throws Exception {
        ChangePassword changePassword = incorrectChangePassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/change-password/confirm", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    public void testChangePasswordWithErrorWhereNewPasswordAndConfirmNewPasswordNotMatch() throws Exception {
        ChangePassword changePassword = incorrectChangePasswordNotMatch();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/change-password/confirm", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    public void testDeleteAccountPage() throws Exception {
        this.mockMvc.perform(get("/delete-account"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-account-page"));
    }

    public User createCorrectUser() {
        User correctUser = new User();
        correctUser.setEmail("test@mail.com");
        correctUser.setPassword("testPassword");
        correctUser.setConfirmPassword("testPassword");
        return correctUser;
    }

    public User createUserWhereEmailIsIncorrect() {
        User correctUser = new User();
        correctUser.setEmail("test2mail.com");
        correctUser.setPassword("testPassword");
        correctUser.setConfirmPassword("testPassword");
        return correctUser;
    }

    public User createUserWherePasswordAndConfirmPasswordAreNotMatch() {
        User incorrectUser = new User();
        incorrectUser.setEmail("testTwo@mail.com");
        incorrectUser.setPassword("testPassword");
        incorrectUser.setConfirmPassword("noTestPassword");
        return incorrectUser;
    }

    public ChangePassword correctChangePassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("testPassword");
        changePassword.setNewPassword("testPassword2");
        changePassword.setConfirmNewPassword("testPassword2");
        return changePassword;
    }

    public ChangePassword incorrectChangePassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("testPassword15");
        changePassword.setNewPassword("testPassword2");
        changePassword.setConfirmNewPassword("testPassword2");
        return changePassword;
    }

    public ChangePassword incorrectChangePasswordNotMatch() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("testPassword");
        changePassword.setNewPassword("testPassword2");
        changePassword.setConfirmNewPassword("testPassword3");
        return changePassword;
    }
}