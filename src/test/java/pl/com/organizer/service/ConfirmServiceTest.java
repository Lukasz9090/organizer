package pl.com.organizer.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Tag("unit_test")
@ExtendWith(MockitoExtension.class)
class ConfirmServiceTest {

    @Mock
    ConfirmService confirmServiceMock;

    @Mock
    static UserRepository userRepositoryMock;

    @Test
    void verifyInvocation() {
        //given
        String confirmationNumber = "123cn123";

        //when
        confirmServiceMock.confirmEmailAddress(confirmationNumber);

        //then
        verify(confirmServiceMock).confirmEmailAddress(confirmationNumber);
    }

    @Test
    void shouldThrowException() {
        //given
        String validConfirmationNumber = "validNumber";

        //when
        given(confirmServiceMock.getUserByConfirmationNumber(validConfirmationNumber))
                .willThrow(UserNotFoundException.class);

        //then
        assertThrows(UserNotFoundException.class,
                () -> confirmServiceMock.getUserByConfirmationNumber(validConfirmationNumber));
    }

    @Test
    void shouldThrowExceptionUserNotFound() {
        //given
        ConfirmService confirmService = new ConfirmService(userRepositoryMock);
        String validConfirmationNumber = "validNumber";

        //then
        assertThrows(UserNotFoundException.class,
                () -> confirmService.getUserByConfirmationNumber(validConfirmationNumber));
    }

    @Test
    void accountShouldNotBeActiveBeforeConfirmation() {
        //given
        User user = new User("123@mail.com", "123123", "123123");

        //then
        assertThat(user.isEmailAddressConfirmationStatus(), is(false));
    }

    @Test
    void accountShouldBeActiveAfterConfirmation() {
        //given
        User user = new User("123@mail.com", "123123", "123123");

        //when
        given(confirmServiceMock.confirmAccount(user)).willCallRealMethod();
        confirmServiceMock.confirmAccount(user);

        //then
        assertAll(
                () -> assertThat(user.isEmailAddressConfirmationStatus(), is(true)),
                () -> assertThat(user.getConfirmationNumber(), equalTo("Account confirmed"))
        );
    }
}