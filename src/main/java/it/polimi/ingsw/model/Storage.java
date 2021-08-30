// TODO: Fix class & controller to allow usage of extra storage.

package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class Storage {

    private Map<Resource, Integer> shelves;
    private Map<Resource, Integer> extra1;
    private Map<Resource, Integer> extra2;
    private Map<Resource, Integer> strongBox;
    private Lobby lobby;
    private int clientId;
    private int count;// notes how many times extra is called
    private ArrayList<Resource> temp;

    public Storage(Lobby lobby,int clientId) {
        this.shelves = new HashMap<>();
        this.strongBox = new HashMap<>();
        this.count = 0;
        this.lobby = lobby;
        this.clientId = clientId;
        this.extra1 = new EnumMap<Resource, Integer>(Resource.class);
        this.extra2 = new EnumMap<Resource, Integer>(Resource.class);
        this.temp = new ArrayList<>();
    }

    /**
     * gets a resource from shelves without removing it, to check if said resource is present
     * @param resource the resource we want to check
     * @return the quantity of the resource if present else 0
     */
    public Integer getResource(Resource resource) {
        if(shelves.get(resource)!=null)  return shelves.get(resource);
        else return 0;
    }
    //returns size of shelves

    /**
     * returns the size of the storage(shelves)
     * @return shelves size
     */
    public int getShelvesSize(){
        return(shelves.size());
    }

    /**
     * Adds resources in the shelves.
     * @param resource the resource to be added
     * @param quantity the quantity of said resource
     * @throws IllegalArgumentException if function checkIfAddable returns false
     */
    public void addResource(Resource resource, int quantity)throws IllegalArgumentException {
        if(!checkIfAddable(resource,quantity)) throw new IllegalArgumentException();
        else
            if(shelves.containsKey(resource)) {
                int newQuantity = shelves.get(resource) + quantity;
                shelves.replace(resource, newQuantity);
            }
            else {
                shelves.put(resource, quantity);
            }
        Command command = new Command(clientId, Messages.SETRESOURCE,resource.toString() + " " + getResource(resource));
        lobby.updateClients(command);

    }

    /**
     * removes resource from storage(shelves) if it exists and if the quantity demanded is present
     * if the new quantity is 0, we remove the resource from the map, else we just replace the quantity value
     * @param resource the resource we want to remove
     * @param quantity the quantity of said resource
     * @return
     * @throws Exception throws exception if shelves do not contain the resource, or if the quantity demanded is more then what is present
     */
    public int removeStorage(Resource resource,int quantity) throws Exception{
        Command command;
        if(!(shelves.containsKey(resource))) throw new Exception();//
        else{
            if(shelves.get(resource) < quantity) throw new Exception();

            else{
                int newQuantity = shelves.get(resource) - quantity;
                if(newQuantity == 0){
                    shelves.remove(resource);
                }
                else
                    shelves.replace(resource, newQuantity);
            }
            Integer quant = quantity;
            if(shelves.containsKey(resource)) {
                command = new Command(clientId, Messages.SETRESOURCE, resource.toString() + " " + getResource(resource).toString());
            }
            else {
                command = new Command(clientId, Messages.SETRESOURCE, resource.toString() + " " + "0");
            }
            lobby.updateClients(command);
            return quantity;
        }

    }

    /**
     * Checks if a resource is addable, since the storage is made of 3 shelves that can contain only 1,2 and 3 of the same resource
     * respectively, we check if there is still place in the storage for a new resource and if the new quantity respects the
     * limitation explained before.
     * @param resource the resource we want to add
     * @param quantity the quantity of said resource
     * @return false if the resource isn't addable, else true
     */
    public boolean checkIfAddable(Resource resource, int quantity) {
        int x=0;
        int y=0;
        for (Map.Entry<Resource, Integer> iterator : shelves.entrySet()) {
            x+=iterator.getValue();
            if(iterator.getKey()==resource)y+=iterator.getValue();
        }
        if(quantity > 3)
            return false;
        if (shelves.size() == 3){
            if(!shelves.containsKey(resource)) return false;
        }
        if (shelves.containsKey(resource)) {
            // if key already present min value is 1, so max i could add is 2.
            //plus if quantity added is 2 but a resource with already value 2 exist return false anyway.
            if (quantity == 2 && (shelves.containsValue(3) || shelves.get(resource)+quantity > 3))
                return false;
            else if ((quantity == 1 && ((shelves.containsValue(2) && shelves.containsValue(3)))) ||(quantity == 1 && ((shelves.containsValue(2) && shelves.containsValue(1)))&& x==5 && y==1 ) || shelves.get(resource)+quantity > 3)
                return false;

        }
        if(quantity == 3 && shelves.containsValue(3)) return false;
        if(quantity == 2 && shelves.containsValue(2) && shelves.containsValue(3)) return false;
        if(quantity == 1 && shelves.containsValue(1) && shelves.containsValue(2) && shelves.containsValue(3)) return false;

        return true;


    }

    /**
     * Adds resources in the strongobox by simply updating the quantity value of the map,
     * unlike the storage stronbox has unlimited space so we don't need a function checkIfAddable
     * @param resource the resource we want to add
     * @param quantity the quantity of said resource
     */
    public void addInStrongBox(Resource resource, int quantity){
        if(strongBox.containsKey(resource)){
            int newQuantity = strongBox.get(resource) + quantity;
            strongBox.replace(resource,newQuantity);
        }
        else {
            strongBox.put(resource, quantity);
        }
        Command command = new Command(clientId, Messages.SETSTRONG,resource.toString() + " " + getStrong(resource));
        lobby.updateClients(command);

    }

    /**
     * returns the size of the strongbox
     * @return
     */
    public int getStrongSize(){
        return(strongBox.size());
    }
    //removes resources from strongBox if used, throws exception if resource not present or present but non with the number needed.

    /**
     * Removes a resource from the strongbox by simply updating the quantity value fo the map
     * @param resource the resource we want to remove
     * @param quantity the quantity of said resource
     * @return
     * @throws Exception if the requested resource is not present or if the quantity in strongbox is less then requested.
     */
    public int removeBox(Resource resource,int quantity) throws Exception{
        if(!strongBox.containsKey(resource)) throw new Exception();//no such resource in box exception

        else{
            if(strongBox.get(resource) < quantity) throw new Exception();//lessResourceThenAsked exception

            else {
                strongBox.replace(resource, strongBox.get(resource) - quantity);
                Command command = new Command(clientId, Messages.SETSTRONG,resource.toString() + " " + getStrong(resource));
                lobby.updateClients(command);
                return quantity;
            }
        }
    }
    //creates extra storage room if leader card has this power

    /**
     * Creates an extra storage for a certain resource if the players activates a leader card that has that power,
     * in case a player has both leader cards with this power, we keep a count so we now if we have already created one extra storage
     * @param resource the resource which will have extra storage
     */
    public void createExtra(Resource resource){
        if (count == 0) {
            this.extra1 = new EnumMap<>(Resource.class);
            extra1.put(resource, 0);
        }
        if(count == 1) {
            this.extra2 = new EnumMap<>(Resource.class);
            extra2.put(resource, 0);
        }
        count++;
    }

    /**
     * adds a resource in the extra storage, only if it was created beforehand for the requested resource.
     * @param resource the resource we want to add
     * @param quantity the quantity of said resource
     * @throws Exception if there is no extra for said resource, if extra is already full or if adding the new quantity
     * would go over the max space of 2 of the extra storage.
     */
    public void addInExtra(Resource resource,int quantity) throws Exception{
        if(!extra1.containsKey(resource)){
            if(!extra2.containsKey(resource))throw new Exception();//no such resource extra exception
        }
        if(extra1.containsKey(resource)){
            if(extra1.get(resource) == 2)throw new Exception();
            if(extra1.get(resource) == 1 && quantity > 1) throw new Exception();
            extra1.replace(resource,extra1.get(resource) + quantity);
            Command command = new Command(clientId, Messages.SETEXTRA, resource.toString() + " " + extra1.get(resource));
            lobby.updateClients(command);
        } else {
            if(extra2.get(resource) == 2) throw new Exception();
            if(extra2.get(resource) == 1 && quantity > 1) throw new Exception();
            extra2.replace(resource,extra2.get(resource) + quantity);
            Command command = new Command(clientId, Messages.SETEXTRA, resource.toString() + " " + extra2.get(resource));
            lobby.updateClients(command);
        }

    }

    /**
     * Removes resource from the extra storage by replacing the quantity value in the map
     * @param resource the resource requested
     * @param quantity the quantity of said resource
     * @throws Exception if there is no extra of said resource, if the quantity demanded exceeds the quantity present,
     */
    public void removeExtra(Resource resource, int quantity) throws Exception {
        Command command;
        if(!extra1.containsKey(resource)){
            if(!extra2.containsKey(resource))throw new Exception(); //no such resource
        }
        if(extra1.containsKey(resource)) {
            if (extra1.get(resource) < quantity) throw new Exception();
            extra1.replace(resource, extra1.get(resource) - quantity);
            command = new Command(clientId, Messages.SETEXTRA, resource.toString() + " " + extra1.get(resource));
        } else {
            if(extra2.get(resource) < quantity) throw new Exception();
            extra2.replace(resource,extra2.get(resource) - quantity);
            command = new Command(clientId, Messages.SETEXTRA, resource.toString() + " " + extra2.get(resource));
        }
        lobby.updateClients(command);
    }

    public Map<Resource, Integer> getExtra(int id) {
        if (id == 0) {
            return extra1;
        } else {
            return extra2;
        }
    }

    /**
     * returns the strongbox
     * @param resource
     * @return strongbox
     */
    public int getStrong(Resource resource){
        if(strongBox.get(resource)!=null)  return strongBox.get(resource);
        else return 0;
    }

    /**
     * returns the ArrayList getTemp
     * @return getTemp
     */
    public ArrayList<Resource> getTemp() {
        return temp;
    }

    /**
     * sets the temp storage, used after a marble was inserted, since the player has to decide if he wants to add the
     * gained resources in the storage, or discard them.
     * @param temp the ArrayList which works as the temp storage.
     */
    public void setTemp(ArrayList<Resource> temp) {
        this.temp = temp;
        String[] args  = new String[temp.size()];
        for (int i  = 0; i < temp.size(); i++)  {
            args[i] = temp.get(i).name();
        }
        Command command = new Command(clientId, Messages.SETTEMP, String.join(" ", args));
        lobby.updateClients(command);
    }

    /**
     * adds a resource in the temp storage.
     * @param resource the resource to add
     */
    public void addTemp(Resource resource) {
        temp.add(resource);
        setTemp(temp);

    }

    /**
     * removes a resource from the temp storage using the index of the list
     * @param index index of the temp storage list
     */
    public void removeTemp(int index) {
        temp.remove(index);
        setTemp(temp);
    }

}
