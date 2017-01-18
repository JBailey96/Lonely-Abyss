package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by jdevl on 25/11/2016.
 */

public class EnergyCard extends Card {

    /**
     * Enum class of card Energy types
     */
    public enum  EnergyType   {
        MANA, STAMINA, HEALTH, MIXED;
    }

    /**
     * Bitmap images to make up areas of the card
     */
    private Bitmap backGround, icon;

    /**
     * The type of the energy card
     */
    private EnergyType type;

    /**
     * The unimon evolution type to the energy types and the value associated with them.
     */
    private Map<UnimonEvolveType, Map<EnergyType, Integer>>  energy;

    protected boolean cardUsed; //the current use status of the card (used or not used).

    public EnergyCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen,String ID,
                      Bitmap backGround, Bitmap icon, String name, EnergyType type, Map<UnimonEvolveType,Map<EnergyType, Integer>> energy, String description, boolean revealed, CardStatus status) {
        super(x, y, width, height, bitmap, gameScreen,ID,name,description, revealed, icon, status);
        this.backGround = backGround;
        this.type = type;
        this.energy = new HashMap<UnimonEvolveType, Map<EnergyType, Integer>>(energy);
        this.cardUsed = false;
    }

    /**
     * This method returns the background image in the card
     * @return - the background bitmap
     */
    public Bitmap getBackGround() {
        return backGround;
    }

    /**
     * This method sets the backGround image of the card
     * @param backGround - the background image
     */
    public void setBackGround(Bitmap backGround) {
        this.backGround = backGround;
    }

    /**
     * This method returns the energy type icon image assigned to the card
     * @return - the icon bitmap
     */
    public Bitmap getIcon() {
        return icon;
    }

    /**
     * This method sets the type icon of the card
     * @param icon - type icon of the card
     */
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    /**
     * This method returns the energy type of the card
     * @return - the energy type
     */
    public EnergyType getType() {
        return type ;
    }

    /**
     * This method sets the energy type of the card
     * @param type - energy type of the card
     */
    public void setType(EnergyType type) {
        this.type = type;
    }

    //Apply the energy card effect to the player's unimon card.
    public void applyEnergy (UnimonCard playerCard) {
        if (this.status == CardStatus.HAND) { //validates that the card has nto been used yet by the player.
            Map<EnergyType, Integer> energyEffects = energy.get(playerCard.getEvolveType()); //get the energy type effects for the player's unimon card
            for (EnergyType energyEff: energyEffects.keySet()) { //for each energy effect
                switch (energyEff) { //selection on the type of energy effect to be applied
                    case MANA:
                        playerCard.increaseMana(energyEffects.get(EnergyType.MANA)); //get the mana increase value and apply this to the player's unimon card.
                        break;
                    case HEALTH:
                        playerCard.increaseHealth(energyEffects.get(EnergyType.HEALTH));
                        break;
                    case STAMINA:
                        playerCard.increaseStamina(energyEffects.get(EnergyType.STAMINA));
                    default:
                        break;
                }
            }
            this.status = CardStatus.GRAVEYARD; //card has been used
        }
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }
}


