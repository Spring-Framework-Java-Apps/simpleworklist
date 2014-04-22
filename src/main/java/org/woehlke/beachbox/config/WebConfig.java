package org.woehlke.beachbox.config;

import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * Created by tw on 22.04.14.
 */
@Configuration
@ComponentScan("org.woehlke.beachbox")
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="org.woehlke.beachbox")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPersistenceUnitName("beachboxPU");
        HibernateJpaVendorAdapter jva = new HibernateJpaVendorAdapter();
        jva.setShowSql(true);
        jva.setGenerateDdl(true);
        jva.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        bean.setJpaVendorAdapter(jva);
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto","update");
        jpaProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
        jpaProperties.setProperty("hibernate.show_sql","false");
        jpaProperties.setProperty("hibernate.generate_statistics","true");
        jpaProperties.setProperty("jpa.showSql","false");
        jpaProperties.setProperty("jpa.database","MYSQL");
        jpaProperties.setProperty("hibernate.connection.useUnicode","true");
        jpaProperties.setProperty("hibernate.connection.characterEncoding","UTF-8");
        bean.setJpaProperties(jpaProperties);
        return bean;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/beachbox?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8");
        ds.setUsername("beachbox");
        ds.setPassword("beachboxpwd");
        return ds;
    }

    @Bean
    public JpaDialect jpaDialect(){
        return new HibernateJpaDialect();
    }

    @Bean
    public PlatformTransactionManager transactionManager()
    {
        EntityManagerFactory factory = entityManagerFactory().getObject();
        JpaTransactionManager tx = new JpaTransactionManager();
        tx.setEntityManagerFactory(factory);
        tx.setJpaDialect(jpaDialect());
        return tx;
    }

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }


    @Bean
    public ViewResolver resolver(){
        InternalResourceViewResolver url = new InternalResourceViewResolver();
        url.setViewClass(JstlView.class);
        url.setPrefix("/WEB-INF/views/");
        url.setSuffix(".jsp");
        return url;
    }

    @Bean
    public PageableArgumentResolver pageableArgumentResolver(){
        return new PageableArgumentResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }

    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(resolvers());
    }

    @Bean
    public ServletWebArgumentResolverAdapter resolvers() {
        return new ServletWebArgumentResolverAdapter(pageable());
    }

    @Bean
    public PageableArgumentResolver pageable() {
        return new PageableArgumentResolver();
    }


}
