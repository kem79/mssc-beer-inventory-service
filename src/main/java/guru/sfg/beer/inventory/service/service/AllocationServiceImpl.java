package guru.sfg.beer.inventory.service.service;

import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by marecm on 5/7/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
        log.debug("Allocate orderId: " + beerOrderDto.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        for (BeerOrderLineDto beerOrderLine : beerOrderDto.getBeerOrderLines()) {
            if(((beerOrderLine.getOrderQuantity() != null ? beerOrderLine.getOrderQuantity() : 0)
            - (beerOrderLine.getQuantityAllocated() != null ? beerOrderLine.getQuantityAllocated(): 0)) > 0){
                allocateBeerOrderLine(beerOrderLine);
            }
            totalOrdered.set(totalOrdered.get() + beerOrderLine.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + (beerOrderLine.getQuantityAllocated() == null ? 0 : beerOrderLine.getQuantityAllocated()));
        }
        return totalOrdered.get() == totalAllocated.get();

    }

    private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {
        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(beerOrderLine.getUpc());

        beerInventoryList.forEach(beerInventory -> {
            int inventory = beerInventory.getQuantityOnHand() == null ? 0 : beerInventory.getQuantityOnHand();
            int orderQty = beerOrderLine.getOrderQuantity() == null ? 0: beerOrderLine.getOrderQuantity();
            int allocatedQty = beerOrderLine.getQuantityAllocated() == null ? 0: beerOrderLine.getQuantityAllocated();
            int qtyToAllocate = orderQty - allocatedQty;

            if(inventory >= qtyToAllocate){
                inventory = inventory - qtyToAllocate;
                beerOrderLine.setQuantityAllocated(orderQty);
                beerInventory.setQuantityOnHand(inventory);
            } else if (inventory > 0){ // partial inventory
                beerOrderLine.setQuantityAllocated(allocatedQty + inventory);
                beerInventory.setQuantityOnHand(0);

                beerInventoryRepository.delete(beerInventory);
            }
        });
    }
}
