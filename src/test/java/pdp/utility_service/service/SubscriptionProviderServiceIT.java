package pdp.utility_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.SubscriptionProviderDto;
import pdp.utility_service.model.Customer;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.repository.CustomerRepository;
import pdp.utility_service.repository.SubscriptionProviderRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pdp.utility_service.mapper.SubscriptionProviderMapper.toEntity;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ImportAutoConfiguration(JooqAutoConfiguration.class)
class SubscriptionProviderServiceIT {

    @Autowired
    private SubscriptionProviderRepository subscriptionProviderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private SubscriptionProviderService subscriptionProviderService;

    @BeforeEach
    @AfterEach
    void setUp() {
        subscriptionProviderService = new SubscriptionProviderService(subscriptionProviderRepository, customerRepository);
        subscriptionProviderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateSubscriptionProvider() {
        // GIVEN
        SubscriptionProviderDto expected = generateSubscriptionProviderDto();

        // WHEN
        SubscriptionProviderDto actual = subscriptionProviderService.createSubscriptionProvider(expected);

        //THEN
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void shouldFindAllSubscriptionProviders() {
        // GIVEN
        SubscriptionProviderDto expected = subscriptionProviderService.createSubscriptionProvider(generateSubscriptionProviderDto());

        // WHEN
        List<SubscriptionProviderDto> actual = subscriptionProviderService.findAllSubscriptionProviders();

        // THEN
        assertThat(actual).size().isEqualTo(1);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(expected));
    }

    @Test
    void shouldFindAllCustomersById() {
        // GIVEN
        Customer customer = customerRepository.saveAndFlush(generateCustomer());

        SubscriptionProvider subscriptionProvider = toEntity(generateSubscriptionProviderDto());
        subscriptionProvider.getCustomers().add(customer);
        Long subscriptionProviderId = subscriptionProviderRepository.saveAndFlush(subscriptionProvider).getId();

        // WHEN
        List<CustomerDto> customersBySubscriberId = subscriptionProviderService.findAllCustomersById(subscriptionProviderId);

        // THEN
        assertThat(customersBySubscriberId).size().isEqualTo(1);
        assertThat(customersBySubscriberId)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(customer));
    }

    private SubscriptionProviderDto generateSubscriptionProviderDto() {
        return new SubscriptionProviderDto(null, "Netflix",
                "Streaming service that offers a wide variety of TV shows, movies, and much more.",
                BigDecimal.valueOf(9.99));
    }

    private Customer generateCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Nika");
        customer.setLastName("Avalishvili");
        customer.setEmail("my.email@gmail.com");
        customer.setPhone("+995-222-333");
        customer.setSubscriptions(new HashSet<>());
        return customer;
    }

}
