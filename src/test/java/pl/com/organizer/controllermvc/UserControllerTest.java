package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.model.ChangePassword;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest

public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    UserController userController;

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return "test@mail.com";
        }
    };

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLogin() throws Exception {
        this.mockMvc.perform(get("/user")
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("logged-main-page"));
    }

    @Test
    public void testChangePasswordPage() throws Exception {
        this.mockMvc
                .perform(get("/user/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    @Test
    public void testChangePasswordSuccess() throws Exception {
        ChangePassword changePassword = correctChangePassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/change-password", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("success-password-change-page"));
    }

    public ChangePassword correctChangePassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("testPassword");
        changePassword.setNewPassword("testPassword2");
        changePassword.setConfirmNewPassword("testPassword2");
        return changePassword;
    }

    @Test
    public void testChangePasswordWithErrorWhereOldPasswordIsIncorrect() throws Exception {
        ChangePassword changePassword = incorrectChangePassword();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/change-password", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    public ChangePassword incorrectChangePassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("testPassword15");
        changePassword.setNewPassword("testPassword2");
        changePassword.setConfirmNewPassword("testPassword2");
        return changePassword;
    }

    @Test
    public void testChangePasswordWithErrorWhereNewPasswordAndConfirmNewPasswordNotMatch() throws Exception {
        ChangePassword changePassword = incorrectChangePasswordNotMatch();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/change-password", changePassword)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("change-password-page"));
    }

    public ChangePassword incorrectChangePasswordNotMatch() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("testPassword2");
        changePassword.setNewPassword("testPassword2");
        changePassword.setConfirmNewPassword("testPassword3");
        return changePassword;
    }

    @Test
    public void testDeleteAccountPage() throws Exception {
        this.mockMvc.perform(get("/user/delete-account"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-account-page"));
    }
//    TODO - add deleteMethodTest
}
