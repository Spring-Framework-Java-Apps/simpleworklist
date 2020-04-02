package org.woehlke.simpleworklist.user.register;

import org.junit.Assert;
import org.junit.Test;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.user.register.UserRegistration;
import org.woehlke.simpleworklist.user.register.UserRegistrationStatus;
import org.woehlke.simpleworklist.user.register.UserRegistrationService;

import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class UserRegistrationControllerTest extends AbstractTest {


    @Autowired
    private UserRegistrationService userRegistrationService;

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
    public void testRegisterNewUserCheckResponseAndRegistrationForm() throws Exception{
        userRegistrationService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserRegistration o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== UserRegistrationStatus.REGISTRATION_SAVED_EMAIL
                || o.getDoubleOptInStatus()== UserRegistrationStatus.REGISTRATION_SENT_MAIL;
        Assert.assertTrue(result);
        String url = "/confirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/registerConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        userRegistrationService.registrationUserCreated(o);
    }

    @Test
    public void finish(){
        super.deleteAll();
    }
}
