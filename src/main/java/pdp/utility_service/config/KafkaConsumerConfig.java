package pdp.utility_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    JsonMessageConverter jsonMessageConverter() {
        return new JsonMessageConverter();
    }

}