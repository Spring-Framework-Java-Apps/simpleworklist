package org.woehlke.java.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.java.simpleworklist.application.helper.TestHelperService;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountService;
import org.woehlke.java.simpleworklist.domain.security.access.UserAuthorizationService;
import org.woehlke.java.simpleworklist.domain.security.access.ApplicationUserDetailsService;
import org.woehlke.java.simpleworklist.domain.security.login.LoginSuccessService;


import java.net.URL;
import java.util.UUID;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


public abstract class AbstractIntegrationTest {

    //@LocalServerPort
    protected int port;

    protected URL base;

    //@Autowired
    protected SimpleworklistProperties simpleworklistProperties;

    //@Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

   // //@BeforeEach
    public void setUp() throws Exception {
        //log.info(" //@BeforeEach ");
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        for (UserAccount u : testUser) {
            UserAccount a = userAccountService.findByUserEmail(u.getUserEmail());
            if (a == null) {
                userAccountService.saveAndFlush(u);
            }
        }
    }

    //@BeforeTestClass
    public void setupClass() throws Exception {
        //log.info(" //@BeforeTestClass ");
    }

    //@AfterTestClass
    public void clearContext() {
       // log.info(" //@AfterTestClass ");
        SecurityContextHolder.clearContext();
    }

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

    protected void makeActiveUser(String username) {
        UserDetails ud = applicationUserDetailsService.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }

    protected void deleteAll(){
        testHelperService.deleteAllRegistrations();
        testHelperService.deleteAllTasks();
        testHelperService.deleteAllProjects();
        testHelperService.deleteUserAccount();
    }

    @Autowired
    protected TestHelperService testHelperService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected ApplicationUserDetailsService applicationUserDetailsService;

    @Autowired
    protected UserAuthorizationService userAuthorizationService;

    @Autowired
    protected LoginSuccessService loginSuccessService;

}
