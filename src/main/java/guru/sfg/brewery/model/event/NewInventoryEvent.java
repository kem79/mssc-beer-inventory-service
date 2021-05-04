package guru.sfg.brewery.model.event;

import guru.sfg.brewery.model.BeerDto;
import lombok.NoArgsConstructor;

/**
 * Created by marecm on 4/27/2021
 */
@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
