package org.woehlke.simpleworklist.control;

import junit.framework.Assert;
import org.junit.Test;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.RegistrationProcessStatus;
import org.woehlke.simpleworklist.services.RegistrationProcessService;

import javax.inject.Inject;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class PasswordRecoveryControllerTest extends AbstractTest {

    @Inject
    private RegistrationProcessService registrationProcessService;

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
    public void testEnterNewPasswordFormularWithToken() throws Exception {
        registrationProcessService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RegistrationProcess o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== RegistrationProcessStatus.PASSWORD_RECOVERY_SAVED_EMAIL
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
    public void finish(){
        super.deleteAll();
    }
}
