package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import org.junit.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;


public class UserServiceImplTest extends AbstractTest {

    @Inject
    private RegistrationProcessService registrationService;

    @Inject
    private UserService userService;

    @Value("${worklist.registration.mail.from}")
    private String email;

    @Test
    public void testStartSecondOptIn() throws Exception {
        int zeroNumberOfAllRegistrations = 0;
        deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(email);
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertTrue(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }

    @Test
    public void testPasswordResetSendEmail() throws Exception {
        deleteAll();
        for(UserAccount userAccount:testUser){
            userService.saveAndFlush(userAccount);
        }
        int zeroNumberOfAllRegistrations = 0;
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(emails[0]);
        Assert.assertFalse(userService.isEmailAvailable(emails[0]));
        registrationService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(registrationService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        Assert.assertFalse(userService.isEmailAvailable(emails[0]));
        registrationService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(registrationService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        registrationService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(registrationService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        registrationService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(registrationService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        registrationService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(registrationService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        registrationService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertTrue(registrationService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }

    @Test
    public void testSaveAndFlush(){
        deleteAll();
        for(UserAccount userAccount:testUser){
            userService.saveAndFlush(userAccount);
        }
        for(String email:emails){
            UserAccount user = userService.findByUserEmail(email);
            Assert.assertTrue(user.getUserEmail().compareTo(email) == 0);
        }
        for(UserAccount user:userService.findAll()){
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(user.getUserEmail());
            Assert.assertNotNull(user.getUserPassword());
            Assert.assertNotNull(user.getUserFullname());
            Assert.assertNotNull(user.getCreatedTimestamp());
        }
    }

    @Test
    public void testLoadUserByUsername(){
        for(String email:emails){
            UserDetails userDetails = userService.loadUserByUsername(email);
            Assert.assertTrue(userDetails.getUsername().compareTo(email) == 0);
        }
        try {
            UserDetails userDetails = userService.loadUserByUsername(username_email);
        } catch (UsernameNotFoundException e){
            Assert.assertNotNull(e.getMessage());
            Assert.assertTrue(username_email.compareTo(e.getMessage())==0);
        }
    }

    @Test
    public void testAuthorize(){
        LoginFormBean loginFormBean = new LoginFormBean();
        loginFormBean.setUserEmail(emails[0]);
        loginFormBean.setUserPassword(passwords[0]);
        Assert.assertTrue(userService.authorize(loginFormBean));
        loginFormBean = new LoginFormBean();
        loginFormBean.setUserEmail(username_email);
        loginFormBean.setUserPassword(password);
        Assert.assertFalse(userService.authorize(loginFormBean));
    }

    @Test
    public void testIsEmailAvailable() {
        Assert.assertFalse(userService.isEmailAvailable(emails[0]));
        Assert.assertTrue(userService.isEmailAvailable(username_email));
    }

    @Test
    public void testCreateUser() {
        UserAccountFormBean userAccount = new UserAccountFormBean();
        userAccount.setUserEmail(username_email);
        userAccount.setUserPassword(password);
        userAccount.setUserPasswordConfirmation(password);
        userAccount.setUserFullname(full_name);
        userService.createUser(userAccount);
        Assert.assertFalse(userService.isEmailAvailable(username_email));
    }

    @Test
    public void testChangeUsersPassword(){
        UserAccountFormBean userAccount = new UserAccountFormBean();
        userAccount.setUserEmail(emails[0]);
        userAccount.setUserPassword(password + "_NEU");
        userAccount.setUserPasswordConfirmation(password + "_NEU");
        userAccount.setUserFullname(fullnames[0]);
        userService.changeUsersPassword(userAccount);
    }

    @Test
    public void testRetrieveUsernameLoggedOut(){
        String userName = userService.retrieveUsername();
        Assert.assertTrue(userName.compareTo(" ")==0);
    }

    @Test
    public void testRetrieveUsernameLoggedIn(){
        makeActiveUser(emails[0]);
        String userName = userService.retrieveUsername();
        Assert.assertNotNull(userName);
        Assert.assertTrue(emails[0].compareTo(userName) == 0);
        SecurityContextHolder.clearContext();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testRetrieveCurrentUserLoggedOut(){
        userService.retrieveCurrentUser();
    }

    @Test
    public void testRetrieveCurrentUserLoggedIn(){
        makeActiveUser(emails[0]);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Assert.assertNotNull(userAccount);
        Assert.assertTrue(emails[0].compareTo(userAccount.getUserEmail()) == 0);
        SecurityContextHolder.clearContext();
        deleteAll();
    }
}
