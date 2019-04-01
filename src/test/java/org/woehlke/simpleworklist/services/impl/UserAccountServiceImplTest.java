package org.woehlke.simpleworklist.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Assert;

import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.*;


public class UserAccountServiceImplTest extends AbstractTest {

    @Autowired
    private UserRegistrationService registrationService;

    @Autowired
    private UserPasswordRecoveryService userPasswordRecoveryService;

    @Test
    public void testStartSecondOptIn() throws Exception {
        int zeroNumberOfAllRegistrations = 0;
        deleteAll();
        String email = applicationProperties.getRegistration().getMailFrom();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(email);
        Assert.assertTrue(userAccountService.isEmailAvailable(email));
        registrationService.registrationSendEmailTo(email);
        Assert.assertFalse(registrationService.registrationIsRetryAndMaximumNumberOfRetries(email));
        Assert.assertTrue(userAccountService.isEmailAvailable(email));
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
            userAccountService.saveAndFlush(userAccount);
        }
        int zeroNumberOfAllRegistrations = 0;
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(emails[0]);
        Assert.assertFalse(userAccountService.isEmailAvailable(emails[0]));
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(userPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        Assert.assertFalse(userAccountService.isEmailAvailable(emails[0]));
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(userPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(userPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(userPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertFalse(userPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        Assert.assertTrue(userPasswordRecoveryService.passwordRecoveryIsRetryAndMaximumNumberOfRetries(emails[0]));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }

    @Test
    public void testSaveAndFlush(){
        deleteAll();
        for(UserAccount userAccount:testUser){
            userAccountService.saveAndFlush(userAccount);
        }
        for(String email:emails){
            UserAccount user = userAccountService.findByUserEmail(email);
            Assert.assertTrue(user.getUserEmail().compareTo(email) == 0);
        }
        for(UserAccount user: userAccountService.findAll()){
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
            UserDetails userDetails = userAccountSecurityService.loadUserByUsername(email);
            Assert.assertTrue(userDetails.getUsername().compareTo(email) == 0);
        }
        try {
            UserDetails userDetails = userAccountSecurityService.loadUserByUsername(username_email);
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
        Assert.assertTrue(userAccountAccessService.authorize(loginFormBean));
        loginFormBean = new LoginFormBean();
        loginFormBean.setUserEmail(username_email);
        loginFormBean.setUserPassword(password);
        Assert.assertFalse(userAccountAccessService.authorize(loginFormBean));
    }

    @Test
    public void testIsEmailAvailable() {
        Assert.assertFalse(userAccountService.isEmailAvailable(emails[0]));
        Assert.assertTrue(userAccountService.isEmailAvailable(username_email));
    }

    @Test
    public void testCreateUser() {
        UserAccountFormBean userAccount = new UserAccountFormBean();
        userAccount.setUserEmail(username_email);
        userAccount.setUserPassword(password);
        userAccount.setUserPasswordConfirmation(password);
        userAccount.setUserFullname(full_name);
        userAccountService.createUser(userAccount);
        Assert.assertFalse(userAccountService.isEmailAvailable(username_email));
    }

    @Test
    public void testChangeUsersPassword(){
        UserAccountFormBean userAccount = new UserAccountFormBean();
        userAccount.setUserEmail(emails[0]);
        userAccount.setUserPassword(password + "_NEU");
        userAccount.setUserPasswordConfirmation(password + "_NEU");
        userAccount.setUserFullname(fullnames[0]);
        userAccountService.changeUsersPassword(userAccount);
    }

    @Test
    public void testRetrieveUsernameLoggedOut(){
        String userName = userAccountLoginSuccessService.retrieveUsername();
        Assert.assertTrue(userName.compareTo(" ")==0);
    }

    @Test
    public void testRetrieveUsernameLoggedIn(){
        makeActiveUser(emails[0]);
        String userName = userAccountLoginSuccessService.retrieveUsername();
        Assert.assertNotNull(userName);
        Assert.assertTrue(emails[0].compareTo(userName) == 0);
        SecurityContextHolder.clearContext();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testRetrieveCurrentUserLoggedOut(){
        userAccountLoginSuccessService.retrieveCurrentUser();
    }

    @Test
    public void testRetrieveCurrentUserLoggedIn(){
        makeActiveUser(emails[0]);
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Assert.assertNotNull(userAccount);
        Assert.assertTrue(emails[0].compareTo(userAccount.getUserEmail()) == 0);
        SecurityContextHolder.clearContext();
        deleteAll();
    }
}
