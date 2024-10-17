package pdp.utility_service.mapper;

import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.model.Customer;

public class CustomerMapper {

    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone());
    }

    public static Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setId(dto.id());
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        return customer;
    }

}
