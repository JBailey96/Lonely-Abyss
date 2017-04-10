package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;


/**
 * Created by jdevl on 19/11/2016.
 */

public abstract class Card extends GameObject {
    protected String name;
    protected String description;
    protected boolean revealed; // true - card is visible and 'in play', false - only the back of the card is visible.
    protected Bitmap icon; //icon on the top left of the card.
    protected Container container; //the container where the card is.
    protected float x, y;
    protected Vector2 velocity;
    protected String ID;




    /**
     * This is a constructor method for the card object.
     * @param ID - to uniquely identify a card
     * @param name - the name of the card
     * @param description - description of the card
     */
    public Card(float x, float y, float width, float height, Bitmap bitmap,
                GameScreen gameScreen, String ID, String name,String description, boolean revealed, Container container) {
        super(x, y, width, height, bitmap, gameScreen);
        this.name = name;
        this.description = description;
        this.revealed = revealed;
        this.container = container;
        this.x = x;
        this.y = y;
        this.ID = ID;
    }

    //requires all subclasses to have a deep copy method
    public abstract Card copy();

    /**
     * This method returns the unique ID assigned to a specific card
     * @return - the unique ID
     */
    public String getID() {
        return ID;
    }

    /**
     * This method sets the ID of a card
     * @param ID - Unique ID of a card
     */
    public void setID(String ID){this.ID = ID;}

    /**
     * This method returns the name assigned to a specific card
     * @return - name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of a card
     * @param name - name of card
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns a description of a specific card
     * @return - description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method sets the ID of a card
     * @param description - description of the card
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Container getContainer(){
        return container;
    }

    public void setContainer ( Container newContainer){
        this.container = newContainer;
    }

    public void update(ElapsedTime elapsedTime) {

    }

    public void moveCard(float targetX, float targetY){

        setPosition(targetX, targetY);

        /*velocity.x = 50;
        velocity.y = 50;
        float distanceX = this.x - targetX;
        float distanceY = this.y - targetY;
        this.x = this.x + targetX;
        this.y = this.y + targetY;
       // setPosition(this.x, this.y);*/

    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }


    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

}