package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.TestHelperService;
import org.woehlke.simpleworklist.services.UserService;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class UserServiceImplTest {

    @Inject
    private RegistrationProcessService registrationService;

    @Inject
    private UserService userService;

    @Value("${worklist.registration.mail.from}")
    private String email;

    @Inject
    private TestHelperService testHelperService;

    private static String emails[] = {"test01@test.de", "test02@test.de", "test03@test.de"};
    private static String passwords[] = {"test01pwd", "test02pwd", "test03pwd"};
    private static String fullnames[] = {"test01 Name", "test02 Name", "test03 Name"};

    private static String username_email = "undefined@test.de";
    private static String password       = "ASDFG";
    private static String full_name      = "UNDEFINED_NAME";

    private static UserAccount testUser[] = new UserAccount[emails.length];

    static {
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
        }
    }

    private void makeActiveUser(String username) {
        UserDetails ud = userService.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }

    private void deleteAll(){
        testHelperService.deleteAllRegistrationProcess();
        testHelperService.deleteAllCategory();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
        testHelperService.deleteTimelineDay();
        testHelperService.deleteTimelineMonth();
        testHelperService.deleteTimelineYear();
    }

    @Test
    public void testStartSecondOptIn() throws Exception {
        int zeroNumberOfAllRegistrations = 0;
        deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(email);
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.registerNewUserSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.registerNewUserSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.registerNewUserSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.registerNewUserSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.registerNewUserSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.registerNewUserSendEmailTo(email);
        Assert.assertTrue(registrationService.isRetryAndMaximumNumberOfRetries(email));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }



    @Test
    public void testPasswordResetSendEmail() throws Exception {
        deleteAll();
        int zeroNumberOfAllRegistrations = 0;
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(email);
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.usersPasswordChangeSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.usersPasswordChangeSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.usersPasswordChangeSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.usersPasswordChangeSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.usersPasswordChangeSendEmailTo(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.usersPasswordChangeSendEmailTo(email);
        Assert.assertTrue(registrationService.isRetryAndMaximumNumberOfRetries(email));
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
        userAccount.setUserEmail(username_email);
        userAccount.setUserPassword(password + "_NEU");
        userAccount.setUserPasswordConfirmation(password + "_NEU");
        userAccount.setUserFullname(full_name);
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
