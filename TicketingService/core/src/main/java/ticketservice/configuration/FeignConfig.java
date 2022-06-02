package ticketservice.configuration;

import hu.matray.partner.endpoint.PartnerFeignEndpoint;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {PartnerFeignEndpoint.class})
public class FeignConfig {
}
