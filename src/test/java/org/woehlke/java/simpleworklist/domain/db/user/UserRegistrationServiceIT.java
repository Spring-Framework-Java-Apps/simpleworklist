package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.application.helper.TestHelperService;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.config.WebMvcConfig;
import org.woehlke.java.simpleworklist.config.WebSecurityConfig;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
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
public class UserRegistrationServiceIT {

    //@Value("#{registration.maxRetries}")
    //private int maxRetries;

    //@Value("#{org.woehlke.simpleworklist.registration.ttl.email.verifcation.request}")
    //private long ttlEmailVerificationRequest;

    @Autowired
    private SimpleworklistProperties simpleworklistProperties;

    @Autowired
    private UserAccountRegistrationService userAccountRegistrationService;

    @Autowired
    private UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;

    @Autowired
    protected TestHelperService testHelperService;

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


    protected void deleteAll(){
        /*
        testHelperService.deleteAllRegistrations();
        testHelperService.deleteAllTasks();
        testHelperService.deleteAllProjects();
        testHelperService.deleteUserAccount();
         */
    }

    @Order(1)
    @Test
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
        o.setNumberOfRetries(simpleworklistProperties.getRegistration().getMaxRetries());
        userAccountRegistrationService.registrationClickedInEmail(o);
        result = userAccountRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(emails[0]);
        assertTrue(result);
    }

    @Order(2)
    @Test
    public void testCheckIfResponseIsInTimeNewUser(){
        userAccountRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        UserAccountRegistration o = testHelperService.findRegistrationByEmail(emails[0]);
        //assertNull(o);
        assertNotNull(o);
        LocalDateTime a = o.getRowCreatedAt().minusSeconds(simpleworklistProperties.getRegistration().getTtlEmailVerificationRequest());
        o.setRowCreatedAt(a);
        o.setNumberOfRetries(0);
        userAccountRegistrationService.registrationClickedInEmail(o);
        userAccountRegistrationService.registrationCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findRegistrationByEmail(emails[0]);
        assertNotNull(o);
    }

    @Order(3)
    @Test
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
        LocalDateTime a = o.getRowCreatedAt().minusSeconds(simpleworklistProperties.getRegistration().getTtlEmailVerificationRequest());
        o.setRowCreatedAt(a);
        o.setNumberOfRetries(0);
        userAccountPasswordRecoveryService.passwordRecoveryClickedInEmail(o);
        userAccountPasswordRecoveryService.passwordRecoveryCheckIfResponseIsInTime(emails[0]);
        o = testHelperService.findPasswordRecoveryByEmail(emails[0]);
        assertNotNull(o);
    }
}
