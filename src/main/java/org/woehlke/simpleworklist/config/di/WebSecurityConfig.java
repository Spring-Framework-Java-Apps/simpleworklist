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

    @Autowired
    public WebSecurityConfig(
        AuthenticationManagerBuilder authenticationManagerBuilder,
        LoginSuccessHandler loginSuccessHandler,
        UserAccountSecurityService userAccountSecurityService
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.loginSuccessHandler = loginSuccessHandler;
        this.userAccountSecurityService = userAccountSecurityService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable()
            .authorizeRequests()
            .antMatchers(antPatternsPublic)
            .permitAll()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .formLogin()
            .loginPage(loginPage)
            .usernameParameter(usernameParameter).passwordParameter(passwordParameter)
            .loginProcessingUrl(loginProcessingUrl)
            .failureForwardUrl(failureForwardUrl)
            .defaultSuccessUrl(defaultSuccessUrl)
            .successHandler(loginSuccessHandler)
            .permitAll()
            .and()
            .logout()
            .logoutUrl(logoutUrl)
            .deleteCookies(cookieNamesToClear)
            .invalidateHttpSession(invalidateHttpSession)
            .permitAll()
            .and()
            .csrf()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/error/error-403");
    }

    private final static String loginProcessingUrl =  "/j_spring_security_check";
    private final static String logoutUrl = "/logout";
    private final static String[] cookieNamesToClear = {"JSESSIONID"};
    private final static boolean invalidateHttpSession = true;
    private final static String defaultSuccessUrl = "/";
    private final static String failureForwardUrl = "/login?login_error=1";
    private final static String usernameParameter = "j_username";
    private final static String passwordParameter = "j_password";
    private final static String loginPage = "/login";
    private final static String[] antPatternsPublic = {
        "/webjars/**", "/css/**", "/img/**", "/js/**", "/favicon.ico",
        "/test*/**", "/login*", "/register*", "/confirm*/**",
        "/resetPassword*", "/passwordResetConfirm*/**", "/error*"
    };
    private final static int strengthBCryptPasswordEncoder = 10;

    @Bean
    public UserDetailsService userDetailsService(){
        return this.userAccountSecurityService;
    }

    @Bean
    public PasswordEncoder encoder(){
        int strength = strengthBCryptPasswordEncoder;
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
        filter.setFilterProcessesUrl(loginProcessingUrl);
        return filter;
    }
}
