package it.polimi.ingsw.shared;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.CardColour;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Production;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.Card;
import it.polimi.ingsw.view.CardResources;


import java.io.*;
import java.util.*;

public class DevelopmentCardsGetter {
    JsonElement element;
    DevelopmentCard[] cardArray = new DevelopmentCard[48];
    int i = 0;

    public DevelopmentCard[] getCards() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("developmentCards.json");
        InputStreamReader reader = new InputStreamReader(inputStream);
        element = JsonParser.parseReader(reader);
        JsonObject object = element.getAsJsonObject();
        addJsonObject(object);
        return cardArray;
    }

        public void addJsonObject (JsonObject jsonObj){
            jsonObj.keySet().forEach(keyStr ->
            {
                JsonElement keyvalue = jsonObj.get(keyStr);
                JsonObject object = keyvalue.getAsJsonObject();

                //for nested objects iteration
                if (keyvalue instanceof JsonObject) {
                    Integer ID = object.get("ID").getAsInt();
                    Integer vPoint = object.get("victoryPoints").getAsInt();
                    String colour = object.get("colour").getAsString();
                    Integer level = object.get("level").getAsInt();

                    JsonArray tempCost = object.get("cost").getAsJsonArray();
                    EnumMap<Resource,Integer> cost = new EnumMap<>(Resource.class);
                    addInMap(tempCost,cost);

                    JsonArray tempReq = object.get("req").getAsJsonArray();
                    EnumMap<Resource,Integer> req = new EnumMap<>(Resource.class);
                    addInMap(tempReq,req);

                    JsonArray tempProd = object.get("prod").getAsJsonArray();
                    EnumMap<Resource,Integer> prod = new EnumMap<>(Resource.class);
                    addInMap(tempProd,prod);

                    Production production = new Production(req,prod);
                    DevelopmentCard developmentCard =
                                        new DevelopmentCard(level, CardColour.valueOf(colour),vPoint,cost,production,ID);

                    cardArray[i] = developmentCard;
                    i++;

                }
            });

        }

        public Card[] getViewCards() {
            DevelopmentCard[] devCards = getCards();
            Card[] cards = new Card[48];
            for (DevelopmentCard devCard : devCards) {
                int ID = devCard.getID();
                int victoryPoints = devCard.getVictoryPoints();
                int level = devCard.getLevel();
                CardColour colour = devCard.getColour();
                EnumMap<CardResources, Integer> top = new EnumMap<>(CardResources.class);
                EnumMap<CardResources, Integer> left = new EnumMap<>(CardResources.class);
                CardResources middle = CardResources.BRACKET;
                EnumMap<CardResources, Integer> right = new EnumMap<>(CardResources.class);
                for (Resource resource : devCard.getCost().keySet()) {
                    top.put(CardResources.valueOf(resource.name()), devCard.getCost().get(resource));
                }
                for (Resource resource : devCard.getProduction().getRequirements().keySet()) {
                    left.put(CardResources.valueOf(resource.name()), devCard.getProduction().getRequirements().get(resource));
                }
                for (Resource resource : devCard.getProduction().getProducts().keySet()) {
                    right.put(CardResources.valueOf(resource.name()), devCard.getProduction().getProducts().get(resource));
                }
                Card card = new Card(ID, victoryPoints, level, colour, top, left, middle, right);
                cards[ID - 1] = card; // card IDs are 1 indexed.
            }
            return cards;
        }

        public void addInMap(JsonArray temp, EnumMap<Resource, Integer> map){
            for(int i = 0; i < temp.size(); i++) {
                String cost1 = temp.get(i).getAsString();
                i++;
                int cost2 = temp.get(i).getAsInt();
                map.put(Resource.valueOf(cost1),cost2);
            }
        }
}
