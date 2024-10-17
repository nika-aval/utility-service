package pdp.utility_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pdp.utility_service.model.Customer;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class BankAccount {
    private Long id;
    private String iban;
    private BigDecimal balance = BigDecimal.ZERO;
    private boolean isActive = true;
    private Customer customer;
}
