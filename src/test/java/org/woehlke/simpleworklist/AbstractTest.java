package org.woehlke.simpleworklist;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.config.di.WebMvcConfig;
import org.woehlke.simpleworklist.config.di.WebSecurityConfig;
import org.woehlke.simpleworklist.helper.TestHelperService;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.user.account.UserAccountService;
import org.woehlke.simpleworklist.user.account.UserAccountAccessService;
import org.woehlke.simpleworklist.user.account.UserAccountSecurityService;
import org.woehlke.simpleworklist.user.login.UserAccountLoginSuccessService;


import java.net.URL;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration({
    WebMvcConfig.class,
    WebSecurityConfig.class
})
@EnableConfigurationProperties({
    ApplicationProperties.class
})
public abstract class AbstractTest {

    @LocalServerPort
    protected int port;

    protected URL base;

    @Autowired
    protected ApplicationProperties applicationProperties;

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        log.info(" @BeforeEach ");
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        for (UserAccount u : testUser) {
            UserAccount a = userAccountService.findByUserEmail(u.getUserEmail());
            if (a == null) {
                userAccountService.saveAndFlush(u);
            }
        }
    }

    @BeforeTestClass
    public void setupClass() throws Exception {
        log.info(" @BeforeTestClass ");
    }

    @AfterTestClass
    public void clearContext() {
        log.info(" @AfterTestClass ");
        SecurityContextHolder.clearContext();
    }

    protected static String[] emails = {"test01@test.de", "test02@test.de", "test03@test.de"};
    protected static String[] passwords = {"test01pwd", "test02pwd", "test03pwd"};
    protected static String[] fullnames = {"test01 Name", "test02 Name", "test03 Name"};

    protected static String username_email = "undefined@test.de";
    protected static String password = "ASDFG";
    protected static String full_name = "UNDEFINED_NAME";

    protected static UserAccount[] testUser = new UserAccount[emails.length];

    static {
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
        }
    }

    protected void makeActiveUser(String username) {
        UserDetails ud = userAccountSecurityService.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }

    protected void deleteAll(){
        testHelperService.deleteAllRegistrationProcess();
        testHelperService.deleteAllActionItem();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
    }

    @Autowired
    protected TestHelperService testHelperService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected UserAccountSecurityService userAccountSecurityService;

    @Autowired
    protected UserAccountAccessService userAccountAccessService;

    @Autowired
    protected UserAccountLoginSuccessService userAccountLoginSuccessService;

}
