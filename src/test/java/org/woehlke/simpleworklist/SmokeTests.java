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
    public void testF007AddFirstNewTaskToInbox(){
        log.info("testF007AddFirstNewTaskToInbox");
    }

    @Test
    public void testF008AddAnotherNewTaskToInbox(){
        log.info("testF008AddAnotherNewTaskToInbox");
    }

    @Test
    public void testF009AddTaskToProjectRoot(){
        log.info("testF009AddTaskToProjectRoot");
    }
    @Test
    public void testF010AddSubProjectToProjectRoot(){
        log.info("testF010AddSubProjectToProjectRoot");
    }

    @Test
    public void testF011SetFocusOfTask(){
        log.info("testF011SetFocusOfTask");
    }

    @Test
    public void testF012UnSetFocusOfTask(){
        log.info("testF012UnSetFocusOfTask");
    }

    @Test
    public void testF013ShowTaskstateInbox(){
        log.info("testF013ShowTaskstateInbox");
    }

    @Test
    public void testF014ShowTaskstateToday(){
        log.info("testF014ShowTaskstateToday");
    }

    @Test
    public void testF015ShowTaskstateNext(){
        log.info("testF015ShowTaskstateNext");
    }

    @Test
    public void testF016ShowTaskstateWaiting(){
        log.info("testF016ShowTaskstateWaiting");
    }

    @Test
    public void testF017ShowTaskstateScheduled(){
        log.info("testF017ShowTaskstateScheduled");
    }

    @Test
    public void testF018ShowTaskstateSomeday(){
        log.info("testF018ShowTaskstateSomeday");
    }

    @Test
    public void testF019ShowTaskstateFocus(){
        log.info("testF019ShowTaskstateFocus");
    }

    @Test
    public void testF020ShowTaskstateCompleted(){
        log.info("testF020ShowTaskstateCompleted");
    }

    @Test
    public void testF021ShowTaskstateTrash(){
        log.info("testF021ShowTaskstateTrash");
    }

    @Test
    public void testF022TaskEdit(){
        log.info("testF022TaskEdit");
    }

    @Test
    public void testF023TaskEditFormChangeTaskstateViaDropDown(){
        log.info("testF023TaskEditFormChangeTaskstateViaDropDown");
    }

    @Test
    public void testF024TaskComplete(){
        log.info("testF024TaskComplete");
    }

    @Test
    public void testF025TaskIncomplete(){
        log.info("testF025TaskIncomplete");
    }

    @Test
    public void testF026TaskDelete(){
        log.info("testF026TaskDelete");
    }

    @Test
    public void testF027TaskUndelete(){
        log.info("testF027TaskUndelete");
    }


}
