package pdp.utility_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pdp.utility_service.dto.BankAccountDto;
import pdp.utility_service.dto.PayableReportDto;
import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.service.UtilityBillPaymentService;
import pdp.utility_service.service.UtilityBillService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Tag(name = "Utility Bills", description = "APIs for managing utility bills")
@RestController
@RequestMapping("/utility-bill")
@RequiredArgsConstructor
public class UtilityBillController {

    private final UtilityBillService utilityBillService;
    private final UtilityBillPaymentService utilityBillPaymentService;

    @GetMapping
    @Operation(summary = "Get all utility bills", description = "Retrieves a list of all available utility bills")
    public ResponseEntity<List<UtilityBillDto>> getAllUtilityBills() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(6, TimeUnit.MINUTES))
                .body(utilityBillService.getAllUtilityBills());
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Check utility balance by customer ID", description = "Retrieves a payable report for a specified customer")
    public ResponseEntity<PayableReportDto> checkUtilityBalanceByCustomerId(@PathVariable @Parameter(description = "customer ID") Long customerId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(6, TimeUnit.MINUTES))
                .body(utilityBillService.checkUtilityBalanceByCustomerId(customerId));
    }

    @PutMapping
    @Operation(summary = "Pay utility bills", description = "Pays a list of utility bills for a specified customer")
    public BankAccountDto payUtilityBills(@Parameter(description = "customer ID") Long customerId,
                                                          @Parameter(description = "IBAN of the bank account") String iban,
                                                          @RequestBody @Parameter(description = "List of bill IDs to pay") List<Long> billIds) {
        return utilityBillPaymentService.payBills(customerId, iban, billIds);
    }

}
