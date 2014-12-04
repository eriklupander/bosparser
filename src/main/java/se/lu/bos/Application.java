package se.lu.bos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

//@EnableCaching

@EnableScheduling
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "se.lu.bos.model" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

//    @Bean
//    public CacheManager cacheManager() {
//        // configure and return an implementation of Spring's CacheManager SPI
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("reportsCache")));
//        return cacheManager;
//    }
//
//    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:./db/bosparser;AUTO_SERVER=TRUE;MVCC=TRUE");
        dataSource.setUsername( "bosparser" );
        dataSource.setPassword( "bosparser" );

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return properties;
    }


//    @Configuration
//    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {
//
//        @Autowired
//        private SecurityProperties security;
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable().httpBasic()
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/rest/admin/**")
//                    .hasRole("ADMIN"); // .anyRequest().authenticated()
//        }
//        @Override
//        public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication().withUser("dev").password("dev")
//                    .roles("ADMIN", "USER");
//        }
//    }
//
////    @Bean(initMethod = "start", name = "esServer", destroyMethod = "stop")
////    public ElasticSearchServer esServer() {
////        Map<String,String> configuration = new HashMap<>();
////        configuration.put("location","classpath:elasticsearch-server.properties");
////        configuration.put("localOverride", "true");
////      //  configuration.put("path.conf", "${webapp.root}/WEB-INF/config");
////        return new ElasticSearchServer(configuration);
////    }

}
