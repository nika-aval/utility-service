package pdp.utility_service.repository;

import pdp.utility_service.dto.CustomerDto;

import java.util.List;

public interface CustomerRepositoryCustom {

    List<CustomerDto> findAllCustomersBySubscriptionProviderId(Long subscriptionProviderId);

    void cancelSubscription(Long subscriptionProviderId, Long customerId);

    void cancelAllSubscriptionsForCustomer(Long customerId);
}
