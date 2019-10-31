package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration_test")
@SpringBootTest
public class ConfirmControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ConfirmController confirmController;

    @Autowired
    private MainController mainController;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(confirmController).build();
    }

    @Test
    void shouldReturnErrorPageWhenConfirmAccountIdIsNotExist() throws Exception {
        this.mockMvc.perform(get("/home/confirm-account?id=90"))
                .andExpect(view().name("default-error-page"))
                .andExpect(model().attribute("message", "Invalid confirmation number. Please contact us."));
    }

    @Test
    void shouldReturnDefaultSuccessPageWhenConfirmAccountIdExist() throws Exception {
        createUserToUseForConfirmAccount();

        this.mockMvc.perform(get("/home/confirm-account?id=2"))
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(view().name("default-success-page"))
                .andExpect(model().attribute("message", "Your e-mail address has been verified. You can sign in now."));

        clearAfterTests();
    }

    private void createUserToUseForConfirmAccount() throws Exception {
        User newUserToConfirmAccount =
                new User("testConfirmUser@mail.com",
                        "testPassword",
                        "testPassword");

        MockMvcBuilders.standaloneSetup(mainController).build()
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/home/register", newUserToConfirmAccount));

        userRepository.findByEmail("testConfirmUser@mail.com").ifPresent(user -> {
            user.setConfirmationNumber("2");
            userRepository.save(user);
        });
    }

    private void clearAfterTests(){
        userRepository.findByEmail("testConfirmUser@mail.com").ifPresent(user -> {
            userRepository.delete(user);
        });
    }
}
