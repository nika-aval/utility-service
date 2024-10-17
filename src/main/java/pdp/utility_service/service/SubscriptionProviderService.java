package pdp.utility_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.SubscriptionProviderDto;
import pdp.utility_service.mapper.SubscriptionProviderMapper;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.repository.CustomerRepository;
import pdp.utility_service.repository.SubscriptionProviderRepository;

import java.util.List;

import static pdp.utility_service.mapper.SubscriptionProviderMapper.toDto;
import static pdp.utility_service.mapper.SubscriptionProviderMapper.toDtos;

@Service
@RequiredArgsConstructor
public class SubscriptionProviderService {

    private final SubscriptionProviderRepository subscriptionProviderRepository;
    private final CustomerRepository customerRepository;

    public SubscriptionProviderDto createSubscriptionProvider(SubscriptionProviderDto dto) {
        SubscriptionProvider subscriptionProvider = SubscriptionProviderMapper.toEntity(dto);
        return toDto(subscriptionProviderRepository.save(subscriptionProvider));
    }

    public List<SubscriptionProviderDto> findAllSubscriptionProviders() {
        return toDtos(subscriptionProviderRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAllCustomersById(Long subscriptionProviderId) {
        return customerRepository.findAllCustomersBySubscriptionProviderId(subscriptionProviderId);
    }

}
