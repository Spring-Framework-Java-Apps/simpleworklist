package org.woehlke.simpleworklist.config.di;

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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.user.login.LoginSuccessHandler;
import org.woehlke.simpleworklist.user.account.UserAccountSecurityService;


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
    ApplicationProperties.class
})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final UserAccountSecurityService userAccountSecurityService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public WebSecurityConfig(
        AuthenticationManagerBuilder authenticationManagerBuilder,
        LoginSuccessHandler loginSuccessHandler,
        UserAccountSecurityService userAccountSecurityService,
        ApplicationProperties applicationProperties) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.loginSuccessHandler = loginSuccessHandler;
        this.userAccountSecurityService = userAccountSecurityService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable()
            .authorizeRequests()
            .antMatchers(applicationProperties.getWebSecurity().getAntPatternsPublic())
            .permitAll()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .formLogin()
            .loginPage(applicationProperties.getWebSecurity().getLoginPage())
            .usernameParameter(applicationProperties.getWebSecurity().getUsernameParameter())
            .passwordParameter(applicationProperties.getWebSecurity().getPasswordParameter())
            .loginProcessingUrl(applicationProperties.getWebSecurity().getLoginProcessingUrl())
            .failureForwardUrl(applicationProperties.getWebSecurity().getFailureForwardUrl())
            .defaultSuccessUrl(applicationProperties.getWebSecurity().getDefaultSuccessUrl())
            .successHandler(loginSuccessHandler)
            .permitAll()
            .and()
            .logout()
            .logoutUrl(applicationProperties.getWebSecurity().getLogoutUrl())
            .deleteCookies(applicationProperties.getWebSecurity().getCookieNamesToClear())
            .invalidateHttpSession(applicationProperties.getWebSecurity().getInvalidateHttpSession())
            .permitAll();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return this.userAccountSecurityService;
    }

    @Bean
    public PasswordEncoder encoder(){
        int strength = applicationProperties.getWebSecurity().getStrengthBCryptPasswordEncoder();
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
        filter.setFilterProcessesUrl(applicationProperties.getWebSecurity().getLoginProcessingUrl());
        return filter;
    }
}
