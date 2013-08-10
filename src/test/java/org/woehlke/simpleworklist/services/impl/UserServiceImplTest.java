package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class UserServiceImplTest {
	
	@Inject
	private RegistrationProcessService registrationService;
	
	@Inject
	private UserService userService;
	
	@Autowired
	@Value("${worklist.registration.mail.from}")
	private String email;
	
	@Before
	public void setup(){
		//registrationService.deleteAll();
	}
	
	@After
	public void cleanup(){
		//registrationService.deleteAll();
	}
	
	@Test
	public void testStartSecondOptIn() throws Exception {
        registrationService.deleteAll();
        Assert.assertEquals(0,registrationService.getNumberOfAll());
		Assert.assertNotNull(email);
		Assert.assertTrue(userService.isEmailAvailable(email));
		registrationService.startSecondOptIn(email);
		Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
		Assert.assertTrue(userService.isEmailAvailable(email));
		registrationService.startSecondOptIn(email);
		Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
		registrationService.startSecondOptIn(email);
		Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
		registrationService.startSecondOptIn(email);
		Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
		registrationService.startSecondOptIn(email);
		Assert.assertFalse(registrationService.isRetryAndMaximumNumberOfRetries(email));
		registrationService.startSecondOptIn(email);
		Assert.assertTrue(registrationService.isRetryAndMaximumNumberOfRetries(email));
		Thread.sleep(4000);
        registrationService.deleteAll();
        Assert.assertEquals(0,registrationService.getNumberOfAll());
	}
}
