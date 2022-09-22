package org.woehlke.java.simpleworklist.domain.testdata;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.java.simpleworklist.config.UserAccountTestDataService;
import org.woehlke.java.simpleworklist.domain.meso.testdata.TestDataService;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDataServiceTest {

    @Autowired
    ServletWebServerApplicationContext server;

    @SuppressWarnings("deprecation")
    @LocalServerPort
    int port;

    protected URL base;

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Autowired
    private TestDataService testDataService;

    @Autowired
    private UserAccountTestDataService userAccountTestDataService;

    @BeforeEach
    public void setUp() {
        log.info(" @BeforeEach setUp()");
        try {
            this.base = new URL("http://localhost:" + port + "/");
            this.mockMvc = webAppContextSetup(wac).build();
        } catch (Exception ex) {
            log.warn("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.warn(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }

    @Test
    public void createTestCategoryTreeForUserAccountTest() {
        log.info("createTestCategoryTreeForUserAccountTest");
        try {
            userAccountTestDataService.setUp();
            UserAccount userAccount = userAccountTestDataService.getFirstUserAccount();
            //TODO: #128
            assertNotNull(userAccount);
            testDataService.createTestData(userAccount);
        } catch (Exception ex) {
            log.warn("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.warn(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }
}
