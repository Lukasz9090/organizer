package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.model.ChangePassword;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Tag("integration_test")
@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private MainController mainController;

    @Autowired
    private UserRepository userRepository;

    private String username = "testUser@main.com";

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return username;
        }
    };

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldReturnLoggedMainViewWhenURLIsCalled() throws Exception {
        createCorrectUserAndSaveToDB(username);

        this.mockMvc.perform(get("/user")
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("logged-main-page"));
    }

    private void createCorrectUserAndSaveToDB(String username) throws Exception {
        if (!isUserExist(username)){
            User user = new User(username, "testPassword", "testPassword");
            MockMvcBuilders.standaloneSetup(mainController).build()
                    .perform(MockMvcRequestBuilderUtils
                            .postForm("/home/register", user));
        }
    }

    private boolean isUserExist(String username){
        return userRepository.findByEmail(username).isPresent();
    }

    @Test
    void shouldReturnChangePasswordViewWhenURLIsCalled() throws Exception {
        this.mockMvc
                .perform(get("/user/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    void shouldReturnSuccessChangeViewWhenOldPasswordAndNewPasswordsAreCorrect() throws Exception {
        createCorrectUserAndSaveToDB(username);
        ChangePassword changePassword = new ChangePassword("testPassword", "testPassword", "testPassword");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/change-password", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("success-password-change-page"));
    }

    @Test
    void shouldReturnChangePasswordViewWithErrorWhenOldPasswordIsNotCorrect() throws Exception {
        createCorrectUserAndSaveToDB(username);
        ChangePassword changePassword = new ChangePassword("testPassword10", "testPassword2", "testPassword2");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/change-password", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    void shouldReturnChangePasswordPageWithErrorWhenNewPasswordIsNotMatchToConfirmPassword() throws Exception {
        createCorrectUserAndSaveToDB(username);
        ChangePassword changePassword = new ChangePassword("testPassword", "testPassword2", "notConfirmedCorrectly");

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/change-password", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    void shouldReturnDeleteAccountViewWhenURLIsCalled() throws Exception {
        this.mockMvc.perform(get("/user/delete-account"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-account-page"));
    }
}
