package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.extern.java.Log;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.application.helper.TestHelperService;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.config.WebMvcConfig;
import org.woehlke.java.simpleworklist.config.WebSecurityConfig;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryStatus;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Log
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SimpleworklistApplication.class)
@ImportAutoConfiguration({
    WebMvcConfig.class,
    WebSecurityConfig.class
})
@EnableConfigurationProperties({
    SimpleworklistProperties.class
})
public class UserAccountPasswordRecoveryServiceIT {

    @Autowired
    private ServletWebServerApplicationContext server;

    @Autowired
    private TestHelperService testHelperService;

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation")
    @LocalServerPort
    private int port;

    @Autowired
    private UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;


    protected static String[] emails = {"test01//@Test.de", "test02//@Test.de", "test03//@Test.de"};
    protected static String[] passwords = {"test01pwd", "test02pwd", "test03pwd"};
    protected static String[] fullnames = {"test01 Name", "test02 Name", "test03 Name"};

    protected static String username_email = "undefined//@Test.de";
    protected static String password = "ASDFG";
    protected static String full_name = "UNDEFINED_NAME";

    protected static UserAccount[] testUser = new UserAccount[emails.length];

    static {
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUuid(UUID.randomUUID());
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
        }
    }

    @Order(1)
    @Test
    public void testResetPassword() {
        try {
            this.mockMvc.perform(
                    get("/user/resetPassword/form")).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordForm")));
        } catch (Exception ex) {
            log.info("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.info(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }

    @Order(2)
    @Test
    public void testEnterNewPasswordFormular() {
        try {
            this.mockMvc.perform(
                    get("/user/resetPassword/confirm/ASDF")).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordNotConfirmed")));
        } catch (Exception ex) {
            log.info("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.info(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }

    @Order(3)
    @Test
    public void testEnterNewPasswordFormularWithToken() {
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserAccountPasswordRecovery o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNotNull(o);
        boolean result = o.getDoubleOptInStatus() == UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_SAVED_EMAIL
            || o.getDoubleOptInStatus() == UserAccountPasswordRecoveryStatus.PASSWORD_RECOVERY_SENT_EMAIL;
        assertTrue(result);
        String url = "/user/resetPassword/confirm/" + o.getToken();
        try {
            this.mockMvc.perform(
                    get(url)).andDo(print())
                .andExpect(view().name(containsString("user/resetPassword/resetPasswordConfirmed")))
                .andExpect(model().attributeExists("userAccountFormBean"));

        } catch (Exception ex) {
            log.info("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.info(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
        userAccountPasswordRecoveryService.passwordRecoveryDone(o);
    }

    @Order(4)
    @Test
    public void finish() {
        //deleteAll();
    }


    protected void deleteAll() {
        testHelperService.deleteAllRegistrations();
        testHelperService.deleteAllTasks();
        testHelperService.deleteAllProjects();
        testHelperService.deleteUserAccount();
    }
}
