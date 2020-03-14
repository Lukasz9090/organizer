package pl.com.organizer.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.model.Expense;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unit_test")
@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    UserService userServiceMock;

    @Test
    void shouldSetCorrectAmountFirstCase (){
        //given
        ExpenseService expenseService = new ExpenseService(userRepositoryMock, userServiceMock);
        Expense firstExpense = new Expense("111", ForWhatEnum.CAR_FUEL, LocalDate.now());

        //when
        expenseService.checkingAmountFormat(firstExpense);

        //then
        assertThat(firstExpense.getAmount(), is(equalTo("111.00")));
    }

    @Test
    void shouldSetCorrectAmountSecondCase (){
        //given
        ExpenseService expenseService = new ExpenseService(userRepositoryMock, userServiceMock);
        Expense secondExpense = new Expense("222.", ForWhatEnum.CAR_FUEL, LocalDate.now());

        //when
        expenseService.checkingAmountFormat(secondExpense);

        //then
        assertThat(secondExpense.getAmount(), is(equalTo("222.00")));
    }

    @Test
    void shouldSetCorrectAmountThirdCase (){
        //given
        ExpenseService expenseService = new ExpenseService(userRepositoryMock, userServiceMock);
        Expense thirdExpense = new Expense("333.3", ForWhatEnum.CAR_FUEL, LocalDate.now());

        //when
        expenseService.checkingAmountFormat(thirdExpense);

        //then
        assertThat(thirdExpense.getAmount(), is(equalTo("333.30")));
    }

    @Test
    void shouldSetCorrectAmountFourthCase (){
        //given
        ExpenseService expenseService = new ExpenseService(userRepositoryMock, userServiceMock);
        Expense fourthExpense = new Expense("444.44", ForWhatEnum.CAR_FUEL, LocalDate.now());

        //when
        expenseService.checkingAmountFormat(fourthExpense);

        //then
        assertThat(fourthExpense.getAmount(), is(equalTo("444.44")));
    }

    @Test
    void createdUserShouldNotHaveAnyExpenses(){
        //given
        User user = new User("user@mail.com", "pass123", "pass123");

        //then
        assertThat(user.getExpenses(), hasSize(0));
    }

    @Test
    void userShouldHaveAddedExpenses(){
        //given
        ExpenseService expenseService = new ExpenseService(userRepositoryMock, userServiceMock);
        User user = new User("user@mail.com", "pass123", "pass123");

        //when
        expenseService.addExpense(user, new Expense("125.20", ForWhatEnum.CAR_FUEL, LocalDate.now()));
        expenseService.addExpense(user, new Expense("150.20", ForWhatEnum.CLOTHES, LocalDate.now()));

        //then
        assertAll(
                () -> assertThat(user.getExpenses(), hasSize(2)),
                () -> assertThat(user.getExpenses().get(0).getAmount(), is(equalTo("125.20"))),
                () -> assertThat(user.getExpenses().get(1).getAmount(), is(equalTo("150.20")))
        );
    }
}