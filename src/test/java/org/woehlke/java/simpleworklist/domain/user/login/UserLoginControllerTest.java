package org.woehlke.java.simpleworklist.domain.user.login;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.woehlke.java.simpleworklist.application.config.AbstractTest;

public class UserLoginControllerTest extends AbstractTest {

    //@Test
    public void testLoginFormular() throws Exception {
        this.mockMvc.perform(
                get("/user/login")).andDo(print())
                .andExpect(view().name(containsString("user/login/loginForm")));
    }

    //@Test
    public void testFinish() {
        deleteAll();
    }
}
