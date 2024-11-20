package pdp.utility_service.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}