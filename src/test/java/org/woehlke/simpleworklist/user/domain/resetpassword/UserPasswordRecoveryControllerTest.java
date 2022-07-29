package org.woehlke.simpleworklist.user.domain.resetpassword;

import org.woehlke.simpleworklist.application.config.AbstractTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.domain.user.resetpassword.UserPasswordRecovery;
import org.woehlke.simpleworklist.domain.user.resetpassword.UserPasswordRecoveryStatus;
import org.woehlke.simpleworklist.domain.user.resetpassword.UserPasswordRecoveryService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserPasswordRecoveryControllerTest extends AbstractTest {

    @Autowired
    private UserPasswordRecoveryService userPasswordRecoveryService;

    //@Test
    public void testResetPassword() throws Exception {
        this.mockMvc.perform(
                get("/user/resetPassword")).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordForm")));
    }

    //@Test
    public void testEnterNewPasswordFormular() throws Exception {
        this.mockMvc.perform(
                get("/user/resetPassword/confirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordNotConfirmed")));
    }

    //@Test
    public void testEnterNewPasswordFormularWithToken() throws Exception {
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserPasswordRecovery o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== UserPasswordRecoveryStatus.PASSWORD_RECOVERY_SAVED_EMAIL
                || o.getDoubleOptInStatus()== UserPasswordRecoveryStatus.PASSWORD_RECOVERY_SENT_EMAIL;
        assertTrue(result);
        String url = "/user/resetPassword/confirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        userPasswordRecoveryService.passwordRecoveryDone(o);
    }

    //@Test
    public void finish(){
        super.deleteAll();
    }
}
