package org.woehlke.java.simpleworklist.domain.user.register;

import org.springframework.beans.factory.annotation.Value;
import org.woehlke.java.simpleworklist.config.AbstractTest;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationServiceImplTest extends AbstractTest {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${org.woehlke.simpleworklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    @Autowired
    private UserAccountRegistrationService userAccountRegistrationService;

    @Autowired
    private UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;

    //@Test
    public void testIsRetryAndMaximumNumberOfRetries(){
        deleteAll();
        boolean result = userAccountRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(username_email);
        assertFalse(result);
        userAccountRegistrationService.registrationSendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserAccountRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        assertTrue(o.getEmail().compareTo(emails[0])==0);
        o.setNumberOfRetries(maxRetries);
        userAccountRegistrationService.registrationClickedInEmail(o);
        result = userAccountRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(emails[0]);
        assertTrue(result);
    }


    //@Test
    public void testCheckIfResponseIsInTimeNewUser(){
        userAccountRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        UserAccountRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNotNull(o);
        o.setRowCreatedAt(new Date(o.getRowCreatedAt().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        userAccountRegistrationService.registrationClickedInEmail(o);
        userAccountRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNull(o);
    }

    //@Test
    public void testCheckIfResponseIsInTime(){
        userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(emails[0]);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userAccountPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        UserAccountPasswordRecovery o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNotNull(o);
        o.setRowCreatedAt(new Date(o.getRowCreatedAt().getTime() - ttlEmailVerificationRequest));
        o.setNumberOfRetries(0);
        userAccountPasswordRecoveryService.passwordRecoveryClickedInEmail(o);
        userAccountPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNull(o);
    }
}
