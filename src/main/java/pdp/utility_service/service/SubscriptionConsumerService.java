package pdp.utility_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.dto.SubscriptionDto;
import pdp.utility_service.model.Customer;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.repository.CustomerRepository;
import pdp.utility_service.repository.CustomerRepositoryCustomImpl;
import pdp.utility_service.repository.SubscriptionProviderRepository;

import static pdp.utility_service.mapper.CustomerMapper.toEntity;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionConsumerService {

    public static final String SUBSCRIBE_UTILITY = "subscribe_utility";
    public static final String SUBSCRIBE_UTILITY_GROUP = "subscribe_utility_group";

    public static final String CANCEL_SUBSCRIPTION = "cancel_subscription";
    public static final String CANCEL_SUBSCRIPTION_GROUP = "cancel_subscription_group";

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustomImpl customerRepositoryCustomImpl;
    private final SubscriptionProviderRepository subscriptionProviderRepository;

    @KafkaListener(topics = SUBSCRIBE_UTILITY, groupId = SUBSCRIBE_UTILITY_GROUP)
    public void subscribeUtility(Message<SubscriptionDto> subscriptionDto) {
        Long subscriptionProviderId = subscriptionDto.getPayload().subscriptionProviderId();
        Customer customer = toEntity(subscriptionDto.getPayload().customerDto());

        SubscriptionProvider subscriptionProvider = subscriptionProviderRepository.findById(subscriptionProviderId)
                .orElseThrow(() -> new RuntimeException("Subscription provider with id " + subscriptionProviderId + " not found"));

        if (customerRepository.findById(customer.getId()).isEmpty()){
            customerRepository.save(customer);
        }

        subscriptionProvider.getCustomers().add(customer);
        subscriptionProviderRepository.saveAndFlush(subscriptionProvider);
    }

    @KafkaListener(topics = CANCEL_SUBSCRIPTION, groupId = CANCEL_SUBSCRIPTION_GROUP)
    public void cancelSubscription(Message<SubscriptionDto> subscriptionDto) {
        Long subscriptionProviderId = subscriptionDto.getPayload().subscriptionProviderId();
        Long customerId = subscriptionDto.getPayload().customerDto().id();

        customerRepositoryCustomImpl.cancelSubscription(subscriptionProviderId, customerId);
    }

}
