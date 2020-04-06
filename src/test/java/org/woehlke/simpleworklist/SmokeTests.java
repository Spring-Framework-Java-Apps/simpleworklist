package org.woehlke.simpleworklist;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.config.UserAccountTestDataService;

import java.net.URL;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmokeTests {

    @Autowired
    ServletWebServerApplicationContext server;

    @LocalServerPort
    int port;

    protected URL base;

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Autowired
    private UserAccountTestDataService userAccountTestDataService;

    @BeforeEach
    public void setUp() throws Exception {
        log.info(" @BeforeEach setUp()");
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        userAccountTestDataService.setUp();
    }

    @BeforeTestClass
    public void runBeforeTestClass() throws Exception {
        log.info(" @BeforeTestClass runBeforeTestClass");
    }

    @AfterTestClass
    public void runAfterTestClass() {
        log.info(" @AfterTestClass clearContext");
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testF001ServerStarts(){
        log.info("testF001ServerStarts");
    }

    @Test
    public void testF002HomePageRendered(){
        log.info("testF002HomePageRendered");
    }

    @Test
    public void testF003Registration(){
        log.info("testF003Registration");
    }

    @Test
    public void testF004PasswordRecovery(){
        log.info("testF004PasswordRecovery");
    }

    @Test
    public void testF005Login(){
        log.info("testF005Login");
    }

    @Test
    public void testF006PageAfterFirstSuccessfulLogin(){
        log.info("testF006PageAfterFirstSuccessfulLogins");
    }

    @Test
    public void testF007AddFirstNewTask(){
        log.info("testF007AddFirstNewTask");
    }

    @Test
    public void testF008AddFirstNewProject(){
        log.info("testF008AddFirstNewProject");
    }
}
