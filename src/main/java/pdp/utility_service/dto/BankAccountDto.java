package pdp.utility_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {
    private Long id;
    private String iban;
    private BigDecimal balance;
}
