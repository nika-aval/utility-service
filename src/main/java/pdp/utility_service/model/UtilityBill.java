package pdp.utility_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UtilityBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date = LocalDateTime.now();
    private BigDecimal amount;
    private boolean isPaid = false;
    @ManyToOne
    @JoinColumn
    private SubscriptionProvider subscriptionProvider;
    @ManyToOne
    @JoinColumn
    private Customer customer;
}