package org.woehlke.java.simpleworklist.domain.db.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.woehlke.java.simpleworklist.SimpleworklistApplication;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.config.WebMvcConfig;
import org.woehlke.java.simpleworklist.config.WebSecurityConfig;

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
public class TokenGeneratorServiceIT {
}
