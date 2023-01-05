package org.woehlke.java.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.woehlke.java.simpleworklist.domain.security.access.ApplicationUserDetailsService;
import org.woehlke.java.simpleworklist.domain.security.login.LoginSuccessHandler;

@Configuration
@EnableAsync
@EnableJpaAuditing
@EnableWebMvc
@EnableSpringDataWebSupport
@ImportAutoConfiguration({
    WebMvcConfig.class
})
@EnableConfigurationProperties({
    SimpleworklistProperties.class
})
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final SimpleworklistProperties simpleworklistProperties;
    private final LoginSuccessHandler loginSuccessHandler;

    @Autowired
    public WebSecurityConfig(
        ApplicationUserDetailsService applicationUserDetailsService,
        SimpleworklistProperties simpleworklistProperties,
        LoginSuccessHandler loginSuccessHandler) {
        this.applicationUserDetailsService = applicationUserDetailsService;
        this.simpleworklistProperties = simpleworklistProperties;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return this.applicationUserDetailsService;
    }

    /**
     * @see <a href="https://bcrypt-generator.com/">bcrypt-generator.com</a>
     * @return PasswordEncoder encoder
     */
    @Bean
    public PasswordEncoder encoder(){
        int strength = simpleworklistProperties.getWebSecurity().getStrengthBCryptPasswordEncoder();
        return new BCryptPasswordEncoder(strength);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider d = new DaoAuthenticationProvider();
        d.setPasswordEncoder(encoder());
        d.setUserDetailsService(userDetailsService());
        return d;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers((headers) -> headers.disable() )
            .authorizeRequests((authorizeRequests) -> authorizeRequests
                .requestMatchers(
                    simpleworklistProperties.getWebSecurity().getAntPatternsPublic()
                )
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
            )
            .csrf()
            .and()
            .formLogin((formLogin) -> formLogin
                .loginPage(simpleworklistProperties.getWebSecurity().getLoginPage())
                .usernameParameter(simpleworklistProperties.getWebSecurity().getUsernameParameter())
                .passwordParameter(simpleworklistProperties.getWebSecurity().getPasswordParameter())
                .loginProcessingUrl(simpleworklistProperties.getWebSecurity().getLoginProcessingUrl())
                .failureForwardUrl(simpleworklistProperties.getWebSecurity().getFailureForwardUrl())
                .defaultSuccessUrl(simpleworklistProperties.getWebSecurity().getDefaultSuccessUrl())
                .successHandler(this.loginSuccessHandler)
                .permitAll()
            )
            .csrf()
            .and()
            .logout((logout) ->  logout
                .logoutUrl(simpleworklistProperties.getWebSecurity().getLogoutUrl())
                .deleteCookies(simpleworklistProperties.getWebSecurity().getCookieNamesToClear())
                .invalidateHttpSession(simpleworklistProperties.getWebSecurity().getInvalidateHttpSession())
                .permitAll()
            );
        return http.build();
    }

}
