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
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.user.login.LoginSuccessHandler;
import org.woehlke.simpleworklist.user.account.UserAccountSecurityService;


@Configuration
@EnableAsync
@EnableJpaAuditing
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableJdbcHttpSession
@EnableWebSecurity
@ImportAutoConfiguration({
    ApplicationConfig.class,
    WebMvcConfig.class
})
@EnableConfigurationProperties({
    ApplicationProperties.class
})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public WebSecurityConfig(AuthenticationManagerBuilder auth, LoginSuccessHandler loginSuccessHandler, UserAccountSecurityService userAccountSecurityService) {
        this.auth = auth;
        this.loginSuccessHandler = loginSuccessHandler;
        this.userAccountSecurityService = userAccountSecurityService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable()
            .authorizeRequests()
            .antMatchers(
                    "/webjars/**", "/css/**", "/img/**", "/js/**", "/favicon.ico",
                    "/test*/**", "/login*", "/register*", "/confirm*/**",
                    "/resetPassword*", "/passwordResetConfirm*/**", "/error*"
            )
            .permitAll()
            //.and()
            //.authorizeRequests()
            .anyRequest().fullyAuthenticated().and()
            //.authenticated().antMatcher("/**").fullyAuthenticated()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("j_username").passwordParameter("j_password")
            .loginProcessingUrl("/j_spring_security_check")
            .failureForwardUrl("/login?login_error=1")
            .defaultSuccessUrl("/")
            .successHandler(loginSuccessHandler)
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        int strength = 10;
        PasswordEncoder encoder = new BCryptPasswordEncoder(strength);
        return auth.userDetailsService(userAccountSecurityService).passwordEncoder(encoder).and().build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/j_spring_security_check");
        return filter;
    }

    private final AuthenticationManagerBuilder auth;

    private final AuthenticationSuccessHandler loginSuccessHandler;

    private final UserDetailsService userAccountSecurityService;

}
