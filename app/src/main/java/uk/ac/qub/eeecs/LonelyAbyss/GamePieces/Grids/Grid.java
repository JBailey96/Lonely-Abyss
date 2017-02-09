package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;


/**
 * Created by James on 19/11/2016.
 */

public class Grid extends GameObject {
    private boolean hidden; //true if the grid has not been selected, false if the grid has
    private Bitmap realBitmap; //the actual bitmap displayed after the grid has been touched
    private boolean terminus; //whether the grid square is an 'end' grid
    private GridType type; //type of grid square e.g battle, location, start


    public Grid(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen, boolean hidden, Bitmap realBitmap, boolean terminus, GridType type) {
        super(x, y, width, height, bitmap, gameScreen);
        this.hidden = hidden;
        this.realBitmap = realBitmap;
        this.terminus = terminus;
        this.type = type;

        if (!hidden) { //if the grid is not hidden
            reveal(); //reveal the grid square
        }
    }

    //called when the grid has been touched or has been selected as not hidden
    public void reveal() {
        setHidden(false); //Change the hidden flag of the grid
        this.mBitmap = realBitmap; //set the actual bitmap of the grid to be the 'actual' bitmap
    }

    public boolean getHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean getTerminus() {
        return this.terminus;
    }

    public GridType getType() {
        return this.type;
    }




}