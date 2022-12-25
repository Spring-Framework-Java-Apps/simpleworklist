package org.woehlke.java.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.woehlke.java.simpleworklist.domain.security.access.ApplicationUserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
public class WebSecurityConfig /* extends WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> */ {

    //private final AuthenticationManagerBuilder authenticationManagerBuilder;
    //private final AuthenticationSuccessHandler authenticationSuccessHandler;
    //private final AuthenticationManager authenticationManager;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final SimpleworklistProperties simpleworklistProperties;

    @Autowired
    public WebSecurityConfig(
        //AuthenticationManagerBuilder auth,
        //AuthenticationSuccessHandler authenticationSuccessHandler,
        //AuthenticationManager authenticationManager,
        ApplicationUserDetailsService applicationUserDetailsService,
        SimpleworklistProperties simpleworklistProperties
    ) {
        //this.authenticationManagerBuilder = auth;
        //this.authenticationSuccessHandler = authenticationSuccessHandler;
        //this.authenticationManager = authenticationManager;
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

    /*
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationManagerBuilder
            .userDetailsService(userDetailsService())
            .passwordEncoder(encoder()).and().build();
    }
    */

    /*
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl(simpleworklistProperties.getWebSecurity().getLoginProcessingUrl());
        return filter;
    }

    private AuthenticationManagerBuilder authenticationBuilder;

    private AuthenticationManagerBuilder localConfigureAuthenticationBldr;

    private ApplicationContext context;

    private HttpSecurity http;

    private boolean disableDefaults;

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    private ContentNegotiationStrategy contentNegotiationStrategy = new HeaderContentNegotiationStrategy();

    private ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
        @Override
        public <T> T postProcess(T object) {
            throw new IllegalStateException(ObjectPostProcessor.class.getName()
                + " is a required bean. Ensure you have used @EnableWebSecurity and @Configuration");
        }
    };

    private AuthenticationEventPublisher getAuthenticationEventPublisher() {
        if (this.context.getBeanNamesForType(AuthenticationEventPublisher.class).length > 0) {
            return this.context.getBean(AuthenticationEventPublisher.class);
        }
        return this.objectPostProcessor.postProcess(new DefaultAuthenticationEventPublisher());
    }
    */

    /**
     * Creates the shared objects
     * @return the shared Objects
     */
    /*
    private Map<Class<?>, Object> createSharedObjects() {
        Map<Class<?>, Object> sharedObjects = new HashMap<>();
        sharedObjects.putAll(this.localConfigureAuthenticationBldr.getSharedObjects());
        sharedObjects.put(UserDetailsService.class, userDetailsService());
        sharedObjects.put(ApplicationContext.class, this.context);
        sharedObjects.put(ContentNegotiationStrategy.class, this.contentNegotiationStrategy);
        sharedObjects.put(AuthenticationTrustResolver.class, this.trustResolver);
        return sharedObjects;
    }

    private void applyDefaultConfiguration(HttpSecurity http) throws Exception {
        http.csrf();
        http.addFilter(new WebAsyncManagerIntegrationFilter());
        http.exceptionHandling();
        http.headers();
        http.sessionManagement();
        http.securityContext();
        http.requestCache();
        http.anonymous();
        http.servletApi();
        http.apply(new DefaultLoginPageConfigurer<>());
        http.logout();
    }
    */
    /**
     * Creates the {@link HttpSecurity} or returns the current instance
     * @return the {@link HttpSecurity}
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    /*
    protected final HttpSecurity getHttp() throws Exception {
        if (this.http != null) {
            return this.http;
        }
        AuthenticationEventPublisher eventPublisher = getAuthenticationEventPublisher();
        this.localConfigureAuthenticationBldr.authenticationEventPublisher(eventPublisher);
        this.authenticationBuilder.parentAuthenticationManager(authenticationManager);
        Map<Class<?>, Object> sharedObjects = createSharedObjects();
        this.http = new HttpSecurity(this.objectPostProcessor, this.authenticationBuilder, sharedObjects);
        if (!this.disableDefaults) {
            applyDefaultConfiguration(this.http);
            ClassLoader classLoader = this.context.getClassLoader();
            List<AbstractHttpConfigurer> defaultHttpConfigurers = SpringFactoriesLoader
                .loadFactories(AbstractHttpConfigurer.class, classLoader);
            for (AbstractHttpConfigurer configurer : defaultHttpConfigurers) {
                this.http.apply(configurer);
            }
        }
        configure(this.http);
        return this.http;
    }


    ///@Override
    public void init(WebSecurity web) throws Exception {
        HttpSecurity http = getHttp();
        web.addSecurityFilterChainBuilder(http).postBuildAction(() -> {
            FilterSecurityInterceptor securityInterceptor = http.getSharedObject(FilterSecurityInterceptor.class);
            web.securityInterceptor(securityInterceptor);
        });
    }


    //@Override
    public void configure(WebSecurity builder) throws Exception {

    }
    */

   /*
    public void configure(HttpSecurity builder) throws Exception {

        http
            .headers((headers) -> headers.disable() )
            .authorizeRequests((authorizeRequests) -> authorizeRequests
                .antMatchers(
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
                //.successHandler(authenticationSuccessHandler)
                .permitAll()
            )
            .csrf()
            .and()
            .logout((logout)->  logout
                .logoutUrl(simpleworklistProperties.getWebSecurity().getLogoutUrl())
                .deleteCookies(simpleworklistProperties.getWebSecurity().getCookieNamesToClear())
                .invalidateHttpSession(simpleworklistProperties.getWebSecurity().getInvalidateHttpSession())
                .permitAll()
            );

    }
    */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
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
                .antMatchers(
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
                //.successHandler(authenticationSuccessHandler)
                .permitAll()
            )
            .csrf()
            .and()
            .logout((logout)->  logout
                .logoutUrl(simpleworklistProperties.getWebSecurity().getLogoutUrl())
                .deleteCookies(simpleworklistProperties.getWebSecurity().getCookieNamesToClear())
                .invalidateHttpSession(simpleworklistProperties.getWebSecurity().getInvalidateHttpSession())
                .permitAll()
            );
        return http.build();
    }

}
