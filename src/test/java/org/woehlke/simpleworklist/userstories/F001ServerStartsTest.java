package org.woehlke.simpleworklist.userstories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.context.ContextService;
import org.woehlke.simpleworklist.context.NewContextForm;
import org.woehlke.simpleworklist.language.Language;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.user.account.UserAccountService;

import java.net.URL;
import java.util.Date;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Slf4j
//@DataJpaTest
//@WebMvcTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class F001ServerStartsTest {

    @Autowired
    ServletWebServerApplicationContext server;

    @LocalServerPort
    int port;

    protected URL base;

    @Autowired
    protected ApplicationProperties applicationProperties;

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected ContextService contextService;

    protected static String[] emails = {"test01//@Test.de", "test02//@Test.de", "test03//@Test.de"};
    protected static String[] passwords = {"test01pwd", "test02pwd", "test03pwd"};
    protected static String[] fullnames = {"test01 Name", "test02 Name", "test03 Name"};

    protected static String username_email = "undefined//@Test.de";
    protected static String password = "ASDFG";
    protected static String full_name = "UNDEFINED_NAME";

    protected static UserAccount[] testUser = new UserAccount[emails.length];

    static {
        Date lastLoginTimestamp = new Date();
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
            testUser[i].setDefaultLanguage(Language.EN);
            testUser[i].setLastLoginTimestamp(lastLoginTimestamp);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        log.info(" //@BeforeEach ");
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        for (UserAccount u : testUser) {
            UserAccount a = userAccountService.findByUserEmail(u.getUserEmail());
            if (a == null) {
                NewContextForm newContext = new NewContextForm("test","test");
                contextService.createNewContext(newContext,u);
                userAccountService.saveAndFlush(u);
            }
        }
    }

    @BeforeTestClass
    public void setupClass() throws Exception {
        log.info(" //@BeforeTestClass ");
    }

    @AfterTestClass
    public void clearContext() {
        log.info(" //@AfterTestClass ");
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testStartup(){
        log.info("testStartup");
    }
}
