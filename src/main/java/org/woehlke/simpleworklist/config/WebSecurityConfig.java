package org.woehlke.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.woehlke.simpleworklist.application.LoginSuccessHandler;
import org.woehlke.simpleworklist.services.UserAccountService;

@Configuration
@EnableWebSecurity
@EnableSpringDataWebSupport
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable()
            .authorizeRequests()
            .antMatchers("/css/*","/css/**","/img/**","/img/*","/js/*","/js/**","/webjars/*","/webjars/**","/resetPassword","/confirm/**","/register","/passwordResetConfirm/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureForwardUrl("/login?login_error=1")
            .defaultSuccessUrl("/tasks/inbox")
            .successHandler(loginSuccessHandler)
            .permitAll()
            .and()
            .logout()
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login")
            .permitAll();
    }

    @Bean
    public PasswordEncoder encoder(){
        // @see https://www.dailycred.com/article/bcrypt-calculator
        int strength = applicationProperties.getUser().getStrengthBCryptPasswordEncoder();
        return new BCryptPasswordEncoder(strength);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return auth.userDetailsService(userAccountService).passwordEncoder(encoder()).and().build();
    }

    @Autowired
    private AuthenticationManagerBuilder auth;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserAccountService userAccountService;

}
