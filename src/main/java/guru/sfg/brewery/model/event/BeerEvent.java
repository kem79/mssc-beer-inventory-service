package guru.sfg.brewery.model.event;

import guru.sfg.brewery.model.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by marecm on 4/27/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = 4129804180705537841L;

    private BeerDto beerDto;
}
