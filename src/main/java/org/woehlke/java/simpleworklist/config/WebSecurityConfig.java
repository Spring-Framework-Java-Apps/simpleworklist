package org.woehlke.java.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.woehlke.java.simpleworklist.domain.security.access.ApplicationUserDetailsService;


@Configuration
@EnableAsync
@EnableJpaAuditing
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableWebSecurity
@ImportAutoConfiguration({
    WebMvcConfig.class
})
@EnableConfigurationProperties({
    SimpleworklistProperties.class
})
public class WebSecurityConfig implements WebSecurityConfigurer<WebSecurity> {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final SimpleworklistProperties simpleworklistProperties;

    @Autowired
    public WebSecurityConfig(
        AuthenticationManagerBuilder auth,
        AuthenticationSuccessHandler authenticationSuccessHandler,
        ApplicationUserDetailsService applicationUserDetailsService,
        SimpleworklistProperties simpleworklistProperties) {
        this.authenticationManagerBuilder = auth;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.applicationUserDetailsService = applicationUserDetailsService;
        this.simpleworklistProperties = simpleworklistProperties;
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
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationManagerBuilder
            .userDetailsService(userDetailsService())
            .passwordEncoder(encoder()).and().build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl(simpleworklistProperties.getWebSecurity().getLoginProcessingUrl());
        return filter;
    }

    @Override
    public void init(WebSecurity builder) throws Exception {

    }

    @Override
    public void configure(WebSecurity builder) throws Exception {

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers()
            .disable()
            .authorizeRequests()
            .antMatchers(
                simpleworklistProperties.getWebSecurity().getAntPatternsPublic()
            )
            .permitAll()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .csrf()
            .and()
            .formLogin()
            .loginPage(simpleworklistProperties.getWebSecurity().getLoginPage())
            .usernameParameter(simpleworklistProperties.getWebSecurity().getUsernameParameter())
            .passwordParameter(simpleworklistProperties.getWebSecurity().getPasswordParameter())
            .loginProcessingUrl(simpleworklistProperties.getWebSecurity().getLoginProcessingUrl())
            .failureForwardUrl(simpleworklistProperties.getWebSecurity().getFailureForwardUrl())
            .defaultSuccessUrl(simpleworklistProperties.getWebSecurity().getDefaultSuccessUrl())
            //.successHandler(authenticationSuccessHandler)
            .permitAll()
            .and()
            .csrf()
            .and()
            .logout()
            .logoutUrl(simpleworklistProperties.getWebSecurity().getLogoutUrl())
            .deleteCookies(simpleworklistProperties.getWebSecurity().getCookieNamesToClear())
            .invalidateHttpSession(simpleworklistProperties.getWebSecurity().getInvalidateHttpSession())
            .permitAll();
        return http.build();
    }

}
