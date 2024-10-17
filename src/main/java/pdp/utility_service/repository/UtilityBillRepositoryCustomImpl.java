package pdp.utility_service.repository;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import pdp.utility_service.dto.PayableReportDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.*;

public class UtilityBillRepositoryCustomImpl implements UtilityBillRepositoryCustom {

    @Autowired
    private DSLContext dslContext;

    @Override
    public PayableReportDto getPayableReport(Long customerId) {
        return mapToPayableReportDto(customerId, executeQuery(customerId));
    }

    private PayableReportDto mapToPayableReportDto(Long customerId, Result<Record3<String, Long[], BigDecimal>> result) {
        BigDecimal totalPayableAmount = BigDecimal.ZERO;
        Map<String, BigDecimal> payableAmounts = new HashMap<>();
        Map<String, Long[]> payableBills = new HashMap<>();

        for (Record3<String, Long[], BigDecimal> record : result) {
            payableAmounts.put(record.getValue("name", String.class), record.getValue("payable_amount", BigDecimal.class));
            payableBills.put(record.getValue("name", String.class), record.getValue("bill_ids", Long[].class));
            totalPayableAmount = totalPayableAmount.add(record.getValue("payable_amount", BigDecimal.class));
        }

        return PayableReportDto.builder()
                .customerId(customerId)
                .totalPayableAmount(totalPayableAmount)
                .payableAmounts(payableAmounts)
                .payableBillIds(payableBills)
                .build();
    }

    private Result<Record3<String, Long[], BigDecimal>> executeQuery(Long customerId) {
        return dslContext.
                select(field(name("subscription_provider", "name"), String.class),
                        arrayAgg(field(name("utility_bill", "id"), Long.class)).as(unquotedName("bill_ids")),
                        sum(field(unquotedName("amount"), BigDecimal.class)).as(unquotedName("payable_amount"))
                )
                .from(table(unquotedName("utility_bill"))
                        .join(table(unquotedName("subscription_provider")))
                        .on(field(name("utility_bill", "subscription_provider_id")).eq(field(name("subscription_provider", "id"))))
                )
                .where(
                        field(unquotedName("customer_id"), Long.class).eq(inline(customerId))
                                .and(field(unquotedName("is_paid"), Boolean.class).eq(inline(false)))
                )
                .groupBy(field(unquotedName("subscription_provider_id")), field(name("subscription_provider", "name")))
                .orderBy(field(unquotedName("payable_amount")).asc())
                .fetch();
    }

}
