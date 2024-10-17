package pdp.utility_service.repository;

import pdp.utility_service.dto.PayableReportDto;

public interface UtilityBillRepositoryCustom {

    PayableReportDto getPayableReport(Long customerId);

}
