package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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

    @Test
    public void testStartSecondOptIn() throws Exception {
        testHelperService.deleteAll();
        int zeroNumberOfAllRegistrations = 0;
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
        Assert.assertNotNull(email);
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.sendEmailForVerification(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        Assert.assertTrue(userService.isEmailAvailable(email));
        registrationService.sendEmailForVerification(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.sendEmailForVerification(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.sendEmailForVerification(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.sendEmailForVerification(email);
        Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
        registrationService.sendEmailForVerification(email);
        Assert.assertTrue(registrationService.isRetryAndMaximumNumberOfRetries(email));
        int sixSeconds = 6000;
        Thread.sleep(sixSeconds);
        testHelperService.deleteAll();
        Assert.assertEquals(zeroNumberOfAllRegistrations, testHelperService.getNumberOfAllRegistrations());
    }
}
