package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.java.simpleworklist.config.AbstractIntegrationTest;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountForm;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;
import org.woehlke.java.simpleworklist.domain.security.login.LoginForm;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserAccountServiceIT extends AbstractIntegrationTest {

    @Autowired
    private UserAccountRegistrationService registrationService;

    @Autowired
    private UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;

    //@Test
    public void testStartSecondOptIn() throws Exception {
        int zeroNumberOfAllRegistrations = 0;
        deleteAll();
        String email = simpleworklistProperties.getRegistration().getMailFrom();
        assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        assertNotNull(email);
        assertTrue(userAccountService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        assertTrue(userAccountService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        assertTrue(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }

    //@Test
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

    //@Test
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

    //@Test
    public void testLoadUserByUsername(){
        for(String email:emails){
            UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(email);
            assertTrue(userDetails.getUsername().compareTo(email) == 0);
        }
        try {
            UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(username_email);
        } catch (UsernameNotFoundException e){
            assertNotNull(e.getMessage());
            assertTrue(username_email.compareTo(e.getMessage())==0);
        }
    }

    //@Test
    public void testAuthorize(){
        LoginForm loginForm = new LoginForm();
        loginForm.setUserEmail(emails[0]);
        loginForm.setUserPassword(passwords[0]);
        assertTrue(userAuthorizationService.authorize(loginForm));
        loginForm = new LoginForm();
        loginForm.setUserEmail(username_email);
        loginForm.setUserPassword(password);
        assertFalse(userAuthorizationService.authorize(loginForm));
    }

    //@Test
    public void testIsEmailAvailable() {
        assertFalse(userAccountService.isEmailAvailable(emails[0]));
        assertTrue(userAccountService.isEmailAvailable(username_email));
    }

    //@Test
    public void testCreateUser() {
        UserAccountForm userAccount = new UserAccountForm();
        userAccount.setUserEmail(username_email);
        userAccount.setUserPassword(password);
        userAccount.setUserPasswordConfirmation(password);
        userAccount.setUserFullname(full_name);
        userAccountService.createUser(userAccount);
        assertFalse(userAccountService.isEmailAvailable(username_email));
    }

    //@Test
    public void testChangeUsersPassword(){
        UserAccountForm userAccount = new UserAccountForm();
        userAccount.setUserEmail(emails[0]);
        userAccount.setUserPassword(password + "_NEU");
        userAccount.setUserPasswordConfirmation(password + "_NEU");
        userAccount.setUserFullname(fullnames[0]);
        userAccountService.changeUsersPassword(userAccount);
    }

    //@Test
    public void testRetrieveUsernameLoggedOut(){
        String userName = loginSuccessService.retrieveUsername();
        assertTrue(userName.compareTo(" ")==0);
    }

    //@Test
    public void testRetrieveUsernameLoggedIn(){
        makeActiveUser(emails[0]);
        String userName = loginSuccessService.retrieveUsername();
        assertNotNull(userName);
        assertTrue(emails[0].compareTo(userName) == 0);
        SecurityContextHolder.clearContext();
    }

    //@Test
    ////@Test(expected = UsernameNotFoundException.class)
    public void testRetrieveCurrentUserLoggedOut(){
        loginSuccessService.retrieveCurrentUser();
    }

    //@Test
    public void testRetrieveCurrentUserLoggedIn(){
        makeActiveUser(emails[0]);
        UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
        assertNotNull(userAccount);
        assertTrue(emails[0].compareTo(userAccount.getUserEmail()) == 0);
        SecurityContextHolder.clearContext();
        deleteAll();
    }
}
