package org.woehlke.simpleworklist.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix="org.woehlke.simpleworklist")
@Validated
public class SimpleworklistProperties {

    @Valid
    private Mail mail = new Mail();

    @Valid
    private WebMvc webMvc = new WebMvc();

    @Valid
    private WebSecurity webSecurity = new WebSecurity();

    @Valid
    private Registration registration = new Registration();

    @Getter
    @Setter
    @Validated
    public static class Mail {

        @NotNull
        private String host;

        @NotNull
        private Integer port;

        @NotNull
        private String username;

        @NotNull
        private String password;

        @NotNull
        private Boolean auth;

        @NotNull
        private Boolean sslEnable;

        @NotNull
        private String socketFactoryPort;

        @NotNull
        private String socketFactoryClass;
    }

    @Getter
    @Setter
    @Validated
    public static class Registration {

        @NotNull
        private Integer maxRetries;

        @NotNull
        private Long ttlEmailVerificationRequest;

        @NotNull
        private String urlHost;

        @NotNull
        private String mailFrom;
    }

    @Getter
    @Setter
    @Validated
    public static class WebMvc {

        @NotNull
        private Integer controllerPageSize;

        private String[] staticResourceHandler;

        private String[] dynamicResourceHandler;
    }

    @Getter
    @Setter
    @Validated
    public static class WebSecurity {

        @NotNull
        private String loginProcessingUrl;

        @NotNull
        private String logoutUrl;

        private String[] cookieNamesToClear;

        @NotNull
        private Boolean invalidateHttpSession;

        @NotNull
        private String failureForwardUrl;

        @NotNull
        private String defaultSuccessUrl;

        @NotNull
        private String usernameParameter;

        @NotNull
        private String passwordParameter;

        @NotNull
        private String loginPage;

        private String[] antPatternsPublic;

        @NotNull
        private Integer strengthBCryptPasswordEncoder;

        /*
        @NotNull
        private Integer hashWidth;

        @NotBlank
        private String secret;
        */

    }

}
