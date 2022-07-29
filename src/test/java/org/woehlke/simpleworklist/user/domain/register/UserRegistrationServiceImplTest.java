package org.woehlke.simpleworklist.user.domain.register;

import org.springframework.beans.factory.annotation.Value;
import org.woehlke.simpleworklist.application.config.AbstractTest;
import org.woehlke.simpleworklist.domain.user.register.UserRegistration;
import org.woehlke.simpleworklist.domain.user.passwordrecovery.UserPasswordRecovery;
import org.woehlke.simpleworklist.domain.user.passwordrecovery.UserPasswordRecoveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.domain.user.register.UserRegistrationService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationServiceImplTest extends AbstractTest {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${org.woehlke.simpleworklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserPasswordRecoveryService userPasswordRecoveryService;

    //@Test
    public void testIsRetryAndMaximumNumberOfRetries(){
        deleteAll();
        boolean result = userRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(username_email);
        assertFalse(result);
        userRegistrationService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        assertTrue(o.getEmail().compareTo(emails[0])==0);
        o.setNumberOfRetries(maxRetries);
        userRegistrationService.registrationClickedInEmail(o);
        result = userRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(emails[0]);
        assertTrue(result);
    }


    //@Test
    public void testCheckIfResponseIsInTimeNewUser(){
        userRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        UserRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNotNull(o);
        o.setRowCreatedAt(new Date(o.getRowCreatedAt().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        userRegistrationService.registrationClickedInEmail(o);
        userRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNull(o);
    }

    //@Test
    public void testCheckIfResponseIsInTime(){
        userPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        UserPasswordRecovery o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNotNull(o);
        o.setRowCreatedAt(new Date(o.getRowCreatedAt().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        userPasswordRecoveryService.passwordRecoveryClickedInEmail(o);
        userPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNull(o);
    }
}
