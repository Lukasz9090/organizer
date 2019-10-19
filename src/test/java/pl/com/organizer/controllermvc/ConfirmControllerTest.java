package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ConfirmControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ConfirmController confirmController;

    @Autowired
    private MainController mainController;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(confirmController).build();
    }

    @Test
    public void testConfirmCreatedAccountWithError() throws Exception {
        this.mockMvc.perform(get("/home/confirm-account?id=90"))
                .andExpect(view().name("default-error-page"))
                .andExpect(model().attribute("message", "Invalid confirmation number. Please contact us."));
    }

    @Test
    public void testConfirmCreatedAccountSuccess() throws Exception {
        createUserToUseForConfirmAccount();
        this.mockMvc.perform(get("/home/confirm-account?id=2"))
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("default-success-page"))
                .andExpect(model().attribute("message", "Your e-mail address has been verified. You can sign in now."));
    }

    private void createUserToUseForConfirmAccount() throws Exception {
        Optional<User> userToConfirmAccount = userRepository.findByEmail("testConfirmUser@mail.com");

        if (userToConfirmAccount.isEmpty()) {
            User newUserToConfirmAccount = new User();
            newUserToConfirmAccount.setEmail("testConfirmUser@mail.com");
            newUserToConfirmAccount.setPassword("testPassword");
            newUserToConfirmAccount.setConfirmPassword("testPassword");

            MockMvcBuilders.standaloneSetup(mainController).build()
                    .perform(MockMvcRequestBuilderUtils
                            .postForm("/home/register", newUserToConfirmAccount));
        }

        Optional<User> user = userRepository.findByEmail("testConfirmUser@mail.com");
        if (user.isPresent()) {
            User userToSave = user.get();
            userToSave.setConfirmationNumber("2");
            userRepository.save(userToSave);
        }
    }
}
