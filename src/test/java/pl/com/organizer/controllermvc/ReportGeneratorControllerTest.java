package pl.com.organizer.controllermvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.com.organizer.enums.ForWhatEnum;
import pl.com.organizer.model.ReportSettings;
import pl.com.organizer.model.Spending;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;
import pl.com.organizer.service.MainService;
import pl.com.organizer.service.UserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
class ReportGeneratorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    ReportGeneratorController reportGeneratorController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MainService mainService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportGeneratorController).build();
        createUser();
    }

    public void createUser() {
        Optional<User> userToReport = userRepository.findByEmail("testReport@mail.com");

        if (userToReport.isEmpty()) {
            Spending spending = new Spending("150.20", ForWhatEnum.CLOTHES, LocalDate.of(2019, 8, 1));
            User correctUserForReport = new User();
            correctUserForReport.setEmail("testReport@mail.com");
            correctUserForReport.setPassword("testPassword");
            correctUserForReport.setConfirmPassword("testPassword");
            correctUserForReport.setSpending(List.of(spending));

            mainService.createNewUser(correctUserForReport);
        }
    }

    @Test
    public void testCreateReport() throws Exception {
        this.mockMvc.perform(get("/user/report"))
                .andExpect(status().isOk())
                .andExpect(view().name("report-generator-page"));
    }

    @Test
    public void generateReportSuccess() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/user/report";
        URI uri = new URI(baseUrl);
        ReportSettings reportSettings = new ReportSettings(LocalDate.of(2019, 7, 1), LocalDate.now(), ForWhatEnum.ALL);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Spending> request = new HttpEntity<>(headers);
        ResponseEntity<byte[]> result = testRestTemplate.exchange(uri,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<byte[]>() {
                });
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}