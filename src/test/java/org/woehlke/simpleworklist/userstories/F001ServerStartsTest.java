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
import org.woehlke.simpleworklist.config.TestDataUser;
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

    private TestDataUser testDataUser = new TestDataUser();

    @BeforeEach
    public void setUp() throws Exception {
        log.info(" //@BeforeEach ");
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        for (int i = 0; i < testDataUser.getTestUser().length; i++) {
            UserAccount a = userAccountService.findByUserEmail(testDataUser.getTestUser()[i].getUserEmail());
            if (a == null) {
                //NewContextForm newContext = new NewContextForm("test","test");
                //contextService.createNewContext(newContext,u);
                UserAccount persisted = userAccountService.saveAndFlush(testDataUser.getTestUser()[i]);
                testDataUser.getTestUser()[i] = persisted;
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
