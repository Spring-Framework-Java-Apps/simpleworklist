package org.woehlke.simpleworklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SimpleworklistApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SimpleworklistApplication.class);
	}

    public static void main(String[] args) {
        SpringApplication.run(SimpleworklistApplication.class, args);
    }
}
