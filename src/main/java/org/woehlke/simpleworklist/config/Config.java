package org.woehlke.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class Config {

    @Bean
    public JavaMailSender mailSender(){
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty(
                "mail.smtp.auth",
                applicationProperties.getMail().getSmtp().getAuth().toString()
        );
        javaMailProperties.setProperty(
                "mail.smtp.ssl.enable",
                applicationProperties.getMail().getSmtp().getSslEnable().toString()
        );
        javaMailProperties.setProperty(
                "mail.smtp.socketFactory.port",
                applicationProperties.getMail().getSmtp().getSocketFactoryPort()
        );
        javaMailProperties.setProperty(
                "mail.smtp.socketFactory.class",
                applicationProperties.getMail().getSmtp().getSocketFactoryClass()
        );
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(javaMailProperties);
        mailSender.setHost(applicationProperties.getMail().getHost());
        mailSender.setPort(applicationProperties.getMail().getPort());
        mailSender.setUsername(applicationProperties.getMail().getUsername());
        mailSender.setPassword(applicationProperties.getMail().getPassword());
        return mailSender;
    }

    @Autowired
    protected ApplicationProperties applicationProperties;
}
