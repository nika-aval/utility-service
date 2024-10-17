package pdp.utility_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.utility_service.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
}
