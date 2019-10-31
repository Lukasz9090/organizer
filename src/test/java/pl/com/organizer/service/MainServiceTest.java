package pl.com.organizer.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.RoleRepository;
import pl.com.organizer.repository.UserRepository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Tag("unit_test")
@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    RoleRepository roleRepositoryMock;

    @Mock
    EmailService emailServiceMock;

    @Mock
    PasswordEncoder passwordEncoderMock;

    @Test
    void shouldReturnUserWhenUserIsExist(){
        //given
        User user = new User("testUser@mail.com", "testPassword","testPassword");

        //when
        given(userRepositoryMock.findByEmail("testUser@mail.com")).willReturn(java.util.Optional.of(user));

        //then
        assertThat(userRepositoryMock.findByEmail("testUser@mail.com").get().getEmail(), is(equalTo("testUser@mail.com")));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotExist(){
        //given
        MainService mainService = new MainService(userRepositoryMock, roleRepositoryMock, emailServiceMock, passwordEncoderMock);

        //then
        assertThrows(UserNotFoundException.class,
                () -> mainService.getUserByUsername("testUserNotExist@mail.com"));
    }

    @Test
    void shouldThrowExceptionWhenConfirmationNumberIsNotCorrect(){
        //given
        MainService mainService = new MainService(userRepositoryMock, roleRepositoryMock, emailServiceMock, passwordEncoderMock);

        //then
        assertThrows(UserNotFoundException.class,
                () -> mainService.findUserByResetPasswordNumber("15"));
    }

    @Test
    void shouldReturnFalseWhenUserIsNotExist(){
        //given
        MainService mainService = new MainService(userRepositoryMock, roleRepositoryMock, emailServiceMock, passwordEncoderMock);

        //then
        assertThat(mainService.checkIfUserExist("userNotExist@mail.com"), is(false));
    }
}