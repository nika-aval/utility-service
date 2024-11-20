package pdp.utility_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.utility_service.model.OutboxEvent;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByProcessedFalseOrderByCreatedAtAsc();
}
