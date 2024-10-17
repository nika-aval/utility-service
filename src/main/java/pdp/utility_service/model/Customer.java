package pdp.utility_service.model;

import jakarta.persistence.*;
import lombok.Data;
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
