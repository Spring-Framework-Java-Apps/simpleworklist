package org.woehlke.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.woehlke.simpleworklist.application.SimpleworklistProperties;

import java.util.Locale;
import java.util.Properties;


@Configuration
@EnableAsync
@EnableJpaAuditing
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableJpaRepositories({
    "org.woehlke.simpleworklist"
})
@EnableConfigurationProperties({
    SimpleworklistProperties.class
})
public class WebMvcConfig implements WebMvcConfigurer {

    private final SimpleworklistProperties simpleworklistProperties;

    @Autowired
    public WebMvcConfig(SimpleworklistProperties simpleworklistProperties) {
        this.simpleworklistProperties = simpleworklistProperties;
    }

    @Bean
    public JavaMailSender mailSender(){
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty(
            "mail.smtp.auth",
            simpleworklistProperties.getMail().getAuth().toString()
        );
        javaMailProperties.setProperty(
            "mail.smtp.ssl.enable",
            simpleworklistProperties.getMail().getSslEnable().toString()
        );
        javaMailProperties.setProperty(
            "mail.smtp.socketFactory.port",
            simpleworklistProperties.getMail().getSocketFactoryPort()
        );
        javaMailProperties.setProperty(
            "mail.smtp.socketFactory.class",
            simpleworklistProperties.getMail().getSocketFactoryClass()
        );
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(javaMailProperties);
        mailSender.setHost(simpleworklistProperties.getMail().getHost());
        mailSender.setPort(simpleworklistProperties.getMail().getPort());
        mailSender.setUsername(simpleworklistProperties.getMail().getUsername());
        mailSender.setPassword(simpleworklistProperties.getMail().getPassword());
        return mailSender;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.GERMANY);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource =  new  ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/taskstate/inbox");
        registry.addViewController("/home").setViewName("redirect:/taskstate/inbox");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for(String h : simpleworklistProperties.getWebMvc().getStaticResourceHandler()){
            String location = "classpath:/static"+h+"/";
            registry.addResourceHandler(h+"/*").addResourceLocations(location);
            registry.addResourceHandler(h+"/**").addResourceLocations(location);
        }
        for(String h : simpleworklistProperties.getWebMvc().getDynamicResourceHandler()){
            String location = h+"/";
            registry.addResourceHandler(h+"/*").addResourceLocations(location);
            registry.addResourceHandler(h+"/**").addResourceLocations(location);
        }
    }
}
