package guru.sfg.beer.inventory.service.service;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.event.AllocateOrderRequestEvent;
import guru.sfg.brewery.model.event.AllocateOrderResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by marecm on 5/8/2021
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AllocationListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequestEvent event){
        AllocateOrderResultEvent.AllocateOrderResultEventBuilder builder = AllocateOrderResultEvent.builder();
        builder.beerOrderDto(event.getBeerOrderDto());

        try{
            Boolean allocationResult = allocationService.allocateOrder(event.getBeerOrderDto());

            if(allocationResult == Boolean.TRUE){
                builder.pendingInventory(Boolean.FALSE);
            } else {
                builder.pendingInventory(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.error("Allocation failed for OrderId: " + event.getBeerOrderDto().getId());
            builder.allocationError(Boolean.TRUE);
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE,
                builder.build());
    }
}
