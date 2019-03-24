package org.woehlke.simpleworklist.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.Date;

public class RegistrationProcessServiceImplTest extends AbstractTest {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${org.woehlke.simpleworklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    @Inject
    private RegistrationProcessService registrationProcessService;

    @Inject
    private UserService userService;

    @Test
    public void testIsRetryAndMaximumNumberOfRetries(){
        deleteAll();
        boolean result = registrationProcessService.registrationIsRetryAndMaximumNumberOfRetries(username_email);
        Assert.assertFalse(result);
        registrationProcessService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RegistrationProcess o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertTrue(o.getEmail().compareTo(emails[0])==0);
        o.setNumberOfRetries(maxRetries);
        registrationProcessService.registrationClickedInEmail(o);
        result = registrationProcessService.registrationIsRetryAndMaximumNumberOfRetries(emails[0]);
        Assert.assertTrue(result);
    }


    @Test
    public void testCheckIfResponseIsInTimeNewUser(){
        registrationProcessService.registrationCheckIfResponseIsInTime(emails[0]);
        RegistrationProcess o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNotNull(o);
        o.setCreatedTimestamp(new Date(o.getCreatedTimestamp().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        registrationProcessService.registrationClickedInEmail(o);
        registrationProcessService.registrationCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findByEmailRegistration(emails[0]);
        Assert.assertNull(o);
    }

    @Test
    public void testCheckIfResponseIsInTime(){
        registrationProcessService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        registrationProcessService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        RegistrationProcess o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNotNull(o);
        o.setCreatedTimestamp(new Date(o.getCreatedTimestamp().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        registrationProcessService.passwordRecoveryClickedInEmail(o);
        registrationProcessService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findByEmailPasswordRecovery(emails[0]);
        Assert.assertNull(o);
    }
}
