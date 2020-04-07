package org.woehlke.simpleworklist;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.config.UserAccountTestDataService;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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


    private final String eyecatcherH1 = "##################################################################";
    private final String eyecatcherH2 = "------------------------------------------------------------------";
    private final String eyecatcherH3 = "******************************************************************";

    @BeforeEach
    public void setUp() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @BeforeEach setUp()");
        log.info(eyecatcherH2);
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        log.info(" Server URL: "+this.base.toString());
        //userAccountTestDataService.setUp();
        log.info(eyecatcherH1);
    }

    @BeforeAll
    public void runBeforeTestClass() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH2);
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
        log.info(" Server URL: "+this.base.toString());
        log.info(eyecatcherH2);
        userAccountTestDataService.setUp();
        log.info(eyecatcherH2);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH1);
    }

    @AfterAll
    public void runAfterTestClass() {
        log.info(eyecatcherH1);
        log.info(" @AfterTestClass clearContext");
        log.info(eyecatcherH2);
        SecurityContextHolder.clearContext();
        log.info(eyecatcherH1);
    }


    @DisplayName("Test Mock helloService + helloRepository")
    @Order(1)
    @Test
    public void testF001ServerStarts(){
        log.info(eyecatcherH1);
        log.info("testF001ServerStarts");
        log.info(eyecatcherH2);
        log.info("Server URL: "+this.base.toString());
        assertTrue(true);
        log.info(eyecatcherH2);
    }

    @Order(2)
    @Test
    public void testF002HomePageRendered(){
        log.info(eyecatcherH1);
        log.info("testF002HomePageRendered");
        log.info(eyecatcherH2);
        log.info(eyecatcherH2);
    }

    @Order(3)
    @Test
    public void testF003Registration(){
        log.info(eyecatcherH1);
        log.info("testF003Registration");
        log.info(eyecatcherH2);
    }

    @Order(4)
    @Test
    public void testF004PasswordRecovery(){
        log.info(eyecatcherH1);
        log.info("testF004PasswordRecovery");
        log.info(eyecatcherH2);
    }

    @Order(5)
    @Test
    public void testF005Login(){
        log.info(eyecatcherH1);
        log.info("testF005Login");
        log.info(eyecatcherH2);
    }

    @Order(6)
    @Test
    public void testF006PageAfterFirstSuccessfulLogin(){
        log.info(eyecatcherH1);
        log.info("testF006PageAfterFirstSuccessfulLogins");
        log.info(eyecatcherH2);
    }

    @Order(7)
    @Test
    public void testF007AddFirstNewTaskToInbox(){
        log.info(eyecatcherH1);
        log.info("testF007AddFirstNewTaskToInbox");
        log.info(eyecatcherH2);
    }

    @Order(8)
    @Test
    public void testF008AddAnotherNewTaskToInbox(){
        log.info(eyecatcherH1);
        log.info("testF008AddAnotherNewTaskToInbox");
        log.info(eyecatcherH2);
    }

    @Order(9)
    @Test
    public void testF009AddTaskToProjectRoot(){
        log.info(eyecatcherH1);
        log.info("testF009AddTaskToProjectRoot");
        log.info(eyecatcherH2);
    }

    @Order(10)
    @Test
    public void testF010AddSubProjectToProjectRoot(){
        log.info(eyecatcherH1);
        log.info("testF010AddSubProjectToProjectRoot");
        log.info(eyecatcherH2);
    }

    @Order(11)
    @Test
    public void testF011SetFocusOfTask(){
        log.info(eyecatcherH1);
        log.info("testF011SetFocusOfTask");
        log.info(eyecatcherH2);
    }

    @Order(12)
    @Test
    public void testF012UnSetFocusOfTask(){
        log.info(eyecatcherH1);
        log.info("testF012UnSetFocusOfTask");
        log.info(eyecatcherH2);
    }

    @Order(13)
    @Test
    public void testF013ShowTaskstateInbox(){
        log.info(eyecatcherH1);
        log.info("testF013ShowTaskstateInbox");
        log.info(eyecatcherH2);
    }

    @Order(14)
    @Test
    public void testF014ShowTaskstateToday(){
        log.info(eyecatcherH1);
        log.info("testF014ShowTaskstateToday");
        log.info(eyecatcherH2);
    }

    @Order(15)
    @Test
    public void testF015ShowTaskstateNext(){
        log.info(eyecatcherH1);
        log.info("testF015ShowTaskstateNext");
        log.info(eyecatcherH2);
    }

    @Order(16)
    @Test
    public void testF016ShowTaskstateWaiting(){
        log.info(eyecatcherH1);
        log.info("testF016ShowTaskstateWaiting");
        log.info(eyecatcherH2);
    }

    @Order(17)
    @Test
    public void testF017ShowTaskstateScheduled(){
        log.info(eyecatcherH1);
        log.info("testF017ShowTaskstateScheduled");
        log.info(eyecatcherH2);
    }

    @Order(18)
    @Test
    public void testF018ShowTaskstateSomeday(){
        log.info(eyecatcherH1);
        log.info("testF018ShowTaskstateSomeday");
        log.info(eyecatcherH2);
    }

    @Order(19)
    @Test
    public void testF019ShowTaskstateFocus(){
        log.info(eyecatcherH1);
        log.info("testF019ShowTaskstateFocus");
        log.info(eyecatcherH2);
    }

    @Order(20)
    @Test
    public void testF020ShowTaskstateCompleted(){
        log.info(eyecatcherH1);
        log.info("testF020ShowTaskstateCompleted");
        log.info(eyecatcherH2);
    }

    @Order(21)
    @Test
    public void testF021ShowTaskstateTrash(){
        log.info(eyecatcherH1);
        log.info("testF021ShowTaskstateTrash");
        log.info(eyecatcherH2);
    }

    @Order(22)
    @Test
    public void testF022TaskEdit(){
        log.info(eyecatcherH1);
        log.info("testF022TaskEdit");
        log.info(eyecatcherH2);
    }

    @Order(23)
    @Test
    public void testF023TaskEditFormChangeTaskstateViaDropDown(){
        log.info(eyecatcherH1);
        log.info("testF023TaskEditFormChangeTaskstateViaDropDown");
        log.info(eyecatcherH2);
    }

    @Order(24)
    @Test
    public void testF024TaskComplete(){
        log.info(eyecatcherH1);
        log.info("testF024TaskComplete");
        log.info(eyecatcherH2);
    }

    @Order(25)
    @Test
    public void testF025TaskIncomplete(){
        log.info(eyecatcherH1);
        log.info("testF025TaskIncomplete");
        log.info(eyecatcherH2);
    }

    @Order(26)
    @Test
    public void testF026TaskDelete(){
        log.info(eyecatcherH1);
        log.info("testF026TaskDelete");
        log.info(eyecatcherH2);
    }

    @Order(27)
    @Test
    public void testF027TaskUndelete(){
        log.info(eyecatcherH1);
        log.info("testF027TaskUndelete");
        log.info(eyecatcherH2);
    }

}
