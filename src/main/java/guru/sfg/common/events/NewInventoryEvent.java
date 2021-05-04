package guru.sfg.common.events;

/**
 * Created by marecm on 4/27/2021
 */
public class NewInventoryEvent extends BeerEvent {
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
