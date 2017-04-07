package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.StatusEffect;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
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
     * Bitmap images to make up areas of the card
     */
    private Bitmap backGround;

    /**
     * The type of the energy card
     */
    private EnergyType type;

    /**
     * The unimon evolution type to the energy types and the value associated with them.
     */
    private Map<UnimonEvolveType, Map<EnergyType, Integer>>  energy;

    public EnergyCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen, String ID,
                      Bitmap backGround, String name, EnergyType type, Map<UnimonEvolveType,Map<EnergyType, Integer>> energy, String description, boolean revealed, Container container) {
        super(x, y, width, height, bitmap, gameScreen,ID,name,description, revealed, container);
        this.backGround = backGround;
        this.type = type;
        this.energy = new HashMap<UnimonEvolveType, Map<EnergyType, Integer>>(energy);
    }

    //James Bailey 40156063
    //Create a deep copy of the energy card.
    public EnergyCard copyEnergyCard() {
        EnergyCard copyEnergy = new EnergyCard(getX(), getY(), getmBound().getWidth(), getmBound().getHeight(), Bitmap.createBitmap(getBitmap()), getmGameScreen(), getID(), null, getName(), getType(), copyEnergyEffects(), getDescription(), isRevealed(), getContainer());
        return copyEnergy;
    }

    //James Bailey 40156063
    //Create a deep copy of the energy card's energy effects.
    public Map<UnimonEvolveType, Map<EnergyType, Integer>> copyEnergyEffects() {
        Map<UnimonEvolveType, Map<EnergyType, Integer>> energy = getEnergy();
        Map<UnimonEvolveType, Map<EnergyType, Integer>> copyEnergy = new HashMap<>(); //used to store the energy card's effects

        for (UnimonEvolveType evolveType : energy.keySet()) {
            Map<EnergyType, Integer> energyEff = energy.get(evolveType);
            Map<EnergyType, Integer> copyEnergyEff = new HashMap<>();

            for(EnergyType energyType : energyEff.keySet()) {
                int value = energyEff.get(energyType);
                copyEnergyEff.put(energyType, value);
            }
            copyEnergy.put(evolveType, copyEnergyEff);
        }
        return copyEnergy;
    }

    //test constructor
    public EnergyCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen, String ID,
                      Bitmap backGround, String name, EnergyType type, String description, boolean revealed, Container container) {
        super(x, y, width, height, bitmap, gameScreen, ID, name, description, revealed, container);
        this.backGround = backGround;
        this.type = type;

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
        if (this.container == Container.HAND) { //validates that the card is in the players hand
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
                        break;
                    case CURE:
                        playerCard.getStatusEffects().clear();
                        break;
                    default:
                        break;
                }
            }
            this.setContainer(Container.GRAVEYARD); //card has been used
        }
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }

    public Map<UnimonEvolveType, Map<EnergyType, Integer>> getEnergy() {
        return energy;
    }

    public void setEnergy(Map<UnimonEvolveType, Map<EnergyType, Integer>> energy) {
        this.energy = energy;
    }
}


