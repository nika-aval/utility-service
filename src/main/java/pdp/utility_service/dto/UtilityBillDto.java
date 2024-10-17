package pdp.utility_service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UtilityBillDto(Long Id, LocalDateTime date, BigDecimal Amount, boolean isPaid,
                             SubscriptionProviderDto subscriptionProvider, CustomerDto customer) implements Serializable {
}
