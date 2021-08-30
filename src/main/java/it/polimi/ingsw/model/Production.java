package it.polimi.ingsw.model;

import com.sun.scenario.effect.impl.prism.PrDrawable;

import java.util.EnumMap;

public class Production implements Cloneable {

    private EnumMap<Resource, Integer> requirements;
    private final EnumMap<Resource, Integer> products;

    /**
     * Constructor for Production
     *
     * @param requirements the resources need for the production
     * @param products     the resources we get after the production
     */
    public Production(EnumMap<Resource, Integer> requirements, EnumMap<Resource, Integer> products) {
        this.requirements = requirements;
        this.products = products;
    }

    /**
     * @return requirements
     */
    public EnumMap<Resource, Integer> getRequirements() {
        return requirements;
    }

    /**
     * @return products
     */
    public EnumMap<Resource, Integer> getProducts() {
        return products;
    }

    /**
     * clones the production of the leader card used in buy card and play leader
     * @return a clone of production
     */
    @Override
    public Object clone() {
        Production production=null;
        try {
            production = (Production) super.clone();
        } catch (CloneNotSupportedException e) {
            production = new Production(this.requirements,this.products);
        }
        production.requirements = (EnumMap<Resource,Integer>)this.requirements.clone();
        return production;
    }
}