package pl.com.organizer.controllermvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void testMainPageRedirect() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())//.andDo(print())
                .andExpect(redirectedUrl("/home"));
    }

//    @Test
//    public void testMainPage() throws Exception {
//        this.mockMvc.perform(get("/home"))
//                .andExpect(status().isOk())//.andDo(print())
//                .andExpect(view().name("main-page"));
//    }
//
//    @Test
//    public void testLoginPage() throws Exception {
//        this.mockMvc.perform(get("/home/login"))
//                .andExpect(status().isOk())//.andDo(print())
//                .andExpect(view().name("login-page"));
//    }
//
//    @Test
//    public void testResetAccountPasswordWriteEmail() throws Exception {
//        this.mockMvc.perform(get("/home/login/reset-password"))
//                .andExpect(status().isOk())//.andDo(print())
//                .andExpect(view().name("reset-password-page-write-email"));
//    }
//
//    @Test
//    public void testResetAccountPasswordSetNewPassword() throws Exception {
//        this.mockMvc.perform(get("/home/login/set-new-password")
//        .param("id", "123"))
//                .andExpect(status().isOk())//.andDo(print())
//                .andExpect(view().name("reset-password-page-write-new-password"));
//    }
//
//    @Test
//    public void testRegisterNewUser() throws Exception {
//        this.mockMvc.perform(get("/home/register"))
//                .andExpect(status().isOk())//.andDo(print())
//                .andExpect(view().name("register-page"));
//    }
}