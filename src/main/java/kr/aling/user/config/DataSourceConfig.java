package kr.aling.user.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The Configuration bean class for using commons DBCP DataSource.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "mysql")
public class DataSourceConfig {
    private String url;
    private String userName;
    private String password;
    private Integer minIdle;
    private Integer maxIdle;
    private Integer initialSize;
    private Integer maxWait;
    private Integer maxTotal;
    private String validationQuery;
    private String driverName;

    /**
     * DBCP DataSource Bean.
     *
     * @return set DataSource
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMaxWaitMillis(maxWait);

        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        return dataSource;
    }

    /**
     * EntityManagerFactory Bean.
     *
     * @param dataSource for DBCP
     * @return set EntityManagerFactory
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("kr.aling.user");
        emf.setJpaVendorAdapter(jpaVendorAdapter());

        return emf;
    }

    /**
     * JpaVendorAdapter Bean.
     *
     * @return set JpaVendorAdapter
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hjv = new HibernateJpaVendorAdapter();
        hjv.setDatabase(Database.MYSQL);
        return hjv;
    }

    /**
     * TransactionManager Bean.
     *
     * @param entityManagerFactory set EntityManagerFactory Bean
     * @return set TransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jtm = new JpaTransactionManager();
        jtm.setEntityManagerFactory(entityManagerFactory);

        return jtm;
    }
}
