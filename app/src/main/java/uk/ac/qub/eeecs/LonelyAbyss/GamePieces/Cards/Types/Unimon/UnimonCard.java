package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.StatusEffect;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
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

    protected Rect templateRect;
    private Bitmap healthBar; //health bar bitmap representing how much health the player has
    private Bitmap manaBar;
    private Bitmap staminaBar;

    /**
     * The cards current evolution type
     */
    private UnimonEvolveType evolveType;

    /**
     * The cards element(eg.Fire/Water etc)
     */
    private Element cardElement;

    private Map<StatusEffect, Integer> statusEffects; ///The status effect applying on the card and how many turns it is to be applied for.


    /**
     * The unimon's current stats
     */
    private int health; // 0-100, number of healthpoints the card has. 0 and the card is no longer active and 'knocked out'
    private int mana; // number of mana points the card has, some moves require mana 
    private int stamina; //number of mana points the card has, some moves require stamina
    
    private int absorptionValue; // percentage value 0-100 how well the card can absorb a specific elemental attack
    private Element absorptionElement; //the specific elemental attack the card can absorb.


    private int weaknessValue; // multipler xINT, how well the card is vunerable to specific elemental attack
    private Element weaknessElement; //the specific elemental attack the card is vunerable to.

    private int armourValue; // percentage value 0-100 of how well the card can absorb attacks in general.
    

    protected int maxHealth; //the max health the unimon card can have
    protected int maxMana; //max mana
    protected int maxStamina; //max stamina

    private UnimonMoves[] moves = new UnimonMoves[3];

    public enum StatType {
        HEALTH, MANA, STAMINA
    }

    private static Paint formatText; // the default format for the card's text

    final int numberOfMoves = 3;

    /**
     * This is a constructor method for the unimon card object.
     * @param health - the Health points of the card
     * @param mana - the number of mana points of the card
     * @param stamina - the number of stamina points of the card
     * @param healthBar - Bitmap used to represent health value
     * @param manaBar - Bitmap used to represent mana value
     * @param staminaBar - Bitmap used to represent stamina value
     * @param evolveType - the evolution type of the card
     * @param cardElement - the element of the card
     */
    public UnimonCard(float x, float y, float width, float height, Bitmap bitmapTemplate, GameScreen gameScreen,
                      String ID, Bitmap healthBar, Bitmap manaBar, Bitmap staminaBar, String name,
                      UnimonEvolveType evolveType, Element cardElement, UnimonMoves[] moves, int health, int mana, int stamina, String description,
                      int armourValue,int weaknessValue, Element weaknessElement, int absorptionValue, Element absorptionElement, boolean revealed, Container container) {
        super(x, y, width, height, bitmapTemplate, gameScreen, ID,name, description, revealed, container);

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
        this.absorptionValue = absorptionValue;
        this.weaknessValue = weaknessValue;
        this.armourValue = armourValue;
        this.weaknessElement = weaknessElement;
        this.absorptionElement = absorptionElement;

        developCard();
    }

    //James Bailey 40156063
    //create a deep copy of the unimon card
    public UnimonCard copyUnimonCard() {
        UnimonCard newCard = new UnimonCard(getX(), getY(), getBound().getWidth(), getBound().getHeight(),
                Bitmap.createBitmap(getBitmap()), getmGameScreen(), getID(), null, null, null, getName(),
                getEvolveType(), getCardElement(), copyUnimonMovesList(), getHealth(), getMana(), getStamina(),
                getDescription(), getArmourValue(), (int) getWeaknessValue(), getWeaknessElement(), getAbsorptionValue(), getAbsorptionElement(), isRevealed(), getContainer());

        return newCard;
    }

    //James Bailey 40156063
    //creat a deep copy of the list of unimon moves
    public UnimonMoves[] copyUnimonMovesList() {
        UnimonMoves[] moves = getMoves();
        UnimonMoves[] movesCopy = new UnimonMoves[numberOfMoves];

        for (int i = 0; i < numberOfMoves; i++) {
            UnimonMoves unimonMove = moves[i];
            movesCopy[i] = unimonMove.copyUnimonMove(); //deep copy of the single unimon move
        }

        return movesCopy;
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

    public Element getWeaknessElement(){ return weaknessElement; }


    public int getAbsorptionValue() {
        return absorptionValue;
    }

    public Element getAbsorptionElement() { return absorptionElement; }

    public int getArmourValue() {
        return armourValue;
    }

    public float getWeaknessValue() {
        return weaknessValue;
    }

    //getters and setters for status effects

    public Map<StatusEffect, Integer> getStatusEffects() {
        return statusEffects;
    }

    public void setStatusEffects(Map<StatusEffect, Integer> statusEffects) {
        this.statusEffects = statusEffects;
    }

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

    //process the overall damage to be inflicted when the armour reduces it
    public int processArmourDAM(int baseDamage) {
        return (int) (baseDamage * ((100 - getArmourValue()) / 100f));
    }

    //processes the overall damage to be inflicted when the weakness multiplies it
    public int processWeaknessDAM(int baseDamage) {
        return (int) (baseDamage * getWeaknessValue());
    }

    //processes the overall damage absorbed by the attack
    public int processAbsorb(int baseDamage) {
        return (int) (baseDamage * getAbsorptionValue()/ 100f);
    }

    /**
     * Checks if the health is less than/equal to zero and if true sets it statue to Graveyard
     * @return - the true;
     */
    public boolean isDead(){
        if(getHealth() <= 0){
            this.container = Container.GRAVEYARD;
            return true;
        }
        return false;
    }


    //James Bailey 40156063
    //create the card's rectangle that will be used to proportionally draw the stats onto
    public void developCard() {
        templateRect = new Rect((int) (mBound.x-mBound.halfWidth), (int) (mBound.y-mBound.halfHeight), (int) (mBound.x+mBound.halfWidth), (int) (mBound.y+mBound.halfHeight));
        setDrawScreenRect(templateRect);
    }

    public void update(ElapsedTime elapsedTime) {
        isDead();
        developCard();
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        drawStats(graphics2D);
    }

    //James Bailey 40156063
    //draw all the stats' bars and foreground text
    private void drawStats(IGraphics2D graphics2D) {
        formatText = formatText(); //get the default formatting for stat text
        drawHealthStat(graphics2D, formatText);
        drawStaminaStat(graphics2D, formatText);
        drawManaStat(graphics2D, formatText);
    }

    //James Bailey 40156063
    //default text formatting methods can call and modify
    private static Paint formatText() {
        if (formatText == null) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.CENTER);
            return paint;
        } else {
            return formatText;
        }
    }

    //James Bailey 40156063
    private void drawHealthStat(IGraphics2D graphics2D, Paint formatText) {
        Rect healthPointTextRect = constructStatRect(StatType.HEALTH);
        drawHealthBar(graphics2D, healthPointTextRect);
        drawHealthText(graphics2D, healthPointTextRect, formatText);
    }

    //James Bailey 40156063
    private void drawHealthText(IGraphics2D graphics2D, Rect healthPointTextRect, Paint formatText) {
        String healthString = ("Health | " +  Integer.toString(health) + "/" + Integer.toString(maxHealth));
        formatText = calculateTextSize(formatText, healthPointTextRect.width()/2, healthString);
        graphics2D.drawText(healthString, healthPointTextRect.centerX(), healthPointTextRect.centerY(), formatText);
    }

    //James Bailey 40156063
    private void drawHealthBar(IGraphics2D graphics2D, Rect healthPointTextRect) {
        int widthHealthBar = (int) (healthPointTextRect.width()*(health/(float) maxHealth));

        Rect healthBarRect = new Rect(healthPointTextRect.left, healthPointTextRect.top, healthPointTextRect.left+widthHealthBar, healthPointTextRect.bottom);

        Bitmap redBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        redBitmap.eraseColor(Color.RED);

        Paint paint = new Paint();

        paint.setAlpha(128);
        graphics2D.drawBitmap(redBitmap, null, healthPointTextRect, paint);
        graphics2D.drawBitmap(redBitmap, null, healthBarRect, null);
    }

    //James Bailey 40156063
    private void drawStaminaStat(IGraphics2D graphics2D, Paint formatText) {
        Rect staminaPointTextRect = constructStatRect(StatType.STAMINA);
        drawStaminaBar(graphics2D, staminaPointTextRect);
        drawStaminaText(graphics2D, staminaPointTextRect, formatText);
    }

    //James Bailey 40156063
    private void drawStaminaText(IGraphics2D graphics2D, Rect staminaPointTextRect, Paint formatText) {
        String staminaString = ("Stamina | " +  Integer.toString(stamina) + "/" + Integer.toString(maxStamina));
        formatText = calculateTextSize(formatText, staminaPointTextRect.width()/2, staminaString);
        graphics2D.drawText(staminaString,staminaPointTextRect.centerX(), staminaPointTextRect.centerY(), formatText);
    }

    //James Bailey 40156063
    private void drawStaminaBar(IGraphics2D graphics2D, Rect staminaPointTextRect) {
        int widthStaminaBar = (int) (staminaPointTextRect.width()*(stamina/(float) maxStamina));

        Rect staminaBarRect = new Rect(staminaPointTextRect.left, staminaPointTextRect.top, staminaPointTextRect.left+widthStaminaBar, staminaPointTextRect.bottom);

        Bitmap greenBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        greenBitmap.eraseColor(Color.rgb(0, 164, 11));

        Paint paint = new Paint();
        paint.setAlpha(128);

        graphics2D.drawBitmap(greenBitmap, null, staminaPointTextRect, paint);
        graphics2D.drawBitmap(greenBitmap, null, staminaBarRect, null);
    }

    //James Bailey 40156063
    //draw the mana bar bitmaps and the text showing the mana stats
    private void drawManaStat(IGraphics2D graphics2D, Paint formatText) {
        Rect manaPointTextRect = constructStatRect(StatType.MANA);
        drawManaBar(graphics2D, manaPointTextRect);
        drawManaText(graphics2D, manaPointTextRect, formatText);
    }

    //James Bailey 40156063
    //draw the text showing the current mana stats
    private void drawManaText(IGraphics2D graphics2D, Rect manaPointTextRect, Paint formatText) {
        String manaString = ("Mana | " +  Integer.toString(mana) + "/" + Integer.toString(maxMana)); //string showing the current mana and the maximum mana the card can have
        formatText = calculateTextSize(formatText, manaPointTextRect.width()/2, manaString); //generate the text size that fits the width of the mana bar
        graphics2D.drawText(manaString,manaPointTextRect.centerX(), manaPointTextRect.centerY(), formatText);
    }


    //James Bailey 40156063
    //draw mana bar onto the unimon card
    private void drawManaBar(IGraphics2D graphics2D, Rect manaPointTextRect) {
        int widthManaBar = (int) (manaPointTextRect.width() * (mana / (float) maxMana)); //calculate the width of the mana bar by the percentage of mana the unimon card currently has

        Rect manaBarRect = new Rect(manaPointTextRect.left, manaPointTextRect.top, manaPointTextRect.left + widthManaBar, manaPointTextRect.bottom); //the current mana bar dimensions

        //create the bitmap for the mana bar
        Bitmap blueBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        blueBitmap.eraseColor(Color.BLUE); //fill with blue

        //used for drawing the full mana bar - opacity 50%
        Paint paint = new Paint();
        paint.setAlpha(128);

        graphics2D.drawBitmap(blueBitmap, null, manaPointTextRect, paint); //draw the full mana bar in the background of the remaining mana bar

        graphics2D.drawBitmap(blueBitmap, null, manaBarRect, null); //draw the current mana bar in the foreground - emphasises contrast through opacity
    }

    //James Bailey 40156063
    //scale a string to a required width boundary, calculating an optimum text size
    private Paint calculateTextSize(Paint paint, float width, String text) {
        float optimumTextSize = 60; //optimum text size - used to test the text's boundaries
        paint.setTextSize(optimumTextSize);

        Rect bounds = new Rect(); //the boundary for the text - smallest rectangle to contain all the characters in the string
        paint.getTextBounds(text, 0, text.length(), bounds); //calculate smallest rectangles

        float textSize = optimumTextSize * width/bounds.width(); //calculates text size that will fit into the boundary of the required width

        paint.setTextSize(textSize); //set the paint's text size to the calculated size
        return paint;
    }

    //James Bailey 40156063
    //construct the rectangle boundaries for each stat to be drawn onto the card proportional to the card's size
    private Rect constructStatRect(StatType statToBeDrawn) {
        Rect statRect = new Rect();
        switch (statToBeDrawn) {
            case HEALTH:
                statRect = new Rect((templateRect.left), (int) (templateRect.top + (templateRect.height()*0.4f)), (int) (templateRect.right-(templateRect.width())*0.55f),  (int) (templateRect.top + (templateRect.height()*0.46)));
                break;
            case STAMINA:
                statRect = new Rect((templateRect.left), (int) (templateRect.top + (templateRect.height()*0.6f)), (int) (templateRect.right-(templateRect.width())*0.55f),  (int) (templateRect.top + (templateRect.height()*0.66)));
                break;
            case MANA:
                statRect = new Rect((templateRect.left), (int) (templateRect.top + (templateRect.height()*0.5f)), (int) (templateRect.right-(templateRect.width())*0.55f),  (int) (templateRect.top + (templateRect.height()*0.56)));
                break;
            default:
                break;
        }
        return statRect;
    }
}


