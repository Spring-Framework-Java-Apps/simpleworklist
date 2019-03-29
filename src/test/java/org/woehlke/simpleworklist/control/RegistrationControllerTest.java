package org.woehlke.simpleworklist.control;

import org.junit.Assert;
import org.junit.Test;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.enumerations.RegistrationProcessStatus;
import org.woehlke.simpleworklist.services.RegistrationProcessService;

import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class RegistrationControllerTest extends AbstractTest {


    @Autowired
    private RegistrationProcessService registrationProcessService;

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
        registrationProcessService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RegistrationProcess o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== RegistrationProcessStatus.REGISTRATION_SAVED_EMAIL
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
    public void finish(){
        super.deleteAll();
    }
}
