package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.application.helper.TestHelperService;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.config.WebMvcConfig;
import org.woehlke.java.simpleworklist.config.WebSecurityConfig;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountForm;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountService;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;
import org.woehlke.java.simpleworklist.domain.security.access.ApplicationUserDetailsService;
import org.woehlke.java.simpleworklist.domain.security.access.UserAuthorizationService;
import org.woehlke.java.simpleworklist.domain.security.login.LoginForm;
import org.woehlke.java.simpleworklist.domain.security.login.LoginSuccessService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;



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
public class UserAccountServiceIT {

    @Autowired
    private UserAccountRegistrationService registrationService;

    @Autowired
    private UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;

    @Autowired
    protected TestHelperService testHelperService;

    @Autowired
    protected ContextService contextService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected ApplicationUserDetailsService applicationUserDetailsServiceImpl;

    @Autowired
    protected UserAuthorizationService userAuthorizationService;

    @Autowired
    protected LoginSuccessService loginSuccessService;

    @Autowired
    protected SimpleworklistProperties simpleworklistProperties;

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

    @Ignore
    @Order(1)
    @Test
    public void testStartSecondOptIn() throws Exception {
        int zeroNumberOfAllRegistrations = 1;
        deleteAll();
        String email = simpleworklistProperties.getRegistration().getMailFrom();
        assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        assertNotNull(email);
        assertTrue(userAccountService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        assertTrue(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        assertTrue(userAccountService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertTrue(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertTrue(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }

    @Ignore
    @Order(2)
    @Test
    public void testPasswordResetSendEmail() throws Exception {
        deleteAll();
        for(UserAccount userAccount:testUser){
            userAccountService.saveAndFlush(userAccount);
        }
        int zeroNumberOfAllRegistrations = 0;
        assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        assertNotNull(emails[0]);
        assertFalse(userAccountService.isEmailAvailable(emails[0]));
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        assertFalse(userAccountPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        assertFalse(userAccountService.isEmailAvailable(emails[0]));
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        assertFalse(userAccountPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        assertFalse(userAccountPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        assertFalse(userAccountPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        assertFalse(userAccountPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        assertTrue(userAccountPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }

    @Ignore
    @Order(3)
    @Test
    public void testSaveAndFlush(){
        deleteAll();
        for(UserAccount userAccount:testUser){
            userAccountService.saveAndFlush(userAccount);
        }
        for(String email:emails){
            UserAccount user = userAccountService.findByUserEmail(email);
            assertTrue(user.getUserEmail().compareTo(email) == 0);
        }
        Pageable request = PageRequest.of(0,10);
        for(UserAccount user: userAccountService.findAll(request)){
            assertNotNull(user.getId());
            assertNotNull(user.getUserEmail());
            assertNotNull(user.getUserPassword());
            assertNotNull(user.getUserFullname());
            assertNotNull(user.getRowCreatedAt());
        }
    }

    @Ignore
    @Order(4)
    @Test
    public void testLoadUserByUsername(){
        for(String email:emails){
            try {
                UserDetails userDetails = applicationUserDetailsServiceImpl.loadUserByUsername(email);
                assertTrue(userDetails.getUsername().compareTo(email) == 0);
            } catch (UsernameNotFoundException e){
                assertNotNull(e.getMessage());
                assertFalse(username_email.compareTo(e.getMessage())==0);
            }
        }
    }

    @Ignore
    @Order(5)
    @Test
    public void testAuthorize(){
        LoginForm loginForm = new LoginForm();
        loginForm.setUserEmail(emails[0]);
        loginForm.setUserPassword(passwords[0]);
        assertFalse(userAuthorizationService.authorize(loginForm));
        loginForm = new LoginForm();
        loginForm.setUserEmail(username_email);
        loginForm.setUserPassword(password);
        assertFalse(userAuthorizationService.authorize(loginForm));
    }

    @Ignore
    @Order(6)
    @Test
    public void testIsEmailAvailable() {
        assertTrue(userAccountService.isEmailAvailable(emails[0]));
        assertFalse(userAccountService.isEmailAvailable(username_email));
    }


    @Ignore
    @Order(7)
    @Test
    public void testCreateUser() {
        UserAccountForm userAccount = new UserAccountForm();
        userAccount.setUserEmail(username_email);
        userAccount.setUserPassword(password);
        userAccount.setUserPasswordConfirmation(password);
        userAccount.setUserFullname(full_name);
        userAccountService.createUser(userAccount);
        assertTrue(userAccountService.isEmailAvailable(username_email));
    }

    @Ignore
    @Order(8)
    @Test
    public void testChangeUsersPassword(){
        UserAccountForm userAccount = new UserAccountForm();
        userAccount.setUserEmail(emails[0]);
        userAccount.setUserPassword(password + "_NEU");
        userAccount.setUserPasswordConfirmation(password + "_NEU");
        userAccount.setUserFullname(fullnames[0]);
        userAccountService.changeUsersPassword(userAccount);
    }

    @Ignore
    @Order(9)
    @Test
    public void testRetrieveUsernameLoggedOut(){
        String userName = loginSuccessService.retrieveUsername();
        assertTrue(userName.compareTo(" ")==0);
    }

    @Ignore
    @Order(10)
    @Test
    public void testRetrieveUsernameLoggedIn(){
        makeActiveUser(emails[0]);
        String userName = loginSuccessService.retrieveUsername();
        assertNotNull(userName);
        assertTrue(emails[0].compareTo(userName) == 0);
        SecurityContextHolder.clearContext();
    }

    @Ignore
    @Order(11)
    @Test
    //@Test(expected = UsernameNotFoundException.class)
    public void testRetrieveCurrentUserLoggedOut(){
        loginSuccessService.retrieveCurrentUser();
    }

    @Ignore
    @Order(12)
    @Test
    public void testRetrieveCurrentUserLoggedIn(){
        makeActiveUser(emails[0]);
        UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
        assertNotNull(userAccount);
        assertTrue(emails[0].compareTo(userAccount.getUserEmail()) == 0);
        SecurityContextHolder.clearContext();
        deleteAll();
    }

    protected void deleteAll(){
        /*2d
        testHelperService.deleteAllRegistrations();
        testHelperService.deleteAllTasks();
        testHelperService.deleteAllProjects();
        testHelperService.deleteUserAccount();
        */
    }

    protected void makeActiveUser(String username) {
        UserDetails ud = applicationUserDetailsServiceImpl.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }
}
