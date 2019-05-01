package org.woehlke.simpleworklist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
@Validated
@ConfigurationProperties(prefix="org.woehlke.simpleworklist")
public class ApplicationProperties {

    @Valid
    private Mail mail = new Mail();

    @Valid
    private Mvc mvc = new Mvc();

    @Valid
    private Registration registration = new Registration();

    @Valid
    private User user = new User();

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

        @Valid
        private Smtp smtp = new Smtp();

        public static class Smtp {

            @NotNull
            private Boolean auth;

            @NotNull
            private Boolean sslEnable;

            @NotNull
            private String socketFactoryPort;

            @NotNull
            private String socketFactoryClass;

            public Boolean getAuth() {
                return auth;
            }

            public void setAuth(Boolean auth) {
                this.auth = auth;
            }

            public Boolean getSslEnable() {
                return sslEnable;
            }

            public void setSslEnable(Boolean sslEnable) {
                this.sslEnable = sslEnable;
            }

            public String getSocketFactoryPort() {
                return socketFactoryPort;
            }

            public void setSocketFactoryPort(String socketFactoryPort) {
                this.socketFactoryPort = socketFactoryPort;
            }

            public String getSocketFactoryClass() {
                return socketFactoryClass;
            }

            public void setSocketFactoryClass(String socketFactoryClass) {
                this.socketFactoryClass = socketFactoryClass;
            }
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Smtp getSmtp() {
            return smtp;
        }

        public void setSmtp(Smtp smtp) {
            this.smtp = smtp;
        }
    }

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

        public Integer getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
        }

        public Long getTtlEmailVerificationRequest() {
            return ttlEmailVerificationRequest;
        }

        public void setTtlEmailVerificationRequest(Long ttlEmailVerificationRequest) {
            this.ttlEmailVerificationRequest = ttlEmailVerificationRequest;
        }

        public String getUrlHost() {
            return urlHost;
        }

        public void setUrlHost(String urlHost) {
            this.urlHost = urlHost;
        }

        public String getMailFrom() {
            return mailFrom;
        }

        public void setMailFrom(String mailFrom) {
            this.mailFrom = mailFrom;
        }
    }

    @Validated
    public static class Mvc {

        @NotNull
        private Integer controllerPageSize;

        public Integer getControllerPageSize() {
            return controllerPageSize;
        }

        public void setControllerPageSize(Integer controllerPageSize) {
            this.controllerPageSize = controllerPageSize;
        }
    }

    @Validated
    public static class User {

        @NotNull
        private Integer strengthBCryptPasswordEncoder;

        public Integer getStrengthBCryptPasswordEncoder() {
            return strengthBCryptPasswordEncoder;
        }

        public void setStrengthBCryptPasswordEncoder(Integer strengthBCryptPasswordEncoder) {
            this.strengthBCryptPasswordEncoder = strengthBCryptPasswordEncoder;
        }
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Mvc getMvc() {
        return mvc;
    }

    public void setMvc(Mvc mvc) {
        this.mvc = mvc;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
