package guru.sfg.beer.inventory.service.service;

import guru.sfg.brewery.model.BeerOrderDto;

/**
 * Created by marecm on 5/7/2021
 */
public interface AllocationService {

    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
