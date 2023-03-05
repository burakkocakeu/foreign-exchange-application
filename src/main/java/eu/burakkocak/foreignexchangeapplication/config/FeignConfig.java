package eu.burakkocak.foreignexchangeapplication.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "eu.burakkocak.foreignexchangeapplication.client")
@Configuration
public class FeignConfig {
}
