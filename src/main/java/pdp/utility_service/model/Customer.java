package pdp.utility_service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Customer {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @ManyToMany(mappedBy = "customers", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<SubscriptionProvider> subscriptions = new HashSet<>();
}
