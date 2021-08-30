package it.polimi.ingsw.shared;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.shared.LeaderCardsGetter;
import org.junit.jupiter.api.Test;

public class LeaderCardsGetterTest {


    @Test
    public void test() {

        LeaderCardsGetter get = new LeaderCardsGetter();

        LeaderCard[] array = get.getCards();
        System.out.println("--------------id---------------" );
        for(int i = 0; i< array.length; i++){
            System.out.println(array[i].getID());
        }
    }

}
