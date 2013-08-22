package org.woehlke.simpleworklist.control;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.inject.Inject;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.RegistrationProcessStatus;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.TestHelperService;
import org.woehlke.simpleworklist.services.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class UserControllerTest {

    @Inject
    protected WebApplicationContext wac;

    @Inject
    private UserService userService;

    @Inject
    private RegistrationProcessService registrationProcessService;

    @Inject
    private TestHelperService testHelperService;

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

    private void deleteAll(){
        testHelperService.deleteAllRegistrationProcess();
        testHelperService.deleteAllCategory();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
        testHelperService.deleteTimelineDay();
        testHelperService.deleteTimelineMonth();
        testHelperService.deleteTimelineYear();
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(wac).build();
        for (UserAccount u : testUser) {
            UserAccount a = userService.findByUserEmail(u.getUserEmail());
            if (a == null) {
                userService.saveAndFlush(u);
            }
        }
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

    @Test
    public void testRegisterNewUserCheckResponseAndRegistrationForm() throws Exception{
        registrationProcessService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RegistrationProcess o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()==RegistrationProcessStatus.REGISTRATION_SAVED_EMAIL
                || o.getDoubleOptInStatus()==RegistrationProcessStatus.REGISTRATION_SENT_MAIL;
        Assert.assertTrue(result);
        String url = "/confirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/registerConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        registrationProcessService.registrationUserCreated(o);
    }

    @Test
    public void testEnterNewPasswordFormularWithToken() throws Exception {
        registrationProcessService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RegistrationProcess o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()==RegistrationProcessStatus.PASSWORD_RECOVERY_SAVED_EMAIL
                || o.getDoubleOptInStatus()==RegistrationProcessStatus.PASSWORD_RECOVERY_SENT_EMAIL;
        Assert.assertTrue(result);
        String url = "/passwordResetConfirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/resetPasswordConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        registrationProcessService.passwordRecoveryDone(o);
    }

    @Test
    public void testFinish() {
        deleteAll();
    }
}
