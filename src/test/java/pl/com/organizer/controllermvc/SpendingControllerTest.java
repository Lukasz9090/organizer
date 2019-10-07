package pl.com.organizer.controllermvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class SpendingControllerTest {

    private MockMvc mockMvc;

    @Autowired SpendingController spendingController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(spendingController).build();
    }

    @Test
    public void testLoginPage() throws Exception {
        this.mockMvc.perform(get("/add-spending"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-spending-page"));
    }
}