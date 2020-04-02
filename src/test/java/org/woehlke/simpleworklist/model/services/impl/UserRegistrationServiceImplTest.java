package org.woehlke.simpleworklist.model.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery;
import org.woehlke.simpleworklist.oodm.entities.UserRegistration;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecoveryService;
import org.woehlke.simpleworklist.user.register.UserRegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

public class UserRegistrationServiceImplTest extends AbstractTest {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${org.woehlke.simpleworklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserPasswordRecoveryService userPasswordRecoveryService;

    @Test
    public void testIsRetryAndMaximumNumberOfRetries(){
        deleteAll();
        boolean result = userRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(username_email);
        Assert.assertFalse(result);
        userRegistrationService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserRegistration o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertTrue(o.getEmail().compareTo(emails[0])==0);
        o.setNumberOfRetries(maxRetries);
        userRegistrationService.registrationClickedInEmail(o);
        result = userRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(emails[0]);
        Assert.assertTrue(result);
    }


    @Test
    public void testCheckIfResponseIsInTimeNewUser(){
        userRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        UserRegistration o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNotNull(o);
        o.setRowCreatedAt(new Date(o.getRowCreatedAt().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        userRegistrationService.registrationClickedInEmail(o);
        userRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNull(o);
    }

    @Test
    public void testCheckIfResponseIsInTime(){
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        UserPasswordRecovery o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNotNull(o);
        o.setRowCreatedAt(new Date(o.getRowCreatedAt().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        userPasswordRecoveryService.passwordRecoveryClickedInEmail(o);
        userPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNull(o);
    }
}
