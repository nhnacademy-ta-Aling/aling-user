package kr.aling.user.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "aling.mysql")
public class MysqlProperties {

    private String driver;
    private String url;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer maxTotal;
    private Integer minIdle;
    private Integer maxIdle;
    private Integer maxWait;
    private String query;

}
