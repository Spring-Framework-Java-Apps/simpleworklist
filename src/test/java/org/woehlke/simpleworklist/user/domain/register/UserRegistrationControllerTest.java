package org.woehlke.simpleworklist.user.domain.register;

import org.woehlke.simpleworklist.application.config.AbstractTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.domain.user.register.UserRegistration;
import org.woehlke.simpleworklist.domain.user.register.UserRegistrationStatus;
import org.woehlke.simpleworklist.domain.user.register.UserRegistrationService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class UserRegistrationControllerTest extends AbstractTest {

    @Autowired
    private UserRegistrationService userRegistrationService;

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
        userRegistrationService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== UserRegistrationStatus.REGISTRATION_SAVED_EMAIL
                || o.getDoubleOptInStatus()== UserRegistrationStatus.REGISTRATION_SENT_MAIL;
        assertTrue(result);
        String url = "/user/register/confirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/register/registerConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        userRegistrationService.registrationUserCreated(o);
    }

    //@Test
    public void finish(){
        super.deleteAll();
    }
}
