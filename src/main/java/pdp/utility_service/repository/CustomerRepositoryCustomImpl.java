package pdp.utility_service.repository;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import pdp.utility_service.dto.CustomerDto;

import java.util.List;

import static org.jooq.impl.DSL.*;

public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    @Autowired
    private DSLContext dslContext;

    @Override
    public List<CustomerDto> findAllCustomersBySubscriptionProviderId(Long subscriptionProviderId) {
        return dslContext.select(
                        field(unquotedName("id"), Long.class),
                        field(unquotedName("first_name"), String.class),
                        field(unquotedName("last_name"), String.class),
                        field(unquotedName("email"), String.class),
                        field(unquotedName("phone"), String.class)
                )
                .from(table(unquotedName("customer"))
                        .join(table(unquotedName("customer_subscription")))
                        .on(field(unquotedName("customer_subscription", "customer_id")).eq(field(unquotedName("customer", "id"))))
                )
                .where(field(unquotedName("customer_subscription", "subscription_provider_id")).eq(inline(subscriptionProviderId)))
                .fetch(Records.mapping(CustomerDto::new));
    }

    @Override
    public void cancelSubscription(Long subscriptionProviderId, Long customerId) {
        dslContext.deleteFrom(table(unquotedName("customer_subscription")))
                .where(field(unquotedName("customer_subscription", "customer_id")).eq(inline(customerId)))
                .and(field(unquotedName("customer_subscription", "subscription_provider_id")).eq(inline(subscriptionProviderId)))
                .execute();
    }

    @Override
    public void cancelAllSubscriptionsForCustomer(Long customerId) {
        dslContext.deleteFrom(table(unquotedName("customer_subscription")))
                .where(field(unquotedName("customer_subscription", "customer_id")).eq(inline(customerId)))
                .execute();
    }

}
