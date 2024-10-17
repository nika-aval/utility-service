package pdp.utility_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.utility_service.model.SubscriptionProvider;

public interface SubscriptionProviderRepository extends JpaRepository<SubscriptionProvider, Long> {
}