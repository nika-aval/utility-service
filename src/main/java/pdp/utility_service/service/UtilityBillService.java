package pdp.utility_service.service;

import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.dto.PayableReportDto;
import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.model.Customer;
import pdp.utility_service.model.OutboxEvent;
import pdp.utility_service.model.SubscriptionProvider;
import pdp.utility_service.model.UtilityBill;
import pdp.utility_service.repository.CustomerRepositoryCustomImpl;
import pdp.utility_service.repository.OutboxEventRepository;
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
    private final OutboxEventRepository outboxEventRepository;
    private final ModelMapper mapper;

    public UtilityBillService(SubscriptionProviderRepository subscriptionProviderRepository,
                              CustomerRepositoryCustomImpl customerRepository,
                              UtilityBillRepository utilityBillRepository,
                              OutboxEventRepository outboxEventRepository) {
        this.subscriptionProviderRepository = subscriptionProviderRepository;
        this.customerRepository = customerRepository;
        this.utilityBillRepository = utilityBillRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.mapper = new ModelMapper();
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

    @SneakyThrows
    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")
    public void generateBills() {
        List<SubscriptionProvider> allProviders = subscriptionProviderRepository.findAll();

        allProviders.forEach(this::processSubscriptionProvider);
    }

    private void processSubscriptionProvider(SubscriptionProvider subscriptionProvider) {
        List<CustomerDto> allCustomerDtos = customerRepository.findAllCustomersBySubscriptionProviderId(subscriptionProvider.getId());
        List<UtilityBill> utilityBills = new ArrayList<>();

        for (CustomerDto customerDto : allCustomerDtos) {
            UtilityBill utilityBill = createUtilityBill(subscriptionProvider, customerDto);
            utilityBills.add(utilityBill);
            saveOutboxEvent(utilityBill);
        }

        utilityBillRepository.saveAll(utilityBills);
    }

    private UtilityBill createUtilityBill(SubscriptionProvider subscriptionProvider, CustomerDto customerDto) {
        UtilityBill utilityBill = new UtilityBill();
        utilityBill.setDate(LocalDateTime.now());
        utilityBill.setAmount(subscriptionProvider.getPrice());
        utilityBill.setSubscriptionProvider(subscriptionProvider);
        utilityBill.setCustomer(mapper.map(customerDto, Customer.class));
        return utilityBill;
    }

    private void saveOutboxEvent(UtilityBill utilityBill) {
        OutboxEvent outbox = new OutboxEvent();
        outbox.setEventType("UtilityBillCreated");
        outbox.setPayload(utilityBill);
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setProcessed(false);

        outboxEventRepository.save(outbox);
    }
}
