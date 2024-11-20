package pdp.utility_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.utility_service.model.UtilityBill;

import java.util.List;

public interface UtilityBillRepository extends JpaRepository<UtilityBill, Long>, UtilityBillRepositoryCustom {
    List<UtilityBill> findAllByIdInAndIsPaid(List<Long> utilityBillIds, boolean isPaid);
}
