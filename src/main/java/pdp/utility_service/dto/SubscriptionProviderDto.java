package pdp.utility_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionProviderDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}