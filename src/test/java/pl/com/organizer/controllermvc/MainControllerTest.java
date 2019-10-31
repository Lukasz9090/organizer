package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.*;
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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration_test")
@SpringBootTest
class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
        if (userRole == null) {
            Role roleUserRole = new Role();
            roleUserRole.setRole(RoleEnum.ROLE_USER);
            roleRepository.save(roleUserRole);
        }
    }

    @Test
    void shouldRedirectToHomePageURL() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void shouldReturnMainViewWhenURLIsCalled() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("main-page"));
    }

    @Test
    void shouldReturnLoginViewWhenURLIsCalled() throws Exception {
        this.mockMvc.perform(get("/home/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login-page"));
    }

    @Test
    void shouldReturnResetPasswordViewWhenURLIsCalled() throws Exception {
        this.mockMvc.perform(get("/home/login/reset-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password-page-write-email"));
    }

    @Test
    void shouldReturnResetPasswordViewWhenResetPasswordNumberIsCorrect() throws Exception {
        createUserToUseInPasswordReset("testUserToResetPassword@mail.com");
        this.mockMvc.perform(get("/home/set-new-password?id=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password-page-write-new-password"));
    }

    void createUserToUseInPasswordReset(String username) throws Exception {
        if (!isUserExist(username)){
            createCorrectUserAndSaveToDB(username);
        }
        setResetPasswordNumber(username,"2");
    }

    boolean isUserExist(String username){
        return userRepository.findByEmail(username).isPresent();
    }

    User createCorrectUserAndSaveToDB(String username) throws Exception {
        User user = createUser(username);
        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", user));
        return user;
    }

    private User createUser(String username) {
        return new User(username,
                "testPassword",
                "testPassword");
    }

    void ifUserExistDeleteIt(String username) {
        userRepository.findByEmail(username)
                .ifPresent(user -> userRepository.delete(user));
    }

    void setResetPasswordNumber(String username, String passwordNumber) throws Exception {
        userRepository.findByEmail(username)
                .ifPresent(user -> {
                    user.setResetPasswordNumber(passwordNumber);
                    userRepository.save(user);
                });
    }

    @Test
    void shouldReturnDefaultSuccessViewWithMessageWhenPasswordToResetOldPasswordIsCorrect() throws Exception {
        createUserToUseInPasswordReset("testUserToResetPassword@mail.com");
        ChangePassword newCorrectPasswordToResetTheOldOne = createCorrectPassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/set-new-password?id=2", newCorrectPasswordToResetTheOldOne))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("default-success-page"))
                .andExpect(model().attribute("message", "Password reset with success. You can sign in now."));
    }

    private ChangePassword createCorrectPassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setNewPassword("testPassword50");
        changePassword.setConfirmNewPassword("testPassword50");
        return changePassword;
    }

    @Test
    void shouldReturnResetPasswordViewWithErrorMessageWhenPasswordToResetOldPasswordIsNotCorrect() throws Exception {
        ChangePassword incorrectPasswordToReset = createIncorrectPassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/set-new-password?id=2", incorrectPasswordToReset))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password-page-write-new-password"));
    }

    private ChangePassword createIncorrectPassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setNewPassword("testPassword0");
        changePassword.setConfirmNewPassword("testPassword50");
        return changePassword;
    }

    @Test
    void shouldReturnDefaultErrorViewWithErrorMessageWhenResetPasswordNumberIsNotCorrect() throws Exception {
        ChangePassword createCorrectPassword = createCorrectPassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/set-new-password?id=250", createCorrectPassword))
                .andExpect(view().name("default-error-page"))
                .andExpect(model().attribute("message", "Invalid confirmation number. Please contact us."));
    }

    @Test
    void shouldReturnRegisterViewWhenURLIsCalled() throws Exception {
        this.mockMvc.perform(get("/home/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-page"));
    }

    @Test
    void shouldReturnDefaultSuccessViewWhenUserIsSuccessfullyCreated() throws Exception {
        ifUserExistDeleteIt("testUserToRegister@mail.com");
        User correctUser = createUser("testUserToRegister@mail.com");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", correctUser))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("default-success-page"))
                .andExpect(model().attribute("message", "Check your mailbox and confirm your account."));

        User userFromDB = userRepository.findByEmail(correctUser.getEmail()).get();

        assertAll("Check if user is in DB.",
                () -> assertThat(userFromDB.getEmail(), is(equalTo(correctUser.getEmail()))),
                () -> assertThat(userFromDB.getPassword(), is(not(equalTo(correctUser.getPassword()))))
        );
    }

    @Test
    void shouldReturnRegisterViewWithErrorMessageWhenUsernameAlreadyExistInDB() throws Exception {
        User correctUser = createCorrectUserAndSaveToDB("testUserToRegisterTwoTimes@mail.com");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", correctUser))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("register-page"));
    }

    @Test
    void shouldReturnRegisterViewWithErrorWhenEmailAddressIsNotCorrect() throws Exception {
        User userWithIncorrectEmail = createUser("testUserWithIncorrectEmail2mail.com");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", userWithIncorrectEmail))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("register-page"));
    }

    @Test
    void shouldReturnRegisterPageWithErrorWhenPasswordsAreIncorrect() throws Exception {
        User userWithIncorrectPasswords = createUser("testUserWithIncorrectPasswords");
        userWithIncorrectPasswords.setPassword("testPassword");
        userWithIncorrectPasswords.setPassword("notMatchesPassword");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", userWithIncorrectPasswords))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("register-page"));
    }
}
