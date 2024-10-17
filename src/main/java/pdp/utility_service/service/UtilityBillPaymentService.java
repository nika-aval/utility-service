package pdp.utility_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pdp.utility_service.dto.BankAccountDto;
import pdp.utility_service.model.UtilityBill;
import pdp.utility_service.repository.UtilityBillRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilityBillPaymentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UtilityBillRepository utilityBillRepository;

    @Transactional
    public BankAccountDto payBills(Long customerId, String iban, List<Long> billIds) {
        List<BankAccountDto> bankAccounts = getBankAccountsByCustomerId(customerId);

        BigDecimal totalPayableAmount = BigDecimal.ZERO;
        List<UtilityBill> allById = utilityBillRepository.findAllById(billIds);

        for (UtilityBill utilityBill : allById) {
            totalPayableAmount = totalPayableAmount.add(utilityBill.getAmount());
        }

        BankAccountDto account = bankAccounts.stream()
                .filter(bankAccount -> bankAccount.iban().equals(iban))
                .findFirst().orElseThrow(() -> new RuntimeException("There are no valid IBAN code provided"));

        if (totalPayableAmount.compareTo(account.balance()) > 0) {
            throw new RuntimeException("The account balance is not enough to cover all indicated bills");
        }

        allById.forEach(utilityBill -> utilityBill.setPaid(true));
        return deductPaymentFromBalance(iban, totalPayableAmount);
    }

    private List<BankAccountDto> getBankAccountsByCustomerId(Long customerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<List<BankAccountDto>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        String url = "http://localhost:8080/bank/get-all-bank-accounts/" + customerId;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                typeRef).getBody();
    }

    private BankAccountDto deductPaymentFromBalance(String iban, BigDecimal payment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<BankAccountDto> typeRef =
                new ParameterizedTypeReference<>() {
                };

        String baseUrl = "http://localhost:8080/bank/withdraw";

        UriComponents url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("iban", iban)
                .queryParam("amount", payment)
                .build();


        return restTemplate.exchange(
                url.toUriString(),
                HttpMethod.PUT,
                entity,
                typeRef).getBody();
    }
}
