package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.application.helper.TestHelperService;
import org.woehlke.java.simpleworklist.config.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryStatus;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;

import java.net.URL;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
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
public class UserPasswordRecoveryControllerIT {

    @Autowired
    private ServletWebServerApplicationContext server;

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation")
    @LocalServerPort
    private int port;

    @Autowired
    private UserAccountTestDataService userAccountTestDataService;

    @Autowired
    protected TestHelperService testHelperService;

    private final String eyecatcherH1 = "##################################################################";
    private final String eyecatcherH2 = "------------------------------------------------------------------";
    private final String eyecatcherH3 = "******************************************************************";

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

    @BeforeEach
    public void setUp() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @BeforeEach setUp()");
        log.info(eyecatcherH2);

        //userAccountTestDataService.setUp();
        log.info(eyecatcherH1);
    }

    @BeforeAll
    public void runBeforeTestClass() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/");
        log.info(" Server URL: " + base.toString());
        log.info(eyecatcherH2);
        userAccountTestDataService.setUp();
        log.info(eyecatcherH2);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH1);
    }

    @AfterAll
    public void runAfterTestClass() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @AfterTestClass clearContext");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/");
        log.info(" Server URL: " + base.toString());
        log.info(eyecatcherH2);
        SecurityContextHolder.clearContext();
        log.info(eyecatcherH1);
    }


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
        deleteAll();
    }

    protected void deleteAll(){
        testHelperService.deleteAllRegistrations();
        testHelperService.deleteAllTasks();
        testHelperService.deleteAllProjects();
        testHelperService.deleteUserAccount();
    }
}
