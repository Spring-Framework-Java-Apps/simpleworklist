package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.woehlke.java.simpleworklist.config.AbstractIntegrationTest;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryStatus;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserAccountPasswordRecoveryServiceIT extends AbstractIntegrationTest {

    @Autowired
    private UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;

    @Test
    public void testResetPassword() throws Exception {
        this.mockMvc.perform(
                get("/user/resetPassword")).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordForm")));
    }

    @Test
    public void testEnterNewPasswordFormular() throws Exception {
        this.mockMvc.perform(
                get("/user/resetPassword/confirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordNotConfirmed")));
    }

    @Test
    public void testEnterNewPasswordFormularWithToken() throws Exception {
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserAccountPasswordRecovery o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNotNull(o);
        boolean result = o.getDoubleOptInStatus()== UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_SAVED_EMAIL
                || o.getDoubleOptInStatus()== UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_SENT_EMAIL;
        assertTrue(result);
        String url = "/user/resetPassword/confirm/"+o.getToken();
        this.mockMvc.perform(
                get(url)).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));
        userAccountPasswordRecoveryService.passwordRecoveryDone(o);
    }

    @Test
    public void finish(){
        super.deleteAll();
    }
}
