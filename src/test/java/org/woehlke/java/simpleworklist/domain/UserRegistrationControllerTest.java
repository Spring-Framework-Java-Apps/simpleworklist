package org.woehlke.java.simpleworklist.domain;

import org.woehlke.java.simpleworklist.config.AbstractTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationStatus;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class UserRegistrationControllerTest extends AbstractTest {

    @Autowired
    private UserAccountRegistrationService userAccountRegistrationService;

    //@Test
    public void testSignInFormularEmail() throws Exception {
        this.mockMvc.perform(
                get("/user/register")).andDo(print())
                .andExpect(view().name(containsString("user/register/registerForm")));
    }

    //@Test
    public void testSignInFormularAccount() throws Exception {
        this.mockMvc.perform(
                get("/user/register/confirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/register/registerNotConfirmed")));
    }

    //@Test
    public void testRegisterNewUserCheckResponseAndRegistrationForm() throws Exception{
        userAccountRegistrationService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserAccountRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== UserAccountRegistrationStatus.REGISTRATION_SAVED_EMAIL
                || o.getDoubleOptInStatus()== UserAccountRegistrationStatus.REGISTRATION_SENT_MAIL;
        assertTrue(result);
        String url = "/user/register/confirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/register/registerConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        userAccountRegistrationService.registrationUserCreated(o);
    }

    //@Test
    public void finish(){
        super.deleteAll();
    }
}
