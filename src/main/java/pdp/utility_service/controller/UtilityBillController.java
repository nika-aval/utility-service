package pdp.utility_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import pdp.utility_service.dto.BankAccountDto;
import pdp.utility_service.dto.PayableReportDto;
import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.service.UtilityBillPaymentService;
import pdp.utility_service.service.UtilityBillService;

import java.util.List;

@Tag(name = "Utility Bills", description = "APIs for managing utility bills")
@RestController
@RequestMapping("/utility-bill")
@RequiredArgsConstructor
public class UtilityBillController {

    private final UtilityBillService utilityBillService;
    private final UtilityBillPaymentService utilityBillPaymentService;

    @GetMapping("/all")
    @Cacheable(value = "utilityBills")
    @Operation(summary = "Get all utility bills", description = "Retrieves a list of all available utility bills")
    public List<UtilityBillDto> getAllUtilityBills() {
        return utilityBillService.getAllUtilityBills();
    }

    @GetMapping("/{customerId}")
    @Cacheable(value = "utilityBalance", key = "#customerId")
    @Operation(summary = "Check utility balance by customer ID", description = "Retrieves a payable report for a specified customer")
    public PayableReportDto checkUtilityBalanceByCustomerId(@PathVariable @Parameter(description = "customer ID") Long customerId) {
        return utilityBillService.checkUtilityBalanceByCustomerId(customerId);
    }

    @PutMapping("/pay-bills")
    @Operation(summary = "Pay utility bills", description = "Pays a list of utility bills for a specified customer")
    public BankAccountDto payUtilityBills(@Parameter(description = "customer ID") Long customerId,
                                          @Parameter(description = "IBAN of the bank account") String iban,
                                          @RequestBody @Parameter(description = "List of bill IDs to pay") List<Long> billIds) {
        return utilityBillPaymentService.payBills(customerId, iban, billIds);
    }

}
