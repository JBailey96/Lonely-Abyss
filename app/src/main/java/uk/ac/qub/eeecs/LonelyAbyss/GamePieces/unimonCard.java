package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by jdevl on 25/11/2016.
 */

public class UnimonCard extends Card {


    /**
     * Bitmap images to make up areas of the card
     */
    private Bitmap backGround, healthBar, manaBar, staminaBar, absorption, weakness,armour;


    /**
     * The cards current evolution type
     */
    private UnimonEvolveType evolveType;

    /**
     * The cards element(eg.Fire/Water etc)
     */
    private Element cardElement;

    private StatusEffect statusEffect1;
    private int statusEffectCounter1;
    private StatusEffect statusEffect2;
    private int statusEffectCounter2;
    private StatusEffect statusEffect3;
    private int statusEffectCounter3;

    /**
     * The unimons current stats
     */
    private int health, mana, stamina, absorptionValue, armourValue;

    private float weaknessValue;

    private Element weaknessElement;
    private Element absorbtionElement;

    protected int maxHealth; //the max health the unimon card can have
    protected int maxMana; //max mana
    protected int maxStamina; //max stamina

    private UnimonMoves[] moves = new UnimonMoves[3];

    /**
     * This is a constructor method for the unimon card object.
     * @param backGround - the cards image
     * @param health - the Health points of the card
     * @param mana - the number of mana points of the card
     * @param stamina - the number of stamina points of the card
     * @param typeIcon - Bitmap used to represent the type of the card
     * @param healthBar - Bitmap used to represent health value
     * @param manaBar - Bitmap used to represent mana value
     * @param staminaBar - Bitmap used to represent stamina value
     * @param evolveType - the evolution type of the card
     * @param cardElement - the element of the card
     */
    public UnimonCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen,
                      String ID, Bitmap backGround, Bitmap typeIcon, Bitmap healthBar, Bitmap manaBar, Bitmap staminaBar, String name,
                      UnimonEvolveType evolveType, Element cardElement, UnimonMoves[] moves, int health, int mana, int stamina, String description,
                      Bitmap armour, int armourValue, Bitmap weakness, float weaknessValue, Element weaknessElement, Bitmap absorption, int absorptionValue, Element absorbtionElement, boolean revealed, Container container, StatusEffect statusEffect1, int statusEffectCounter1, StatusEffect statusEffect2,int statusEffectCounter2, StatusEffect statusEffect3, int statusEffectCounter3) {
        super(x, y, width, height, bitmap, gameScreen, ID,name, description, revealed, typeIcon, container);
        this.backGround = backGround;

        //stats
        this.health = health;
        this.mana = mana;
        this.stamina = stamina;
        this.maxHealth = health;
        this.maxMana = mana;
        this.maxStamina = stamina;


        this.healthBar = healthBar;
        this.manaBar = manaBar;
        this.staminaBar = staminaBar;
        this.evolveType = evolveType;
        this.cardElement = cardElement;
        this.moves = moves;

        //defensive stats
        this.absorption = absorption;
        this.absorptionValue = absorptionValue;
        this.weakness = weakness;
        this.weaknessValue = weaknessValue;
        this.armour = armour;
        this.armourValue = armourValue;
        this.weaknessElement = weaknessElement;
        this.absorbtionElement = absorbtionElement;

        //setting status effects
        this.statusEffect1 = statusEffect1;
        this.statusEffectCounter1 = statusEffectCounter1;
        this.statusEffect2 = statusEffect2;
        this.statusEffectCounter2 = statusEffectCounter2;
        this.statusEffect3 = statusEffect3;
        this.statusEffectCounter3 = statusEffectCounter3;


    }

    public UnimonMoves[] getMoves() {
        return this.moves;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public Bitmap getBackGround() {
        return backGround;
    }

    public void setBackGround(Bitmap backGround){this.backGround = backGround; }

    public Bitmap getHealthBar() {
        return healthBar;
    }

    public void setHealthBar(Bitmap healthBar){this.healthBar = healthBar; }

    public Bitmap getStaminaBar() {
        return staminaBar;
    }

    public void setStaminaBar(Bitmap staminaBar){this.staminaBar = staminaBar; }

    public Bitmap getManaBar() {
        return manaBar;
    }

    public void setManaBar(Bitmap manaBar){this.manaBar = manaBar; }

    public UnimonEvolveType getEvolveType() {
        return evolveType;
    }

    public void setEvolveType(UnimonEvolveType evolveType){this.evolveType = evolveType; }

    public Element getCardElement() {
        return cardElement;
    }

    public void setElement(Element cardElement){this.cardElement = cardElement; }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }


    public Bitmap getAbsorption() {
        return absorption;
    }

    public Bitmap getWeakness() {
        return weakness;
    }

    public Element getWeaknessElement(){ return weaknessElement; }


    public Bitmap getArmour() {
        return armour;
    }

    public int getAbsorptionValue() {
        return absorptionValue;
    }

    public Element getAbsorbtionElement() { return absorbtionElement; }

    public int getArmourValue() {
        return armourValue;
    }

    public float getWeaknessValue() {
        return weaknessValue;
    }

    //getters and setters for status effects

    public StatusEffect getStatusEffect1(){ return statusEffect1; }

    public void setStatusEffect1( StatusEffect statusEffect1){ this.statusEffect1 = statusEffect1; }

    public StatusEffect getStatusEffect2(){ return statusEffect2; }

    public void setStatusEffect2( StatusEffect statusEffect2){ this.statusEffect2 = statusEffect2; }

    public StatusEffect getStatusEffect3(){ return statusEffect3; }

    public void setStatusEffect3( StatusEffect statusEffect3){ this.statusEffect3 = statusEffect3; }


    //increase unimon card's mana (e.g when an energy card is applied)
    public void increaseMana(int addMana) {
        if (validIntParam(addMana)) { //check if the desired increase in mana is valid (above >= 0)

            int increasedMana = getMana() + addMana; //what the mana will be after the increase

            if (increasedMana >= getMaxMana()) { //checks whether the increased mana is the max mana or greater
                setMana(getMaxMana()); //set the mana TO MAX
            } else {
                setMana(increasedMana); //set the mana to the increased mana total
            }
        }
    }

    public void increaseHealth(int addHealth) {
        if (validIntParam(addHealth)) {

            int increasedHealth = getHealth() + addHealth;

            if (increasedHealth >= getMaxHealth()) {
                setHealth(getMaxHealth());
            } else {
                setHealth(increasedHealth);
            }
        }
    }

    public void increaseStamina(int addStamina) {
        if (validIntParam(addStamina)) {

            int increasedStamina = getStamina () + addStamina;

            if (increasedStamina >= getMaxStamina()) {
                setStamina(getMaxStamina());
            } else {
                setStamina(increasedStamina);
            }
        }
    }

    //increase unimon card's mana (e.g when an opponent's unimon makes an offensive move against this card)
    public void decreaseMana(int subMana) {
        if (validIntParam(subMana)) { //validates whether the desired increase in mana is valid (above >= 0)
            int decreasedMana = getMana() - subMana; //the value the mana is after the decrease

            if (0 >= decreasedMana) { // if the mana is 0 or negative, then set mana to 0
                setMana(0);
            } else {
                setMana(decreasedMana); // set the mana to the decreased mana total.
            }
        }
    }

    public void decreaseHealth(int subHealth) {
        if (validIntParam(subHealth)) {
            int decreasedHealth = getHealth() - subHealth;

            if (0 >= decreasedHealth) {
                setHealth(0);
            } else {
                setHealth(decreasedHealth);
            }
        }
    }

    public void decreaseStamina(int subStamina) {
        if (validIntParam(subStamina)) {
            int decreasedStamina = getStamina() - subStamina;

            if (0 >= decreasedStamina) {
                setStamina(0);
            } else {
                setStamina(decreasedStamina);
            }
        }
    }

    //increase the unimon card's mana by a percentage value (>0.0%)
    public void increaseManaPercent(float percMana) {
        if (validPercent(percMana)) { //validates whether the parameter is a valid percentage.
            int increasedMana = ((int) (getMana() + (getMana()*(percMana/100)))); //value the mana will be after the increased by percentage
            if (increasedMana >= getMaxMana()) { //if the increased mana by percentage is greater than the max mana of the card
                setMana(getMaxMana()); //set mana of card to max mana
            } else {
                setMana(increasedMana); //set mana of card to the increased mana by percentage
            }
        }
    }

    public void increaseHealthPercent(float percHealth) {
        if (validPercent(percHealth)) {
            int increasedHealth = ((int) (getHealth() + (getHealth()*(percHealth/100))));

            if (increasedHealth >= getMaxHealth()) {
                setHealth(getMaxHealth());
            } else {
                setHealth(increasedHealth);
            }
        }
    }

    public void increaseStaminaPercent(float percStamina) {
        if (validPercent(percStamina)) {
            int increasedStamina = ((int) (getStamina() + (getStamina()*(percStamina/100))));

            if (increasedStamina >= getMaxStamina()) {
                setStamina(getMaxStamina());
            } else {
                setStamina(increasedStamina);
            }
        }
    }

    //decrease the card's mana by a percentage value (>0.0%)
    public void decreaseManaPercent(float percMana) {
        if (validPercent(percMana)) { //if the percentage value in the parameter is valid
            int decreasedMana = ((int) (getMana() - (getMana()*(percMana/100)))); //the value the mana will be after the decrease by percentage.

            if (0 >= decreasedMana) { //if the decreased mana by percentage is 0 or less
                setMana(0); //set card's mana to 0
            } else {
                setMana(decreasedMana); //set card's mana to decreaed mana by percentage
            }
        }

    }

    public void decreaseHealthPercent(float percHealth) {
        if (validPercent(percHealth)) {
            int decreasedHealth= ((int) (getHealth() - (getHealth()*(percHealth/100))));

            if (0 >= decreasedHealth) {
                setHealth(0);
            } else {
                setHealth(decreasedHealth);
            }
        }
    }

    public void decreaseStaminaPercent(float percStamina) {
        if (validPercent(percStamina)) {
            int decreasedStamina = ((int) (getStamina() - (getStamina()*(percStamina/100))));

            if (0 >= decreasedStamina) {
                setStamina(0);
            } else {
                setHealth(decreasedStamina);
            }
        }
    }

    // validates the integer parameter on value increases/decreases is valid
    public boolean validIntParam(int input) {
        if (0 >= input) { //validates the value is not negative or 0.
            return false;
        }
        return true;
    }

    // validates the float parameter on percentage value increases/decreases is valid
    public boolean validPercent(float input) {
        if (0 >= input) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the health is less than/equal to zero and if true sets it statue to Graveyard
     * @return - the true;
     */
    public boolean dead(){
        if(getHealth() <= 0){
            this.container = Container.GRAVEYARD;
        }
        return true;
    }

    public void update(ElapsedTime elapsedTime) {
        dead();
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }

}


