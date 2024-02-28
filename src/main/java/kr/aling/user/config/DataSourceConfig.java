package kr.aling.user.config;

import javax.sql.DataSource;
import kr.aling.user.common.properties.Dbcp2Properties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 데이터 소스 설정.
 *
 * @author 정유진
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final Dbcp2Properties dbcp2Properties;


    /**
     * DBCP2 Bean 설정.
     *
     * @return DBCP2
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(dbcp2Properties.getDriver());
        dataSource.setUrl(dbcp2Properties.getUrl());
        dataSource.setUsername(dbcp2Properties.getUsername());
        dataSource.setPassword(dbcp2Properties.getPassword());

        dataSource.setInitialSize(dbcp2Properties.getInitialSize());
        dataSource.setMaxTotal(dbcp2Properties.getMaxTotal());
        dataSource.setMinIdle(dbcp2Properties.getMinIdle());
        dataSource.setMaxIdle(dbcp2Properties.getMaxIdle());
        dataSource.setMaxWaitMillis(dbcp2Properties.getMaxWaitMillis());

        dataSource.setTestOnBorrow(dbcp2Properties.getTestOnBorrow());
        dataSource.setTestOnReturn(dbcp2Properties.getTestOnReturn());
        dataSource.setValidationQuery(dbcp2Properties.getValidationQuery());

        return dataSource;
    }
}
