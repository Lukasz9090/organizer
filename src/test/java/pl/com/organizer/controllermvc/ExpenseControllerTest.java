package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.model.Expense;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration_test")
@SpringBootTest
class ExpenseControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ExpenseController expenseController;

    @Autowired
    private MainController mainController;

    @Autowired
    private UserRepository userRepository;

    private String username = "testUserAddExpense@mail.com";

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return username;
        }
    };

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    void shouldReturnAddExpensesViewWhenURLIsCalled() throws Exception {
        this.mockMvc.perform(get("/user/add-expense"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-expenses-page"));
    }

    @Test
    void shouldRedirectToAddExpenseURLWhenExpenseIsAddedCorrectly() throws Exception {
        Expense newExpense = createCorrectExpense();
        createCorrectUserAndSaveToDB(username);
        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-expense", newExpense)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(redirectedUrl("/user/add-expense"));
    }

    private Expense createCorrectExpense() {
        return new Expense("150.20",
                ForWhatEnum.CLOTHES,
                LocalDate.now());
    }

    private void createCorrectUserAndSaveToDB(String username) throws Exception {
        if (!isUserExist(username)){
            User user = new User(username, "pass123", "pass123");
            MockMvcBuilders.standaloneSetup(mainController).build()
                    .perform(MockMvcRequestBuilderUtils
                            .postForm("/home/register", user));
        }
    }

    private boolean isUserExist(String username){
        return userRepository.findByEmail(username).isPresent();
    }

    @Test
    void shouldReturnAddExpenseViewWithErrorWhenExpenseIsWithoutAmount() throws Exception {
        Expense newExpense = createExpenseWithoutAmount();
        createCorrectUserAndSaveToDB(username);
        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-expense", newExpense)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("add-expenses-page"));
    }

    private Expense createExpenseWithoutAmount() {
        return new Expense("",
                ForWhatEnum.CLOTHES,
                LocalDate.now());
    }

    @Test
    void shouldReturnAddExpenseViewWithErrorWhenExpenseIsWithoutDate() throws Exception {
        Expense newExpense = createExpenseWithoutDate();
        createCorrectUserAndSaveToDB(username);
        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-expense", newExpense)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("add-expenses-page"));
    }

    private Expense createExpenseWithoutDate() {
        return new Expense("150.80",
                ForWhatEnum.CLOTHES,
                null);
    }
}