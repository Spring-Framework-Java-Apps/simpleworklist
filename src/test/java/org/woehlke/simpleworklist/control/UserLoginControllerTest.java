package org.woehlke.simpleworklist.control;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.woehlke.simpleworklist.AbstractTest;

public class UserLoginControllerTest extends AbstractTest {

    @Test
    public void testLoginFormular() throws Exception {
        this.mockMvc.perform(
                get("/login")).andDo(print())
                .andExpect(view().name(containsString("user/loginForm")));
    }

    @Test
    public void testFinish() {
        deleteAll();
    }
}
