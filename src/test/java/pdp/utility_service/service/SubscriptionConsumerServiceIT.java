package pdp.utility_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.SubscriptionDto;
import pdp.utility_service.dto.SubscriptionProviderDto;
import pdp.utility_service.model.Customer;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.repository.CustomerRepository;
import pdp.utility_service.repository.SubscriptionProviderRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(JooqAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SubscriptionConsumerServiceIT {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubscriptionProviderRepository subscriptionProviderRepository;
    @Autowired
    private SubscriptionProviderService subscriptionProviderService;
    @Autowired
    private SubscriptionConsumerService subscriptionConsumerService;

    private final ModelMapper mapper = new ModelMapper();

    @BeforeEach
    @AfterEach
    void setUp() {
        customerRepository.deleteAll();
        subscriptionProviderRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldSubscribeUtility() {
        // GIVEN
        SubscriptionProvider subscriptionProvider = mapper.map(generateSubscriptionProviderDto(), SubscriptionProvider.class);
        Long subscriptionProviderId = subscriptionProviderRepository.save(subscriptionProvider).getId();

        CustomerDto customerDto = new CustomerDto(1L, "Harry", "Potter", "hp@gmail.com", "+995555555");
        SubscriptionDto subscriptionDto = new SubscriptionDto(subscriptionProviderId, customerDto);

        // WHEN
        List<CustomerDto> customersBeforeSubscribing = subscriptionProviderService.findAllCustomersById(subscriptionProviderId);
        subscriptionConsumerService.subscribeUtility(new GenericMessage<>(subscriptionDto));
        List<CustomerDto> customersAfterSubscribing = subscriptionProviderService.findAllCustomersById(subscriptionProviderId);

        // THEN
        assertThat(customersBeforeSubscribing).size().isEqualTo(0);
        assertThat(customersAfterSubscribing).hasSize(1);
    }

    @Test
    @Transactional
    void shouldCancelSubscription() {
        // GIVEN
        Customer customer = customerRepository.saveAndFlush(generateCustomer(1L));
        SubscriptionProvider subscriptionProvider = mapper.map(generateSubscriptionProviderDto(), SubscriptionProvider.class);
        subscriptionProvider.getCustomers().add(customer);
        Long subscriptionProviderId = subscriptionProviderRepository.saveAndFlush(subscriptionProvider).getId();

        SubscriptionDto subscriptionDto = new SubscriptionDto(subscriptionProviderId, mapper.map(customer, CustomerDto.class));

        // WHEN
        List<CustomerDto> customersBeforeCancelling = subscriptionProviderService.findAllCustomersById(subscriptionProviderId);
        subscriptionConsumerService.cancelSubscription(new GenericMessage<>(subscriptionDto));
        List<CustomerDto> customersAfterCancelling = subscriptionProviderService.findAllCustomersById(subscriptionProviderId);

        // THEN
        assertThat(customersBeforeCancelling).size().isEqualTo(1);
        assertThat(customersAfterCancelling).hasSize(0);
    }

    private SubscriptionProviderDto generateSubscriptionProviderDto() {
        return new SubscriptionProviderDto(null, "Netflix",
                "Streaming service that offers a wide variety of TV shows, movies, and much more.",
                BigDecimal.valueOf(9.99));
    }

    private Customer generateCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setFirstName("Nika");
        customer.setLastName("Avalishvili");
        customer.setEmail("my.email@gmail.com");
        customer.setPhone("+995-222-333");
        customer.setSubscriptions(new HashSet<>());
        return customer;
    }

}