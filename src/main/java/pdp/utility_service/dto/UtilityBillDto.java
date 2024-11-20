package pdp.utility_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilityBillDto implements Serializable {
    private Long id;
    private LocalDateTime date;
    private BigDecimal amount;
    private boolean isPaid;
    private SubscriptionProviderDto subscriptionProvider;
    private CustomerDto customer;
}
