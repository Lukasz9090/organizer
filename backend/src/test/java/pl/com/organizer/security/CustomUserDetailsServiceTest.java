package pl.com.organizer.security;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.controllermvc.MainController;
import pl.com.organizer.exception.UnconfirmedAccountException;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration_test")
@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    private MainController mainController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private String usernameActive = "testActiveUserDetails@mail.com";
    private String usernameNotActive = "testNotActiveUserDetails@mail.com";

    private void createUserToUseForConfirmAccount(String username) throws Exception {
        Optional<User> checkUser = userRepository.findByEmail(username);
        if (checkUser.isEmpty()) {
            User newUser =
                    new User(username,
                            "testPassword",
                            "testPassword");

            MockMvcBuilders.standaloneSetup(mainController).build()
                    .perform(MockMvcRequestBuilderUtils
                            .postForm("/home/register", newUser));

            if (username.equals(usernameActive)) {
                userRepository.findByEmail(usernameActive)
                        .ifPresent(user -> {
                            user.setActive(true);
                            userRepository.save(user);
                        });
            }
        }
    }

    @Test
    void shouldReturnUserDetailsWhenUserAccountIsActive() throws Exception {
        createUserToUseForConfirmAccount(usernameActive);

        assertThat(customUserDetailsService.loadUserByUsername(usernameActive).getUsername(),
                is(equalTo(usernameActive)));
    }

    @Test
    void shouldThrowExceptionWhenUserAccountIsNotActive() throws Exception {
        createUserToUseForConfirmAccount(usernameNotActive);

        assertThrows(UnconfirmedAccountException.class,
                () -> customUserDetailsService.loadUserByUsername(usernameNotActive));
    }
}