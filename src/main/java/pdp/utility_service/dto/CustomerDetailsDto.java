package pdp.utility_service.dto;

import java.util.Set;

public record CustomerDetailsDto(Long id, String firstName, String lastName, String email, String phone, Set<BankAccount> bankAccounts) {
}
