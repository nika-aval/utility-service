package pdp.utility_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pdp.utility_service.config.KafkaTopicConfig;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.PayableReportDto;
import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.model.Customer;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.model.UtilityBill;
import pdp.utility_service.repository.CustomerRepositoryCustomImpl;
import pdp.utility_service.repository.SubscriptionProviderRepository;
import pdp.utility_service.repository.UtilityBillRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class UtilityBillService {

    private final SubscriptionProviderRepository subscriptionProviderRepository;
    private final CustomerRepositoryCustomImpl customerRepository;
    private final UtilityBillRepository utilityBillRepository;
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ModelMapper mapper;

    public UtilityBillService(SubscriptionProviderRepository subscriptionProviderRepository,
                              CustomerRepositoryCustomImpl customerRepository,
                              UtilityBillRepository utilityBillRepository,
                              KafkaTemplate<Object, Object> kafkaTemplate) {
        this.subscriptionProviderRepository = subscriptionProviderRepository;
        this.customerRepository = customerRepository;
        this.utilityBillRepository = utilityBillRepository;
        this.kafkaTemplate = kafkaTemplate;
        mapper = new ModelMapper();
    }

    public List<UtilityBillDto> getAllUtilityBills() {
        List<UtilityBill> utilityBills = utilityBillRepository.findAll();
        return utilityBills.stream()
                .map(utilityBill -> mapper.map(utilityBill, UtilityBillDto.class))
                .toList();
    }

    public PayableReportDto checkUtilityBalanceByCustomerId(Long customerId) {
        return utilityBillRepository.getPayableReport(customerId);
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void generateBills() {
        List<SubscriptionProvider> allProviders = subscriptionProviderRepository.findAll();

        allProviders.forEach(subscriptionProvider -> {
            List<CustomerDto> allCustomerDtos = customerRepository.findAllCustomersBySubscriptionProviderId(subscriptionProvider.getId());
            List<UtilityBill> utilityBills = new ArrayList<>();

            allCustomerDtos.forEach(customerDto -> {
                UtilityBill utilityBill = new UtilityBill();
                utilityBill.setDate(LocalDateTime.now());
                utilityBill.setAmount(subscriptionProvider.getPrice());
                utilityBill.setSubscriptionProvider(subscriptionProvider);
                utilityBill.setCustomer(mapper.map(customerDto, Customer.class));

                utilityBills.add(utilityBill);

                UtilityBillDto utilityBillDto = mapper.map(utilityBill, UtilityBillDto.class);
                kafkaTemplate.send(KafkaTopicConfig.CUSTOMER_NOTIFICATION, utilityBillDto);
            });

            utilityBillRepository.saveAll(utilityBills);
        });
    }
}
