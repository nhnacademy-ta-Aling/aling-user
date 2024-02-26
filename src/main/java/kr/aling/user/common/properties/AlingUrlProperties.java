package kr.aling.user.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Aling Gateway 서버 정보 저장 하는 Custom Properties.
 *
 * @author 박경서
 * @since 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "aling.server")
public class AlingUrlProperties {

    private String gatewayUrl;
    private String fileGatewayUrl;
}
