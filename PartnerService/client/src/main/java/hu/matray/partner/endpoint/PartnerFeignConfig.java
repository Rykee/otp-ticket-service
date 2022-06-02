package hu.matray.partner.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import hu.matray.partner.endpoint.decoder.PartnerErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

public class PartnerFeignConfig {

    public static final String CLIENT_NAME = "partnerFeignClient";

    @Bean
    protected BasicAuthFeignClientConfig getBasicAuthConfig(@Value("${application.partnerFeignClient.username}") String username,
                                                            @Value("${application.partnerFeignClient.password}") String password) {
        return new BasicAuthFeignClientConfig(username, password);
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(Optional<BasicAuthFeignClientConfig> basicAuthFeignClientConfig) {
        return basicAuthFeignClientConfig
                .map(conf -> new BasicAuthRequestInterceptor(conf.getUserName(), conf.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Missing basic auth configuration for feign client: " + CLIENT_NAME));
    }

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new PartnerErrorDecoder(objectMapper);
    }

    @lombok.Value
    protected static class BasicAuthFeignClientConfig {
        String userName;
        String password;
    }

}
