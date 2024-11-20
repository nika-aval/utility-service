package pdp.utility_service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class PayableReportDto implements Serializable {
    private Long customerId;
    private BigDecimal totalPayableAmount;
    private Map<String, BigDecimal> payableAmounts;
    private Map<String, Long[]> payableBillIds;
}
