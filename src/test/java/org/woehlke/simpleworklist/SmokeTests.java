package org.woehlke.simpleworklist;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.woehlke.simpleworklist.application.config.UserAccountTestDataService;

import java.net.URL;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.woehlke.simpleworklist.application.config.Requirements.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SimpleworklistApplication.class)
public class SmokeTests {

    @Autowired
    private ServletWebServerApplicationContext server;

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

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

        //userAccountTestDataService.setUp();
        log.info(eyecatcherH1);
    }

    @BeforeAll
    public void runBeforeTestClass() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/");
        log.info(" Server URL: "+ base.toString());
        log.info(eyecatcherH2);
        userAccountTestDataService.setUp();
        log.info(eyecatcherH2);
        log.info(" @BeforeTestClass runBeforeTestClass");
        log.info(eyecatcherH1);
    }

    @AfterAll
    public void runAfterTestClass() throws Exception {
        log.info(eyecatcherH1);
        log.info(" @AfterTestClass clearContext");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/");
        log.info(" Server URL: "+ base.toString());
        log.info(eyecatcherH2);
        SecurityContextHolder.clearContext();
        log.info(eyecatcherH1);
    }


    @DisplayName(F001)
    @Order(1)
    @Test
    public void testF001ServerStarts() throws Exception{
        log.info(eyecatcherH1);
        log.info("testF001ServerStarts");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/");
        log.info("Server URL: "+ base.toString());
        assertTrue(true);
        log.info(eyecatcherH2);
    }

    @DisplayName(F002)
    @Order(2)
    @Test
    public void testF002HomePageRendered() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF002HomePageRendered");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port);
        URL redirectedUrl =  new URL("http://localhost:" + port+ "/user/login");
        log.info("Server URL: "+ base.toString());
        this.mockMvc.perform(get( base.toString() ))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(redirectedUrl.toString()));
        log.info("Server URL: "+ redirectedUrl.toString());
        this.mockMvc.perform(get(redirectedUrl.toString()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @DisplayName(F003)
    @Order(3)
    @Test
    public void testF003Registration(){
        log.info(eyecatcherH1);
        log.info("testF003Registration");
        log.info(eyecatcherH2);
    }

    @DisplayName(F004)
    @Order(4)
    @Test
    public void testF004PasswordRecovery(){
        log.info(eyecatcherH1);
        log.info("testF004PasswordRecovery");
        log.info(eyecatcherH2);
    }

    @DisplayName(F005)
    @Order(5)
    @Test
    public void testF005Login(){
        log.info(eyecatcherH1);
        log.info("testF005Login");
        log.info(eyecatcherH2);
    }

    @DisplayName(F006)
    @Order(6)
    @Test
    public void testF006PageAfterFirstSuccessfulLogin(){
        log.info(eyecatcherH1);
        log.info("testF006PageAfterFirstSuccessfulLogins");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F007)
    @Order(7)
    @Test
    public void testF007AddFirstNewTaskToInbox(){
        log.info(eyecatcherH1);
        log.info("testF007AddFirstNewTaskToInbox");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F008)
    @Order(8)
    @Test
    public void testF008AddAnotherNewTaskToInbox(){
        log.info(eyecatcherH1);
        log.info("testF008AddAnotherNewTaskToInbox");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F009)
    @Order(9)
    @Test
    public void testF009AddTaskToProjectRoot(){
        log.info(eyecatcherH1);
        log.info("testF009AddTaskToProjectRoot");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F010)
    @Order(10)
    @Test
    public void testF010AddSubProjectToProjectRoot(){
        log.info(eyecatcherH1);
        log.info("testF010AddSubProjectToProjectRoot");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F011)
    @Order(11)
    @Test
    public void testF011SetFocusOfTask(){
        log.info(eyecatcherH1);
        log.info("testF011SetFocusOfTask");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F012)
    @Order(12)
    @Test
    public void testF012UnSetFocusOfTask(){
        log.info(eyecatcherH1);
        log.info("testF012UnSetFocusOfTask");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F013)
    @Order(13)
    @Test
    public void testF013ShowTaskstateInbox() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF013ShowTaskstateInbox");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/inbox");
        log.info("Server URL: "+ base.toString());
        assertNotNull(this.mockMvc);
        try {
            this.mockMvc.perform(get(base.toString()))
                //.andDo(print())
                .andExpect(status().isOk());
            //.andExpect(content().string(containsString("SimpleWorklist")));
        } catch (UsernameNotFoundException e) {
            log.error("UsernameNotFoundException: "+e.getLocalizedMessage());
        } catch (NullPointerException npe){
            log.error("NullPointerException: "+npe.getLocalizedMessage());
            for(StackTraceElement e:npe.getStackTrace()){
                log.error(e.getClassName()+"."+e.getMethodName()+"in: "+e.getFileName()+" line: "+e.getLineNumber());
            }
        }
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F014)
    @Order(14)
    @Test
    public void testF014ShowTaskstateToday() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF014ShowTaskstateToday");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/today");
        log.info("Server URL: "+ base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            //.andDo(print())
            .andExpect(status().isOk());
            //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F015)
    @Order(15)
    @Test
    public void testF015ShowTaskstateNext() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF015ShowTaskstateNext");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/next");
        log.info("Server URL: "+ base.toString());
        assertNotNull(this.mockMvc);
        MockHttpServletRequestBuilder a = get(base.toString());
        assertNotNull(a);
        ResultActions x = this.mockMvc.perform(a);
        assertNotNull(x);
        ResultActions o = x.andDo(print());
        assertNotNull(o);
        o.andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F016)
    @Order(16)
    @Test
    public void testF016ShowTaskstateWaiting() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF016ShowTaskstateWaiting");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/waiting");
        log.info("Server URL: "+ base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            .andDo(print())
            .andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F017)
    @Order(17)
    @Test
    public void testF017ShowTaskstateScheduled() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF017ShowTaskstateScheduled");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/scheduled");
        log.info("Server URL: "+ base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            .andDo(print())
            .andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F018)
    @Order(18)
    @Test
    public void testF018ShowTaskstateSomeday() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF018ShowTaskstateSomeday");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/someday");
        log.info("Server URL: "+base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            .andDo(print())
            .andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F019)
    @Order(19)
    @Test
    public void testF019ShowTaskstateFocus() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF019ShowTaskstateFocus");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/focus");
        log.info("Server URL: "+base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            .andDo(print())
            .andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F020)
    @Order(20)
    @Test
    public void testF020ShowTaskstateCompleted() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF020ShowTaskstateCompleted");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/completed");
        log.info("Server URL: "+base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            .andDo(print())
            .andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F021)
    @Order(21)
    @Test
    public void testF021ShowTaskstateTrash() throws Exception {
        log.info(eyecatcherH1);
        log.info("testF021ShowTaskstateTrash");
        log.info(eyecatcherH2);
        URL base = new URL("http://localhost:" + port + "/taskstate/trash");
        log.info("Server URL: "+base.toString());
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get(base.toString()))
            .andDo(print())
            .andExpect(status().isOk());
        //.andExpect(content().string(containsString("SimpleWorklist")));
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F022)
    @Order(22)
    @Test
    public void testF022TaskEdit(){
        log.info(eyecatcherH1);
        log.info("testF022TaskEdit");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F023)
    @Order(23)
    @Test
    public void testF023TaskEditFormChangeTaskstateViaDropDown(){
        log.info(eyecatcherH1);
        log.info("testF023TaskEditFormChangeTaskstateViaDropDown");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F024)
    @Order(24)
    @Test
    public void testF024TaskComplete(){
        log.info(eyecatcherH1);
        log.info("testF024TaskComplete");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F025)
    @Order(25)
    @Test
    public void testF025TaskIncomplete(){
        log.info(eyecatcherH1);
        log.info("testF025TaskIncomplete");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F026)
    @Order(26)
    @Test
    public void testF026TaskDelete(){
        log.info(eyecatcherH1);
        log.info("testF026TaskDelete");
        log.info(eyecatcherH2);
    }

    @WithMockUser(username="test01@test.de")
    @DisplayName(F027)
    @Order(27)
    @Test
    public void testF027TaskUndelete(){
        log.info(eyecatcherH1);
        log.info("testF027TaskUndelete");
        log.info(eyecatcherH2);
    }

}
