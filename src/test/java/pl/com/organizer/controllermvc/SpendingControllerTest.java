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
import pl.com.organizer.model.Spending;

import java.security.Principal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SpendingControllerTest {

    private MockMvc mockMvc;

    @Autowired
    SpendingController spendingController;

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return "test@mail.com";
        }
    };

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(spendingController).build();
    }

    @Test
    public void testAddSpendingPage() throws Exception {
        this.mockMvc.perform(get("/user/add-spending"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-spending-page"));
    }

    @Test
    public void testAddSpendingCorrect() throws Exception {
        Spending newSpending = createCorrectSpending();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-spending", newSpending)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(redirectedUrl("/user/add-spending"))
                .andDo(print());
    }

    @Test
    public void testAddSpendingWithErrorWithoutAmount() throws Exception {
        Spending newSpending = createSpendingWithoutAmount();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-spending", newSpending)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("add-spending-page"));
    }

    @Test
    public void testAddSpendingWithErrorWithoutDate() throws Exception {
        Spending newSpending = createSpendingWithoutDate();

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/add-spending", newSpending)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(view().name("add-spending-page"));
    }

    private Spending createCorrectSpending() {
        Spending spending = new Spending();
        spending.setAmount("150.20");
        spending.setForWhat(ForWhatEnum.CLOTHES);
        spending.setDate(LocalDate.now());
        return spending;
    }

    private Spending createSpendingWithoutAmount() {
        Spending spending = new Spending();
        spending.setAmount("");
        spending.setForWhat(ForWhatEnum.CLOTHES);
        spending.setDate(LocalDate.now());
        return spending;
    }

    private Spending createSpendingWithoutDate() {
        Spending spending = new Spending();
        spending.setAmount("150.80");
        spending.setForWhat(ForWhatEnum.CLOTHES);
        return spending;
    }
}