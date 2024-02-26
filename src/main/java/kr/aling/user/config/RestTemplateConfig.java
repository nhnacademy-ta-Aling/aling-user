package kr.aling.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate Config.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Configuration
public class RestTemplateConfig {

    /**
     * clientHttpRequestFactory 빈.
     *
     * @return ClientHttpRequestFactory 객체 반환
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        return requestFactory;
    }

    /**
     * restTemplate 빈.
     *
     * @param clientHttpRequestFactory clientHttpRequestFactory
     * @return RestTemplate 객체 반환
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);

        return restTemplate;
    }
}
