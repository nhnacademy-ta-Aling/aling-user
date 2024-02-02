package kr.aling.user.config;

import javax.sql.DataSource;
import kr.aling.user.common.properties.MysqlProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final MysqlProperties mysqlProperties;


    /**
     * DBCP2 Bean 설정.
     *
     * @return DBCP2
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(mysqlProperties.getDriver());
        dataSource.setUrl(mysqlProperties.getUrl());
        dataSource.setUsername(mysqlProperties.getUsername());
        dataSource.setPassword(mysqlProperties.getPassword());

        dataSource.setInitialSize(mysqlProperties.getInitialSize());
        dataSource.setMaxTotal(mysqlProperties.getMaxTotal());
        dataSource.setMinIdle(mysqlProperties.getMinIdle());
        dataSource.setMaxIdle(mysqlProperties.getMaxIdle());
        dataSource.setMaxWaitMillis(mysqlProperties.getMaxWait());

        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setValidationQuery(mysqlProperties.getQuery());

        return dataSource;
    }
}
