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

public class energyCard extends Card {

    /**
     * Enum class of card Energy types
     */
    public enum  EnergyType{
        MANA,STAMINA,HEALTH
    }

    /**
     * Enum class of card Applied types
     */
    public enum AppliedType{
        SPIRITUNIMON,PHANTOMUNIMON,DEMONUNIMON,UNIQUEUNIMON
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
     * What value of energy based on evolution type
     */
    private Map<AppliedType,Integer>energy;

    public energyCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen,String ID,
                      Bitmap backGround, Bitmap icon, String name,EnergyType type,Map<AppliedType,Integer>energy,String description) {
        super(x, y, width, height, bitmap, gameScreen,ID,name,description);
        this.backGround = backGround;
        this.icon = icon;
        this.type = type;
        this.energy = new HashMap<AppliedType, Integer>(energy);
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

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

    }
}


