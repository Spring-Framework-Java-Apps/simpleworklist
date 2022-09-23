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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.application.helper.TestHelperService;
import org.woehlke.java.simpleworklist.config.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationStatus;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;

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
public class UserRegistrationControllerIT {

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
    public void setUp() {
        log.info(eyecatcherH1);
        log.info(" @BeforeEach setUp()");
        log.info(eyecatcherH2);

        //userAccountTestDataService.setUp();
        log.info(eyecatcherH1);
    }

    @BeforeAll
    public void runBeforeTestClass() {
        log.info(eyecatcherH1);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH2);
        try {
            URL base = new URL("http://localhost:" + port + "/");
            log.info(" Server URL: " + base.toString());
            log.info(eyecatcherH2);
            userAccountTestDataService.setUp();
            log.info(eyecatcherH2);
            log.info(" @BeforeTestClass runBeforeTestClass");
            log.info(eyecatcherH1);
        } catch (Exception ex) {
            log.warn("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.warn(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }

    @AfterAll
    public void runAfterTestClass() {
        log.info(eyecatcherH1);
        log.info(" @AfterTestClass clearContext");
        log.info(eyecatcherH2);
        try {
            URL base = new URL("http://localhost:" + port + "/");
            log.info(" Server URL: " + base.toString());
            log.info(eyecatcherH2);
            SecurityContextHolder.clearContext();
            log.info(eyecatcherH1);
        } catch (Exception ex) {
            log.warn("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.warn(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }


    @Autowired
    private UserAccountRegistrationService userAccountRegistrationService;

    @Test
    public void testSignInFormularEmail() {
        try {
            this.mockMvc.perform(
                get("/user/register/"))
                .andDo(print())
                .andExpect(view().name(containsString("user/register/registerForm")));
        } catch (Exception ex) {
            log.warn("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.warn(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }

    @Test
    public void testSignInFormularAccount() {
        try {
            this.mockMvc.perform(
                get("/user/register/confirm/ASDF"))
                .andDo(print())
                .andExpect(view().name(containsString("user/register/registerConfirmFailed")));
        } catch (Exception ex) {
            log.warn("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.warn(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }


    @Test
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
                .andExpect(view().name(containsString("user/register/registerConfirmForm")))
                .andExpect(model().attributeExists("userAccountForm"));
        userAccountRegistrationService.registrationUserCreated(o);
    }
    @WithMockUser("test@test01.de")
    @Test
    public void finish(){
        deleteAll();
    }

    void deleteAll(){
        /*
        testHelperService.deleteAllRegistrations();
        testHelperService.deleteAllTasks();
        testHelperService.deleteAllProjects();
        testHelperService.deleteUserAccount();
         */
    }

}
