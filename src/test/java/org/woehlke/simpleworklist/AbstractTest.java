package org.woehlke.simpleworklist;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TestHelperService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public abstract class AbstractTest {

    @Inject
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Inject
    protected UserService userService;

    @Inject
    protected TestHelperService testHelperService;

    protected static String emails[] = {"test01@test.de", "test02@test.de", "test03@test.de"};
    protected static String passwords[] = {"test01pwd", "test02pwd", "test03pwd"};
    protected static String fullnames[] = {"test01 Name", "test02 Name", "test03 Name"};

    protected static String username_email = "undefined@test.de";
    protected static String password = "ASDFG";
    protected static String full_name = "UNDEFINED_NAME";

    protected static UserAccount testUser[] = new UserAccount[emails.length];

    static {
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
        }
    }

    protected void makeActiveUser(String username) {
        UserDetails ud = userService.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }

    protected void deleteAll(){
        testHelperService.deleteAllRegistrationProcess();
        testHelperService.deleteAllActionItem();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(wac).build();
        for (UserAccount u : testUser) {
            UserAccount a = userService.findByUserEmail(u.getUserEmail());
            if (a == null) {
                userService.saveAndFlush(u);
            }
        }
    }

    @After
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
