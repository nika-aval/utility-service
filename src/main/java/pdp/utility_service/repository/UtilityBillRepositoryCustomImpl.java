package pdp.utility_service.repository;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import pdp.utility_service.dto.PayableReportDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.*;
import static pdp.utility_service.model.Tables.SUBSCRIPTION_PROVIDER;
import static pdp.utility_service.model.Tables.UTILITY_BILL;

public class UtilityBillRepositoryCustomImpl implements UtilityBillRepositoryCustom {

    public static String PAYABLE_AMOUNT = "payable_amount";
    public static String BILL_IDS = "bill_ids";

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
            payableAmounts.put(record.get(SUBSCRIPTION_PROVIDER.NAME), record.getValue(PAYABLE_AMOUNT, BigDecimal.class));
            payableBills.put(record.get(SUBSCRIPTION_PROVIDER.NAME), record.getValue(BILL_IDS, Long[].class));
            totalPayableAmount = totalPayableAmount.add(record.getValue(PAYABLE_AMOUNT, BigDecimal.class));
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
                select(SUBSCRIPTION_PROVIDER.NAME,
                        arrayAgg(UTILITY_BILL.ID).as(unquotedName(BILL_IDS)),
                        sum(UTILITY_BILL.AMOUNT).as(unquotedName(PAYABLE_AMOUNT))
                )
                .from(UTILITY_BILL)
                .join(SUBSCRIPTION_PROVIDER)
                        .on(UTILITY_BILL.SUBSCRIPTION_PROVIDER_ID.eq(SUBSCRIPTION_PROVIDER.ID))
                .where(UTILITY_BILL.CUSTOMER_ID.eq(inline(customerId))
                        .and(UTILITY_BILL.IS_PAID.isFalse()))
                .groupBy(SUBSCRIPTION_PROVIDER.ID, SUBSCRIPTION_PROVIDER.NAME)
                .orderBy(field(unquotedName(PAYABLE_AMOUNT)).asc())
                .fetch();
    }

}
