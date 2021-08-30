package it.polimi.ingsw.shared;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.Card;
import it.polimi.ingsw.view.CardResources;


import java.io.*;
import java.util.EnumMap;

public class LeaderCardsGetter {
    JsonElement element;
    LeaderCard[] leaderDeck = new LeaderCard[16];
    int i = 0;

    public LeaderCard[] getCards() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("leaderCards.json");
        InputStreamReader reader = new InputStreamReader(inputStream);
        element = JsonParser.parseReader(reader);
        JsonObject object = element.getAsJsonObject();
        addJsonObject(object);
        return leaderDeck;
    }

    public void addJsonObject(JsonObject jsonObj) {
        jsonObj.keySet().forEach(keyStr ->
        {
            JsonElement keyvalue = jsonObj.get(keyStr);
            JsonObject object = keyvalue.getAsJsonObject();

            //for nested objects iteration
            if (keyvalue instanceof JsonObject) {
                Integer Id = object.get("ID").getAsInt();
                Integer vPoints = object.get("victoryPoints").getAsInt();
                JsonObject req1 = object.get("req1").getAsJsonObject();
                Integer level = null;
                String resource = null;
                String colour = null;
                LeaderRequirement requirement1 = null;
                LeaderRequirement requirement2 = null;
                Bonus bonus = null;
                Boolean checkReq2 = false;
                Integer amount = req1.get("amount").getAsInt();

                if (req1.has("level")) {
                    colour = req1.get("colour").getAsString();
                    level = req1.get("level").getAsInt();
                    requirement1 = new LeaderRequirement(level, CardColour.valueOf(colour), amount);
                } else if (req1.has("colour")) {
                    colour = req1.get("colour").getAsString();
                    requirement1 = new LeaderRequirement(CardColour.valueOf(colour), amount);
                }


                if (req1.has("resource")) {
                    resource = req1.get("resource").getAsString();
                    EnumMap<Resource, Integer> map = new EnumMap<Resource, Integer>(Resource.class);
                    map.put(Resource.valueOf(resource), amount);
                    requirement1 = new LeaderRequirement(map);
                }


                if (object.has("req2")) {
                    checkReq2 = true;
                    JsonObject req2 = object.get("req2").getAsJsonObject();
                    amount = req2.get("amount").getAsInt();


                    if (req2.has("level")) {
                        colour = req2.get("colour").getAsString();
                        level = req2.get("level").getAsInt();
                        requirement2 = new LeaderRequirement(level, CardColour.valueOf(colour), amount);
                    } else if (req2.has("colour")) {
                        colour = req2.get("colour").getAsString();
                        requirement2 = new LeaderRequirement(CardColour.valueOf(colour), amount);
                    }


                    if (req2.has("resource")) {
                        resource = req2.get("resource").getAsString();
                        EnumMap<Resource, Integer> map2 = new EnumMap<Resource, Integer>(Resource.class);
                        map2.put(Resource.valueOf(resource), amount);
                        requirement2 = new LeaderRequirement(map2);
                    }

                }
                JsonObject bonusObject = object.get("bonus").getAsJsonObject();

                if (bonusObject.has("discount")) {
                    String discount = bonusObject.get("discount").getAsString();
                    bonus = new Bonus(Resource.valueOf(discount));
                }
                if (bonusObject.has("storage")) {
                    String storage = bonusObject.get("storage").getAsString();
                    bonus = new Bonus(storage);
                }
                if (bonusObject.has("marble")) {
                    String marble = bonusObject.get("marble").getAsString();
                    bonus = new Bonus(MarbleColour.valueOf(marble));
                }
                if (bonusObject.has("production")) {
                    JsonObject productionObject = bonusObject.get("production").getAsJsonObject();
                    JsonArray tempReq = productionObject.get("req").getAsJsonArray();

                    EnumMap<Resource, Integer> req = new EnumMap<>(Resource.class);
                    addInMap(tempReq, req);

                    JsonArray tempProd = productionObject.get("prod").getAsJsonArray();

                    EnumMap<Resource, Integer> prod = new EnumMap<>(Resource.class);
                    addInMap(tempProd, prod);

                    Production production = new Production(req, prod);
                    bonus = new Bonus(production);


                }
                LeaderCard leaderCard;
                if (checkReq2)
                    leaderCard = new LeaderCard(vPoints, requirement1, requirement2, bonus, Id);
                else
                    leaderCard = new LeaderCard(vPoints, requirement1, bonus, Id);

                leaderDeck[i] = leaderCard;
                i++;
            }
        });
    }

    public Card[] getViewCards() {
        LeaderCard[] leaderCards = getCards();
        Card[] cards = new Card[16];
        for (LeaderCard leaderCard : leaderCards) {
            LeaderRequirement req1 = leaderCard.getReq1();
            LeaderRequirement req2 = leaderCard.getReq2();
            int ID = leaderCard.getID();
            int victoryPoints = leaderCard.getVictoryPoints();
            int level = req1.getLevel();
            EnumMap<CardResources, Integer> top = new EnumMap<>(CardResources.class);
            EnumMap<CardResources, Integer> left = new EnumMap<>(CardResources.class);
            CardResources middle = CardResources.EMPTY;
            EnumMap<CardResources, Integer> right = new EnumMap<>(CardResources.class);
            if (req1.getColour() != null) top.put(CardResources.valueOf(req1.getColour().name()), req1.getAmount());
            if (req1.getLevel() != 0) top.put(CardResources.LEVEL, req1.getLevel());
            if (req1.getResource() != null) {
                for (Resource resource : req1.getResource().keySet()) {
                    top.put(CardResources.valueOf(resource.name()), req1.getResource().get(resource));
                }
            }
            if (req2 != null) {
                if (req2.getColour() != null) top.put(CardResources.valueOf(req2.getColour().name()), req2.getAmount());
                if (req2.getLevel() != 0) top.put(CardResources.LEVEL, req2.getLevel());
                if (req2.getResource() != null) {
                    for (Resource resource : req2.getResource().keySet()) {
                        top.put(CardResources.valueOf(resource.name()), req2.getResource().get(resource));
                    }
                }
            }
            switch (leaderCard.getBonus().getType()) {
                case MARBLE:
                    left.put(CardResources.MARBLE, 0);
                    middle = CardResources.ARROW;
                    right.put(CardResources.valueOf(leaderCard.getBonus().getMarble().name()), 0);
                    break;
                case STORAGE:
                    CardResources storage = CardResources.valueOf(leaderCard.getBonus().getStorage());
                    left.put(storage, 0);
                    right.put(storage, 0);
                    break;
                case DISCOUNT:
                    left.put(CardResources.DISCOUNT, 0);
                    right.put(CardResources.valueOf(leaderCard.getBonus().getDiscount().name()), 0);
                    break;
                case PRODUCTION:
                    for (Resource resource : leaderCard.getBonus().getProduction().getRequirements().keySet()) {
                        left.put(CardResources.valueOf(resource.name()), leaderCard.getBonus().getProduction().getRequirements().get(resource));
                    }
                    for (Resource resource : leaderCard.getBonus().getProduction().getProducts().keySet()) {
                        right.put(CardResources.valueOf(resource.name()), leaderCard.getBonus().getProduction().getProducts().get(resource));
                    }
                    middle = CardResources.BRACKET;
            }
            cards[ID - 49] = new Card(ID, victoryPoints, level, null, top, left, middle, right); // 48 development cards come before + 1 because they're 1 indexed.
        }
        return cards;
    }

    private void addInMap(JsonArray tempReq, EnumMap<Resource, Integer> req) {
        for (int i = 0; i < tempReq.size(); i++) {
            String cost1 = tempReq.get(i).getAsString();
            i++;
            int cost2 = tempReq.get(i).getAsInt();
            req.put(Resource.valueOf(cost1), cost2);
        }
    }
}
