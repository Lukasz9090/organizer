package pl.com.organizer.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.exception.IncorrectDateException;
import pl.com.organizer.model.Expense;
import pl.com.organizer.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit_test")
@ExtendWith(MockitoExtension.class)
class ReportGeneratorServiceTest {

    @Mock
    static UserRepository userRepositoryMock;

    @Mock
    static SpringTemplateEngine springTemplateEngineMock;

    private static ReportGeneratorService reportGeneratorService;

    @BeforeAll
    static void setup() {
        reportGeneratorService = new ReportGeneratorService(userRepositoryMock, springTemplateEngineMock);
    }

    @Test
    void shouldReturnFilteredAndSortedListWithExpensesWhenListAndDatesAndForWhatAreCorrect(){
        //given
        List<Expense> expenseList = createListWithSixExpenses();

        //when
        List<Expense> allExpenseSortedList = reportGeneratorService.filterList(expenseList,
                LocalDate.of(2019,1,1),
                LocalDate.of(2019,1,6),
                ForWhatEnum.ALL);
        List<Expense> filteredSortedList = reportGeneratorService.filterList(expenseList,
                LocalDate.of(2019,1,3),
                LocalDate.of(2019,1,4),
                ForWhatEnum.TRAVEL);

        //then
        assertAll(
                () -> assertThat(allExpenseSortedList.get(0).getDate(), is(equalTo(LocalDate.of(2019,1,1)))),
                () -> assertThat(allExpenseSortedList.get(allExpenseSortedList.size() - 1).getDate(), is(equalTo(LocalDate.of(2019,1,6)))),
                () -> assertThat(allExpenseSortedList.size(), is(6)),

                () -> assertThat(filteredSortedList.get(0).getDate(), is(equalTo(LocalDate.of(2019,1,4)))),
                () -> assertThat(filteredSortedList.get(filteredSortedList.size() - 1).getDate(), is(equalTo(LocalDate.of(2019,1,4)))),
                () -> assertThat(filteredSortedList.size(), is(1))
        );
    }

    @Test
    void shouldReturnEmptyListWhenExpenseWithSelectedParametersIsNotInList(){
        //given
        List<Expense> expenseList = createListWithSixExpenses();

        //when
        List<Expense> firstEmptyList = reportGeneratorService.filterList(expenseList,
                LocalDate.of(2019,1,1),
                LocalDate.of(2019,1,6),
                ForWhatEnum.RECREATION);
        List<Expense> secondEmptyList = reportGeneratorService.filterList(expenseList,
                LocalDate.of(2019,1,4),
                LocalDate.of(2019,1,6),
                ForWhatEnum.CLOTHES);

        //then
        assertAll(
                () -> assertThat(firstEmptyList.size(), is(0)),
                () -> assertThat(secondEmptyList.size(), is(0))
        );
    }

    @Test
    void shouldReturnTrueWhenExpenseDateIsBetweenOrEqualToSelectedDates() {
        //given
        Expense expense = new Expense(
                "220.00", ForWhatEnum.CAR_FUEL, LocalDate.of(2019, 1, 2));

        //when
        //then
        assertAll(
                () -> assertThat(reportGeneratorService.filterListByDate(
                        expense,
                        LocalDate.of(2019, 1, 1),
                        LocalDate.of(2019, 1, 2)), is(true)),
                () -> assertThat(reportGeneratorService.filterListByDate(
                        expense,
                        LocalDate.of(2019, 1, 2),
                        LocalDate.of(2019, 1, 2)), is(true)),
                () -> assertThat(reportGeneratorService.filterListByDate(
                        expense,
                        LocalDate.of(2019, 1, 2),
                        LocalDate.of(2019, 1, 10)), is(true)),
                () -> assertThat(reportGeneratorService.filterListByDate(
                        expense,
                        LocalDate.of(2018, 1, 1),
                        LocalDate.of(2020, 1, 5)), is(true))
        );
    }

    @Test
    void shouldReturnFalseWhenExpenseDateIsNotBetweenOrNotEqualToSelectedDates() {
        //given
        Expense expense = new Expense(
                "220.00", ForWhatEnum.CAR_FUEL, LocalDate.of(2019, 2, 2));

        //when
        //then
        assertAll(
                () -> assertThat(reportGeneratorService.filterListByDate(
                        expense,
                        LocalDate.of(2019, 1, 1),
                        LocalDate.of(2019, 2, 1)), is(false)),
                () -> assertThat(reportGeneratorService.filterListByDate(
                        expense,
                        LocalDate.of(2019, 2, 3),
                        LocalDate.of(2019, 3, 3)), is(false))
        );
    }

    @Test
    void shouldReturnTrueWhenExpenseTypeIsEqualToSearchingExpenseType() {
        //given
        Expense clothesExpense = new Expense("123.10", ForWhatEnum.CLOTHES, LocalDate.now());
        Expense carExpense = new Expense("140.00", ForWhatEnum.CAR_FUEL, LocalDate.now());
        Expense cosmeticsExpense = new Expense("100.50", ForWhatEnum.COSMETICS, LocalDate.now());

        //then
        assertAll(
                () -> assertThat(reportGeneratorService.filterListByExpenseType(clothesExpense, ForWhatEnum.CLOTHES), is(true)),
                () -> assertThat(reportGeneratorService.filterListByExpenseType(carExpense, ForWhatEnum.CAR_FUEL), is(true)),
                () -> assertThat(reportGeneratorService.filterListByExpenseType(cosmeticsExpense, ForWhatEnum.COSMETICS), is(true)),

                () -> assertThat(reportGeneratorService.filterListByExpenseType(clothesExpense, ForWhatEnum.CAR_FUEL), is(false)),
                () -> assertThat(reportGeneratorService.filterListByExpenseType(carExpense, ForWhatEnum.CAR_OTHER), is(false)),
                () -> assertThat(reportGeneratorService.filterListByExpenseType(cosmeticsExpense, ForWhatEnum.CLOTHES), is(false)),

                () -> assertThat(reportGeneratorService.filterListByExpenseType(clothesExpense, ForWhatEnum.ALL), is(true)),
                () -> assertThat(reportGeneratorService.filterListByExpenseType(carExpense, ForWhatEnum.ALL), is(true)),
                () -> assertThat(reportGeneratorService.filterListByExpenseType(cosmeticsExpense, ForWhatEnum.ALL), is(true))
        );
    }

    @Test
    void shouldReturnCorrectValueWhenListWithExpensesIsNotEmpty() {
        //given
        List<Expense> expenseList = createListWithSixExpenses();

        //when
        BigDecimal sumOfExpenses = reportGeneratorService.calculateSumOfAmount(expenseList);

        //then
        assertAll(
                () -> assertThat(sumOfExpenses, is(notNullValue())),
                () -> assertThat(sumOfExpenses, is(greaterThan(new BigDecimal("0")))),
                () -> assertThat(sumOfExpenses.compareTo(new BigDecimal("1498.52")), is(0))
        );
    }

    @Test
    void shouldReturnFirstDateFromUnsortedListWhenListWithExpensesIsNotEmpty() {
        //given
        List<Expense> listWithExpenses = createListWithSixExpenses();

        //when
        //then
        assertThat(reportGeneratorService.getFirstDate(listWithExpenses),
                is(equalTo(LocalDate.of(2019, 1, 3))));
    }

    @Test
    void shouldReturnLastDateFromUnsortedListWhenListWithExpensesIsNotEmpty() {
        //given
        List<Expense> listWithExpenses = createListWithSixExpenses();

        //when
        //then
        assertThat(reportGeneratorService.getLastDate(listWithExpenses),
                is(equalTo(LocalDate.of(2019, 1, 6))));
    }

    @Test
    void shouldReturnFirstDateEqualToLastDateWhenListHaveOnlyOneExpense() {
        //given
        List<Expense> listWithExpenses = List.of(
                new Expense("123.12", ForWhatEnum.CLOTHES, LocalDate.of(2019, 1, 3)));

        //when
        LocalDate firstDate = reportGeneratorService.getFirstDate(listWithExpenses);
        LocalDate secondDate = reportGeneratorService.getLastDate(listWithExpenses);

        //then
        assertThat(firstDate, is(equalTo(secondDate)));
    }

    @Test
    void shouldThrowExceptionWhenListIsEmpty() {
        //given
        List<Expense> emptyList = new ArrayList<>();

        //when
        //then
        assertAll(
                () -> assertThrows(IncorrectDateException.class, () -> reportGeneratorService.getFirstDate(emptyList)),
                () -> assertThrows(IncorrectDateException.class, () -> reportGeneratorService.getLastDate(emptyList))
        );
    }

    List<Expense> createListWithSixExpenses() {
        return List.of(
                new Expense("123.12", ForWhatEnum.CLOTHES, LocalDate.of(2019, 1, 3)),
                new Expense("220.00", ForWhatEnum.CAR_FUEL, LocalDate.of(2019, 1, 2)),
                new Expense("150.20", ForWhatEnum.COSMETICS, LocalDate.of(2019, 1, 1)),
                new Expense("350.20", ForWhatEnum.TRAVEL, LocalDate.of(2019, 1, 4)),
                new Expense("450.00", ForWhatEnum.FLAT_BILLS, LocalDate.of(2019, 1, 5)),
                new Expense("205.00", ForWhatEnum.CAR_OTHER, LocalDate.of(2019, 1, 6))
        );
    }
}