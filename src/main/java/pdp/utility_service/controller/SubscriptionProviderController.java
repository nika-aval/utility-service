package pdp.utility_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pdp.utility_service.dto.CustomerDto;
import pdp.utility_service.service.SubscriptionProviderService;
import pdp.utility_service.dto.SubscriptionProviderDto;

import java.util.List;

@Tag(name = "Subscription Providers", description = "APIs for managing subscription providers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionProviderController {

    private final SubscriptionProviderService subscriptionProviderService;

    @PostMapping
    @Operation(summary = "Create a new subscription provider", description = "Creates a new subscription provider with the provided details")
    public SubscriptionProviderDto createSubscriptionProvider(@RequestBody @Parameter(description = "Subscription provider details") SubscriptionProviderDto subscriptionProviderDto) {
        return subscriptionProviderService.createSubscriptionProvider(subscriptionProviderDto);
    }

    @GetMapping
    @Operation(summary = "Get all subscription providers", description = "Retrieves a list of all available subscription providers")
    public List<SubscriptionProviderDto> getAllSubscriptionProviders() {
        return subscriptionProviderService.findAllSubscriptionProviders();
    }

    @GetMapping("/{subscriptionProviderId}")
    @Operation(summary = "Get all customers by subscription provider ID", description = "Retrieves a list of customers associated with a specified subscription provider")
    public List<CustomerDto> getAllByCustomerId(@PathVariable @Parameter(description = "Subscription Provider ID") Long subscriptionProviderId) {
        return subscriptionProviderService.findAllCustomersById(subscriptionProviderId);
    }

}
