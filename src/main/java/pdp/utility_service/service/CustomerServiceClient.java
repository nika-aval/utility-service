package pdp.utility_service.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pdp.utility_service.dto.BankAccountDto;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CustomerServiceClient {

    private final RestClient restClient;

    public CustomerServiceClient() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<BankAccountDto> getBankAccountsByCustomerId(Long customerId) {
        return restClient.get()
                .uri("/bank/{customerId}", customerId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public BankAccountDto deductPaymentFromBalance(String iban, BigDecimal amount) {
        return restClient.put()
                .uri("/bank/withdraw?iban={iban}&amount={amount}", iban, amount)
                .retrieve()
                .body(BankAccountDto.class);
    }
}
