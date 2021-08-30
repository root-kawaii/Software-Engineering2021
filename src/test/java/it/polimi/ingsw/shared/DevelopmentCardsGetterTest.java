package it.polimi.ingsw.shared;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.shared.DevelopmentCardsGetter;
import org.junit.jupiter.api.Test;

public class DevelopmentCardsGetterTest {

    @Test
    public void Test(){
        DevelopmentCardsGetter get = new DevelopmentCardsGetter();
        DevelopmentCard[] array;
        array = get.getCards();
        for(int i = 0; i < array.length; i++){
            System.out.println("vPoints :   "+array[i].getVictoryPoints());
        }
    }
}
