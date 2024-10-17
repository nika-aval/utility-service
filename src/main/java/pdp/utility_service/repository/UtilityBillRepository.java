package pdp.utility_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.utility_service.model.UtilityBill;

public interface UtilityBillRepository extends JpaRepository<UtilityBill, Long>, UtilityBillRepositoryCustom {
}
