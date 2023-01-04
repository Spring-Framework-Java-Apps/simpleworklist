package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.config.UserAccountTestDataService;
import org.woehlke.java.simpleworklist.config.WebMvcConfig;
import org.woehlke.java.simpleworklist.config.WebSecurityConfig;

import java.net.URL;

@Slf4j
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
public class ContextControllerIT {
    @Autowired
    private ServletWebServerApplicationContext server;

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation")
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
        log.info(" Server URL: " + base.toString());
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
        log.info(" Server URL: " + base.toString());
        log.info(eyecatcherH2);
        SecurityContextHolder.clearContext();
        log.info(eyecatcherH1);
    }

}
