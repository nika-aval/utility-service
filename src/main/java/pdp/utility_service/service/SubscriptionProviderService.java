package pdp.utility_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.SubscriptionProviderDto;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.repository.CustomerRepository;
import pdp.utility_service.repository.SubscriptionProviderRepository;

import java.util.List;

@Service
public class SubscriptionProviderService {

    private final SubscriptionProviderRepository subscriptionProviderRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    public SubscriptionProviderService(SubscriptionProviderRepository subscriptionProviderRepository,
                                       CustomerRepository customerRepository) {
        this.subscriptionProviderRepository = subscriptionProviderRepository;
        this.customerRepository = customerRepository;
        mapper = new ModelMapper();
    }

    public SubscriptionProviderDto createSubscriptionProvider(SubscriptionProviderDto dto) {
        SubscriptionProvider subscriptionProvider = mapper.map(dto, SubscriptionProvider.class);
        return mapper.map(subscriptionProviderRepository.save(subscriptionProvider), SubscriptionProviderDto.class);
    }

    public List<SubscriptionProviderDto> findAllSubscriptionProviders() {
        return subscriptionProviderRepository.findAll().stream()
                .map(subscriptionProvider -> mapper.map(subscriptionProvider, SubscriptionProviderDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAllCustomersById(Long subscriptionProviderId) {
        return customerRepository.findAllCustomersBySubscriptionProviderId(subscriptionProviderId);
    }

}
