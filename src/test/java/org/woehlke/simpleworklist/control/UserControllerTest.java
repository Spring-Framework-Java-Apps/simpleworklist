package org.woehlke.simpleworklist.control;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.entities.UserAccount;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class UserControllerTest {

    @Inject
    protected WebApplicationContext wac;

    private MockMvc mockMvc;

    private static String emails[] = {"test01@test.de", "test02@test.de", "test03@test.de"};
    private static String passwords[] = {"test01pwd", "test02pwd", "test03pwd"};
    private static String fullnames[] = {"test01 Name", "test02 Name", "test03 Name"};

    private static UserAccount testUser[] = new UserAccount[emails.length];

    static {
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
        }
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void testLoginFormular() throws Exception {
        this.mockMvc.perform(
                get("/login")).andDo(print())
                .andExpect(view().name(containsString("user/loginForm")));
    }

    @Test
    public void testSignInFormularEmail() throws Exception {
        this.mockMvc.perform(
                get("/register")).andDo(print())
                .andExpect(view().name(containsString("user/registerForm")));
    }

    @Test
    public void testSignInFormularAccount() throws Exception {
        this.mockMvc.perform(
                get("/confirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/registerNotConfirmed")));
    }

    @Test
    public void testResetPasswordEmailForm() throws Exception {
        this.mockMvc.perform(
                get("/confirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/registerNotConfirmed")));
    }


    @Test
    public void testResetPassword() throws Exception {
        this.mockMvc.perform(
                get("/resetPassword")).andDo(print())
                .andExpect(view().name(containsString("user/resetPasswordForm")));
    }

    @Test
    public void testEnterNewPasswordFormular() throws Exception {
        this.mockMvc.perform(
                get("/passwordResetConfirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/resetPasswordNotConfirmed")));
    }


}
