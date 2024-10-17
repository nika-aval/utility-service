package pdp.utility_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String CUSTOMER_NOTIFICATION = "customer_notification";

    @Bean
    public NewTopic customerNotificationTopic() {
        return new NewTopic(CUSTOMER_NOTIFICATION, 1, (short) 1);
    }

}
