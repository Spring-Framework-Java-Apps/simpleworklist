package org.woehlke.simpleworklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.woehlke.simpleworklist.application.ApplicationProperties;
import org.woehlke.simpleworklist.application.config.WebMvcConfig;
import org.woehlke.simpleworklist.application.config.WebSecurityConfig;


@ImportAutoConfiguration({
    WebMvcConfig.class,
    WebSecurityConfig.class
})
@EnableConfigurationProperties({
    ApplicationProperties.class
})
@SpringBootApplication
public class SimpleworklistApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleworklistApplication.class, args);
    }
}
