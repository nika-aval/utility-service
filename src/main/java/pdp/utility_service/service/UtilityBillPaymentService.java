package pdp.utility_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.dto.BankAccountDto;
import pdp.utility_service.model.UtilityBill;
import pdp.utility_service.repository.UtilityBillRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilityBillPaymentService {

    private final CustomerServiceClient customerServiceClient;
    private final UtilityBillRepository utilityBillRepository;

    @Transactional
    public BankAccountDto payBills(Long customerId, String iban, List<Long> billIds) {
        List<BankAccountDto> bankAccounts = customerServiceClient.getBankAccountsByCustomerId(customerId);

        BigDecimal totalPayableAmount = BigDecimal.ZERO;
        List<UtilityBill> allById = utilityBillRepository.findAllById(billIds);

        for (UtilityBill utilityBill : allById) {
            totalPayableAmount = totalPayableAmount.add(utilityBill.getAmount());
        }

        BankAccountDto account = bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getIban().equals(iban))
                .findFirst().orElseThrow(() -> new RuntimeException("There are no valid IBAN code provided"));

        if (totalPayableAmount.compareTo(account.getBalance()) > 0) {
            throw new RuntimeException("The account balance is not enough to cover all indicated bills");
        }

        allById.forEach(utilityBill -> utilityBill.setPaid(true));
        return customerServiceClient.deductPaymentFromBalance(iban, totalPayableAmount);
    }

}
