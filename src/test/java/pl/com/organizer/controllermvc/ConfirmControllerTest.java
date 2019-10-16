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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
public class ConfirmControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ConfirmController confirmController;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(confirmController).build();
    }

    private User confirmUser;

    private void createUserToUseToConfirmAccount() throws Exception {
        confirmUser = new User();
        confirmUser.setEmail("testConfirm@mail.com");
        confirmUser.setPassword("testPassword");
        confirmUser.setConfirmPassword("testPassword");

        Optional<User> userTOConfirmAccount = userRepository.findByEmail("testConfirm@mail.com");

        if (userTOConfirmAccount.isEmpty()) {
            this.mockMvc
                    .perform(MockMvcRequestBuilderUtils
                            .postForm("/home/register", confirmUser));
        }

        Optional<User> user = userRepository.findByEmail("test@mail.com");
        if (user.isPresent()) {
            User userToSave = user.get();
            userToSave.setConfirmationNumber("2");
            userRepository.save(userToSave);
        }
    }
//TODO - create new user, not using user from MainControllerTest, test result with error
    @Test
    public void testConfirmCreatedAccount() throws Exception {
        createUserToUseToConfirmAccount();
        this.mockMvc.perform(get("/home/confirm-account?id=2"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(view().name("default-success-page"));
    }
}
