package pdp.utility_service.mapper;

import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.model.UtilityBill;

import java.util.List;

public class UtilityBillMapper {

    public static UtilityBillDto toDto(UtilityBill utilityBill) {
        return new UtilityBillDto(utilityBill.getId(),
                        utilityBill.getDate(),
                        utilityBill.getAmount(),
                        utilityBill.isPaid(),
                        SubscriptionProviderMapper.toDto(utilityBill.getSubscriptionProvider()),
                        CustomerMapper.toCustomerDto(utilityBill.getCustomer()));
    }

    public static List<UtilityBillDto> toDtos(List<UtilityBill> utilityBills) {
        return utilityBills.stream().map(UtilityBillMapper::toDto).toList();
    }

}
