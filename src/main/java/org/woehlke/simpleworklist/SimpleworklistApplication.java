package org.woehlke.simpleworklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.woehlke.simpleworklist.config.di.ApplicationConfig;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.config.di.WebMvcConfig;
import org.woehlke.simpleworklist.config.di.WebSecurityConfig;


@ImportAutoConfiguration({
    ApplicationConfig.class,
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
