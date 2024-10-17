package pdp.utility_service.dto;

import java.io.Serializable;

public record CustomerDto(Long id, String firstName, String lastName, String email, String phone) implements Serializable {
}
