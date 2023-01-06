package org.woehlke.java.simpleworklist.domain.meso;

import lombok.extern.java.Log;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.config.UserAccountTestDataService;
import org.woehlke.java.simpleworklist.config.WebMvcConfig;
import org.woehlke.java.simpleworklist.config.WebSecurityConfig;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.testdata.TestDataService;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Log
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SimpleworklistApplication.class)
@ImportAutoConfiguration({
    WebMvcConfig.class,
    WebSecurityConfig.class
})
@EnableConfigurationProperties({
    SimpleworklistProperties.class
})
public class TestDataServiceIT {

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
            log.info("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.info(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
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
            log.info("Exception: " + ex.getLocalizedMessage());
            for (StackTraceElement e : ex.getStackTrace()) {
                log.info(e.getClassName() + "." + e.getMethodName() + "in: " + e.getFileName() + " line: " + e.getLineNumber());
            }
        }
    }
}
