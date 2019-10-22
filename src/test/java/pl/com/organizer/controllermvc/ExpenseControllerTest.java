package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.model.Expense;

import java.security.Principal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ExpenseControllerTest {

    private MockMvc mockMvc;

    @Autowired
    ExpenseController expenseController;

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return "test@mail.com";
        }
    };

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    public void testAddExpensePage() throws Exception {
        this.mockMvc.perform(get("/user/add-expense"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-expenses-page"));
    }

    @Test
    public void testAddExpenseCorrect() throws Exception {
        Expense newExpense = createCorrectExpense();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-expense", newExpense)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())//.andDo(print())
                .andExpect(redirectedUrl("/user/add-expense"));
    }

    @Test
    public void testAddExpenseWithErrorWithoutAmount() throws Exception {
        Expense newExpense = createExpenseWithoutAmount();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-expense", newExpense)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("add-expenses-page"));
    }

    @Test
    public void testAddExpenseWithErrorWithoutDate() throws Exception {
        Expense newExpense = createExpenseWithoutDate();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-expense", newExpense)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("add-expenses-page"));
    }

    private Expense createCorrectExpense() {
        Expense expense = new Expense();
        expense.setAmount("150.20");
        expense.setForWhat(ForWhatEnum.CLOTHES);
        expense.setDate(LocalDate.now());
        return expense;
    }

    private Expense createExpenseWithoutAmount() {
        Expense expense = new Expense();
        expense.setAmount("");
        expense.setForWhat(ForWhatEnum.CLOTHES);
        expense.setDate(LocalDate.now());
        return expense;
    }

    private Expense createExpenseWithoutDate() {
        Expense expense = new Expense();
        expense.setAmount("150.80");
        expense.setForWhat(ForWhatEnum.CLOTHES);
        return expense;
    }
}