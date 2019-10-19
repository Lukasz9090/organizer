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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
        if (userRole == null) {
            Role roleUserRole = new Role();
            roleUserRole.setRole(RoleEnum.ROLE_USER);
            roleRepository.save(roleUserRole);
        }
    }

    @Test
    public void testMainPageRedirect() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())//.andDo(print())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void testMainPage() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("main-page"));
    }

    @Test
    public void testLoginPage() throws Exception {
        this.mockMvc.perform(get("/home/login"))
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("login-page"));
    }

    @Test
    public void testResetAccountPasswordWriteEmail() throws Exception {
        this.mockMvc.perform(get("/home/login/reset-password"))
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("reset-password-page-write-email"));
    }

    //TODO - post method to resetAccountPasswordWriteEmail

    public void createUserToUseInPasswordResetFeature() throws Exception {
        Optional<User> userToUserInPasswordResetFeature = userRepository.findByEmail("testResetPassword@mail.com");

        if (userToUserInPasswordResetFeature.isEmpty()){
            User newUserToUserInPasswordResetFeature = new User();
            newUserToUserInPasswordResetFeature.setEmail("testResetPassword@mail.com");
            newUserToUserInPasswordResetFeature.setPassword("testPassword");
            newUserToUserInPasswordResetFeature.setConfirmPassword("testPassword");

            this.mockMvc
                    .perform(MockMvcRequestBuilderUtils
                            .postForm("/home/register", newUserToUserInPasswordResetFeature));
        }

        Optional<User> userToResetPassword = userRepository.findByEmail("testResetPassword@mail.com");

        if(userToResetPassword.isPresent()){
            userToResetPassword.get().setResetPasswordNumber("2");
            userRepository.save(userToResetPassword.get());
        }
    }

    @Test
    public void testResetAccountPasswordSetNewPassword() throws Exception {
        createUserToUseInPasswordResetFeature();
        this.mockMvc.perform(get("/home/set-new-password?id=2"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(view().name("reset-password-page-write-new-password"));
    }

    @Test
    public void testResetAccountPasswordSetNewPasswordSuccess() throws Exception {
        createUserToUseInPasswordResetFeature();
        ChangePassword newCorrectPasswordToResetTheOldOne = createCorrectPassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/set-new-password?id=2", newCorrectPasswordToResetTheOldOne))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("default-success-page"))
                .andExpect(model().attribute("message", "Password reset with success. You can sign in now."));
    }

    public ChangePassword createCorrectPassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setNewPassword("testPassword50");
        changePassword.setConfirmNewPassword("testPassword50");
        return changePassword;
    }

    @Test
    public void resetAccountPasswordSetNewPasswordWithError() throws Exception {
        ChangePassword incorrectPasswordToReset = createIncorrectPassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/set-new-password?id=2", incorrectPasswordToReset))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("reset-password-page-write-new-password"));
    }

    public ChangePassword createIncorrectPassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setNewPassword("testPassword0");
        changePassword.setConfirmNewPassword("testPassword50");
        return changePassword;
    }

    @Test
    public void resetAccountPasswordWrongConfirmationNumberToCreatePassword() throws Exception {
        ChangePassword createCorrectPassword = createCorrectPassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/set-new-password?id=250", createCorrectPassword))
                .andExpect(view().name("default-error-page"))
        .andExpect(model().attribute("message", "Invalid confirmation number. Please contact us."));
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        this.mockMvc.perform(get("/home/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-page"));
    }

    @Test
    public void testRegisterNewUserSuccess() throws Exception {
        userRepository.findByEmail("test@mail.com").ifPresent(user -> userRepository.delete(user));
        User correctUser = createCorrectUser();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", correctUser))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("default-success-page"))
        .andExpect(model().attribute("message", "Check your mailbox and confirm your account."));
    }

    public User createCorrectUser() {
        User correctUser = new User();
        correctUser.setEmail("test@mail.com");
        correctUser.setPassword("testPassword");
        correctUser.setConfirmPassword("testPassword");
        return correctUser;
    }

    @Test
    public void testAddUserWithErrorWhereSecondTimeIsEnteredTheSameEmail() throws Exception {
        User correctUser = createCorrectUser();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", correctUser))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("register-page"));
    }

    @Test
    public void testRegisterNewUserWithErrorWhereEmailIsIncorrect() throws Exception {
        User userWithIncorrectEmail = createUserWhereEmailIsIncorrect();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", userWithIncorrectEmail))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("register-page"));
    }

    public User createUserWhereEmailIsIncorrect() {
        User correctUser = new User();
        correctUser.setEmail("test2mail.com");
        correctUser.setPassword("testPassword");
        correctUser.setConfirmPassword("testPassword");
        return correctUser;
    }

    @Test
    public void testAddUserWithErrorWherePasswordAndConfirmPasswordAreNotMatch() throws Exception {
        User userWithIncorrectPasswords = createUserWherePasswordAndConfirmPasswordAreNotMatch();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", userWithIncorrectPasswords))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("register-page"));
    }

    public User createUserWherePasswordAndConfirmPasswordAreNotMatch() {
        User incorrectUser = new User();
        incorrectUser.setEmail("testTwo@mail.com");
        incorrectUser.setPassword("testPassword");
        incorrectUser.setConfirmPassword("noTestPassword");
        return incorrectUser;
    }
}
