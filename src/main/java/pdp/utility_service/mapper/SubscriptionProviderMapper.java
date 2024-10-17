package pdp.utility_service.mapper;

import pdp.utility_service.dto.SubscriptionProviderDto;
import pdp.utility_service.model.SubscriptionProvider;

import java.util.List;

public class SubscriptionProviderMapper {

    public static SubscriptionProvider toEntity(SubscriptionProviderDto dto) {
        SubscriptionProvider entity = new SubscriptionProvider();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        return entity;
    }
    public static SubscriptionProviderDto toDto(SubscriptionProvider entity) {
        return new SubscriptionProviderDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice());
    }

    public static List<SubscriptionProviderDto> toDtos(List<SubscriptionProvider> entities) {
        return entities.stream()
                .map(SubscriptionProviderMapper::toDto)
                .toList();
    }

}
