package pdp.utility_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventType;
    @OneToOne(cascade = CascadeType.DETACH)
    private UtilityBill payload;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean processed = false;
}
