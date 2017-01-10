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

public class unimonCard extends Card {

    /**
     * Enum class of card evolving types
     */
    public enum EvolveType{
        PHANTOM,DEMON,SPIRIT,UNIQUE
    }

    /**
     * Enum of card type
     */
    public enum Type{
        DARK,EARTH,FIRE,WATER,HOLY
    }

    /**
     * Bitmap images to make up areas of the card
     */
    private Bitmap backGround, typeIcon, healthBar, manaBar, staminaBar;


    /**
     * The cards current evolution type
     */
    private EvolveType evolveType;

    /**
     * The cards type(eg.Fire/Water etc)
     */
    private Type type;

    /**
     * The unimons current stats
     */
    private int health, mana, stamina;

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
     * @param type - the type of card
     */
    public unimonCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen,
                      String ID, Bitmap backGround, Bitmap typeIcon, Bitmap healthBar, Bitmap manaBar, Bitmap staminaBar, String name,
                      EvolveType evolveType, Type type, int health, int mana, int stamina, String description) {
        super(x, y, width, height, bitmap, gameScreen,ID,name,description);
        this.backGround = backGround;
        this.health = health;
        this.mana = mana;
        this.stamina = stamina;
        this.typeIcon = typeIcon;
        this.healthBar = healthBar;
        this.manaBar = manaBar;
        this.staminaBar = staminaBar;
        this.evolveType = evolveType;
        this.type = type;

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

    public Bitmap getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(Bitmap typeIcon){this.typeIcon = typeIcon; }

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

    public EvolveType getEvolveType() {
        return evolveType;
    }

    public void setEvolveType(EvolveType evolveType){this.evolveType = evolveType; }

    public Type getType() {
        return type;
    }

    public void setType(Type type){this.type = type; }



    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

    }

}


