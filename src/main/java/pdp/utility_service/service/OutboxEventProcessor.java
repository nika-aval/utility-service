package pdp.utility_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.utility_service.config.KafkaTopicConfig;
import pdp.utility_service.dto.UtilityBillDto;
import pdp.utility_service.model.OutboxEvent;
import pdp.utility_service.repository.OutboxEventRepository;

import java.util.List;

@Service
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxRepository;
    private final ModelMapper mapper;
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public OutboxEventProcessor(OutboxEventRepository outboxRepository, KafkaTemplate<Object, Object> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.mapper = new ModelMapper();
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxMessages() {
        List<OutboxEvent> outboxes = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc();

        for (OutboxEvent outbox : outboxes) {
            kafkaTemplate.send(KafkaTopicConfig.CUSTOMER_NOTIFICATION, mapper.map(outbox.getPayload(), UtilityBillDto.class));
            outbox.setProcessed(true);
        }
    }

}
