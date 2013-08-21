package org.woehlke.simpleworklist.services.impl;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.TestHelperService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.Date;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class RegistrationProcessServiceImplTest {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${worklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    @Inject
    private RegistrationProcessService registrationProcessService;

    @Inject
    private TestHelperService testHelperService;

    @Inject
    private UserService userService;

    private static String emails[] = {"test01@test.de", "test02@test.de", "test03@test.de"};
    private static String passwords[] = {"test01pwd", "test02pwd", "test03pwd"};
    private static String fullnames[] = {"test01 Name", "test02 Name", "test03 Name"};

    private static String username = "undefined@test.de";
    private static String password = "ASDFG";
    private static String name     = "UNDEFINED_NAME";

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
        testHelperService.deleteAllActionItem();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
        testHelperService.deleteTimelineDay();
        testHelperService.deleteTimelineMonth();
        testHelperService.deleteTimelineYear();
    }

    @Test
    public void testIsRetryAndMaximumNumberOfRetries(){
        deleteAll();
        boolean result = registrationProcessService.registerNewUserIsRetryAndMaximumNumberOfRetries(username);
        Assert.assertFalse(result);
        registrationProcessService.registerNewUserSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RegistrationProcess o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertTrue(o.getEmail().compareTo(emails[0])==0);
        o.setNumberOfRetries(maxRetries);
        registrationProcessService.registerNewUserClickedInEmail(o);
        result = registrationProcessService.registerNewUserIsRetryAndMaximumNumberOfRetries(emails[0]);
        Assert.assertTrue(result);
    }


    @Test
    public void testCheckIfResponseIsInTimeNewUser(){ /*
        registrationProcessService.registerNewUserSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */
        registrationProcessService.registerNewUserCheckIfResponseIsInTime(emails[0]);
        RegistrationProcess o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNotNull(o);
        o.setCreatedTimestamp(new Date(o.getCreatedTimestamp().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        registrationProcessService.registerNewUserClickedInEmail(o);
        registrationProcessService.registerNewUserCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNull(o);
    }

    @Test
    public void testCheckIfResponseIsInTime(){
        registrationProcessService.usersPasswordChangeSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        registrationProcessService.usersPasswordChangeCheckIfResponseIsInTime(emails[0]);
        RegistrationProcess o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNotNull(o);
        o.setCreatedTimestamp(new Date(o.getCreatedTimestamp().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        registrationProcessService.usersPasswordChangeClickedInEmail(o);
        registrationProcessService.usersPasswordChangeCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNull(o);
    }
}
