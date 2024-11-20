package pdp.utility_service.repository;

import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.beans.factory.annotation.Autowired;
import pdp.utility_service.dto.CustomerDto;

import java.util.List;

import static org.jooq.impl.DSL.inline;
import static pdp.utility_service.model.Tables.CUSTOMER;
import static pdp.utility_service.model.Tables.CUSTOMER_SUBSCRIPTION;

public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    @Autowired
    private DSLContext dslContext;

    @Override
    public List<CustomerDto> findAllCustomersBySubscriptionProviderId(Long subscriptionProviderId) {
        return dslContext.select(
                        CUSTOMER.ID,
                        CUSTOMER.FIRST_NAME,
                        CUSTOMER.LAST_NAME,
                        CUSTOMER.EMAIL,
                        CUSTOMER.PHONE)
                .from(CUSTOMER)
                .join(CUSTOMER_SUBSCRIPTION).on(CUSTOMER_SUBSCRIPTION.CUSTOMER_ID.eq(CUSTOMER.ID))
                .where(CUSTOMER_SUBSCRIPTION.SUBSCRIPTION_PROVIDER_ID.eq(inline(subscriptionProviderId)))
                .fetch(Records.mapping(CustomerDto::new));
    }

    @Override
    public void cancelSubscription(Long subscriptionProviderId, Long customerId) {
        dslContext.deleteFrom(CUSTOMER_SUBSCRIPTION)
                .where(CUSTOMER_SUBSCRIPTION.CUSTOMER_ID.eq(inline(customerId)))
                .and(CUSTOMER_SUBSCRIPTION.SUBSCRIPTION_PROVIDER_ID.eq(inline(subscriptionProviderId)))
                .execute();
    }

    @Override
    public void cancelAllSubscriptionsForCustomer(Long customerId) {
        dslContext.deleteFrom(CUSTOMER_SUBSCRIPTION)
                .where(CUSTOMER_SUBSCRIPTION.CUSTOMER_ID.eq(inline(customerId)))
                .execute();
    }

}
