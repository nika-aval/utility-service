package pdp.utility_service.dto;

import java.math.BigDecimal;

public record BankAccountDto(Long id, String iban, BigDecimal balance) {
}
