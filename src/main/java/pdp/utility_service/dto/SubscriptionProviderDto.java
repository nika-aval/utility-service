package pdp.utility_service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record SubscriptionProviderDto(Long id, String name, String description, BigDecimal price) implements Serializable {
}