package pl.com.organizer.controllermvc;

import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.model.ReportSettings;
import pl.com.organizer.model.Expense;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;
import pl.com.organizer.service.MainService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration_test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportGeneratorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ReportGeneratorController reportGeneratorController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MainService mainService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportGeneratorController).build();
        createUser();
    }

    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return "testReport@mail.com";
        }
    };

    void createUser() {
        Optional<User> userToReport = userRepository.findByEmail("testReport@mail.com");

        if (userToReport.isEmpty()) {
            Expense expense = new Expense("150.20", ForWhatEnum.CLOTHES, LocalDate.of(2019, 8, 1));
            User correctUserForReport = new User("testReport@mail.com",
                    "testPassword",
                    "testPassword");
            correctUserForReport.setExpenses(List.of(expense));

            mainService.createNewUser(correctUserForReport);
        }
    }

    @Test
    void shouldReturnReportGeneratorPageWhenURLIsCalled() throws Exception {
        this.mockMvc
                .perform(get("/user/report"))
                .andExpect(matchAll(
                        status().isOk(),
                        view().name("report-generator-page")));
    }

    @Test
    void shouldGenerateReportWithSuccess() throws Exception {
        ReportSettings reportSettings = new ReportSettings(LocalDate.of(2019, 7, 1), LocalDate.now(), ForWhatEnum.ALL);

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/report", reportSettings)
                        .principal(principal))
                .andExpect(matchAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_PDF)));
    }

    @Test
    void shouldReturnReportErrorViewWhenDatesAreIncorrect() throws Exception {
        ReportSettings reportSettings = new ReportSettings(LocalDate.of(2015, 7, 1), LocalDate.of(2016, 7, 1), ForWhatEnum.ALL);

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/report", reportSettings)
                        .principal(principal))
                .andExpect(matchAll(
                        view().name("report-error-page"),
                        model().attribute("message", "You have selected the wrong dates")));
    }

    @Test
    void shouldReturnReportErrorViewWhenDatesAreNull() throws Exception {
        ReportSettings reportSettings = new ReportSettings(null, null, ForWhatEnum.ALL);

        this.mockMvc
                .perform(MockMvcRequestBuilderUtils
                        .postForm("/user/report", reportSettings)
                        .principal(principal))
                .andExpect(matchAll(
                        view().name("report-error-page"),
                        model().attribute("message", "You have to select dates")));
    }
}