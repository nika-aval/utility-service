package pdp.utility_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class SubscriptionProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal accumulatedIncome = BigDecimal.ZERO;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "customer_subscription",
            joinColumns = @JoinColumn(name = "subscription_provider_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> customers = new HashSet<>();
}
