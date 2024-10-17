package pdp.utility_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pdp.utility_service.config.KafkaTopicConfig;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.PayableReportDto;
import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.mapper.CustomerMapper;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.model.UtilityBill;
import pdp.utility_service.repository.CustomerRepositoryCustomImpl;
import pdp.utility_service.repository.SubscriptionProviderRepository;
import pdp.utility_service.repository.UtilityBillRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pdp.utility_service.mapper.UtilityBillMapper.toDto;
import static pdp.utility_service.mapper.UtilityBillMapper.toDtos;

@Service
@RequiredArgsConstructor
public class UtilityBillService {

    private final SubscriptionProviderRepository subscriptionProviderRepository;
    private final CustomerRepositoryCustomImpl customerRepository;
    private final UtilityBillRepository utilityBillRepository;
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public List<UtilityBillDto> getAllUtilityBills() {
        List<UtilityBill> utilityBills = utilityBillRepository.findAll();
        return toDtos(utilityBills);
    }

    public PayableReportDto checkUtilityBalanceByCustomerId(Long customerId) {
        return utilityBillRepository.getPayableReport(customerId);
    }

    @Scheduled(cron = "0 0/5 * * * *")
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
                utilityBill.setCustomer(CustomerMapper.toEntity(customerDto));

                utilityBills.add(utilityBill);
                sendMessage(KafkaTopicConfig.CUSTOMER_NOTIFICATION, toDto(utilityBill));
            });

            utilityBillRepository.saveAll(utilityBills);
        });
    }

    private void sendMessage(String topic, UtilityBillDto utilityBillDto) {
        kafkaTemplate.send(topic, utilityBillDto);
    }

}
