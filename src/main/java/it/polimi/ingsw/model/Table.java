package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.DevelopmentCardsGetter;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.*;

public class Table {
    List<Stack<DevelopmentCard>> decks;
    //Added market as an attribute for table
    private Market market;
    DevelopmentCard[] cards;
    private Lobby lobby;
    private Match match;
    int i = 0;
    //Same constructor for both Table and market
    public Table(Lobby lobby, Match match) {
            decks = new ArrayList<>();
            this.match = match;
            this.market = new Market(lobby, match);
            this.cards = new DevelopmentCardsGetter().getCards();
            Collections.shuffle(Arrays.asList(this.cards));
            createDeck(cards);
            this.lobby = lobby;
            sendDecks();
    }

    /**
     * creates the 3x4 deck which contains all the development cards for sale. The deck is made of an arrayList of Stacks.
     * The first 4 index of the array represent the first row of the 3x4 matrix and so on.
     * Each stack represents a smaller a cell of the 3x4 deck.
     * each smaller deck is a stack since the player can always only see and buy the top most card of each "small deck".
     * @param array The array containing the 48 development cards.
     */
    public void createDeck(DevelopmentCard[] array){
        Stack<DevelopmentCard> green1 = new Stack<>();
        Stack<DevelopmentCard> blue1 = new Stack<>();
        Stack<DevelopmentCard> yellow1 = new Stack<>();
        Stack<DevelopmentCard> purple1 = new Stack<>();
        Stack<DevelopmentCard> green2 = new Stack<>();
        Stack<DevelopmentCard> blue2 = new Stack<>();
        Stack<DevelopmentCard> yellow2 = new Stack<>();
        Stack<DevelopmentCard> purple2 = new Stack<>();
        Stack<DevelopmentCard> green3 = new Stack<>();
        Stack<DevelopmentCard> blue3 = new Stack<>();
        Stack<DevelopmentCard> yellow3 = new Stack<>();
        Stack<DevelopmentCard> purple3 = new Stack<>();
        for(i = 0; i < array.length; i++){
            switch (array[i].getLevel()){
                case 1 ->{
                    addInStack(array, green1, blue1, yellow1, purple1);
                }
                case 2 ->{
                    addInStack(array, green2, blue2, yellow2, purple2);
                }
                case 3 ->{
                    addInStack(array, green3, blue3, yellow3, purple3);
                }
            }
        }
        //row 1
        decks.add(0,green1);
        decks.add(1,blue1);
        decks.add(2,yellow1);
        decks.add(3,purple1);

        //row 2
        decks.add(4,green2);
        decks.add(5,blue2);
        decks.add(6,yellow2);
        decks.add(7,purple2);

        //row 3
        decks.add(8,green3);
        decks.add(9,blue3);
        decks.add(10,yellow3);
        decks.add(11,purple3);

    }

    /**
     * transfers the cards from the array to the stacks, based on the card colour and level
     * @param array the array that contains all the development cards
     * @param green the stack that will contain the green cards
     * @param blue  the stack that will contain the blue cards
     * @param yellow the stack that will contain the yellow cards
     * @param purple the stack that will contain the purple cards
     */
    private void addInStack(DevelopmentCard[] array, Stack<DevelopmentCard> green, Stack<DevelopmentCard> blue, Stack<DevelopmentCard> yellow, Stack<DevelopmentCard> purple) {
        switch (array[i].getColour()) {
            case GREEN -> green.push(array[i]);
            case BLUE ->  blue.push(array[i]);
            case YELLOW -> yellow.push(array[i]);
            case PURPLE -> purple.push(array[i]);
        }
    }

    /**
     * gets the top card of a certain small deck.
     * @param x the index of the "Deck array", that contains the stack we want to acces.
     * @return a copy og the top most card.
     */
    public DevelopmentCard getCard(int x){
        if (x < 12 && !decks.get(x).empty()) {
            return decks.get(x).peek();
        } else {
            return null;
        }
    }

    /**
     * removes the top cards of a certain small deck by checking if the top card equals the requested card.
     * @param developmentCard The development card we want to remove
     * @param clientId the id representing the client interested in said card.
     */
    public void removeCard(DevelopmentCard developmentCard,int clientId){
        Command command = null;
        //Pop the first card and return it,
        for(i=0;i<12;i++){
            if(!decks.get(i).empty() && decks.get(i).peek().equals(developmentCard)){
                decks.get(i).pop();
                if (decks.get(i).empty()) { // Check if empty after popping, notify client of the card underneath.
                    command = new Command(clientId, Messages.SETMARKETCARD, i + " -1");
                } else {
                    command = new Command(clientId, Messages.SETMARKETCARD, i + " " + decks.get(i).peek().getID());
                }
                break;
            }
        }
        if (command != null) {
            lobby.updateClients(command);
        }
    }

    /**
     * removes a development card form the decks this time using the index
     * @param index index representing the stack we want to remove from
     */
    public void removeCard(int index) {
        Command command;
        if (index >= 0 && index < 12 && !decks.get(index).empty()) {
            decks.get(index).pop();
        }
        if (decks.get(index).empty()) {
            command = new Command(0, Messages.SETMARKETCARD, index + " -1");
        } else {
            command = new Command(0, Messages.SETMARKETCARD, index + " " + decks.get(index).peek().getID());
        }
        lobby.updateClients(command);
    }

    /**
     * Method that sends the deck to te client for viewing purposes
     */
    public void sendDecks() {
        ArrayList<String> cardIDs = new ArrayList<String>();
        for (Stack<DevelopmentCard> deck : decks) {
            cardIDs.add(String.valueOf(deck.peek().getID()));
        }
        Command command = new Command(0, Messages.SETMARKETCARDS, String.join(" ", cardIDs));
        lobby.updateClients(command);
    }

    /**
     *
     * @return the market
     */
    public Market getMarket() {
        return market;
    }

    /**
     *
     * @return returns the match
     */
    public Match getMatch() {
        return match;
    }

    /**
     * returns the deck
     * @return deck
     */
    public Card[] getDecks() {
        Card[] decks = new Card[12];
        for (int i = 0; i < 12; i++) {
            decks[i] = this.decks.get(i).empty() ? null : this.decks.get(i).peek();
        }
        return decks;
    }

    public DevelopmentCard[] getCards() {
        return cards;
    }
}
