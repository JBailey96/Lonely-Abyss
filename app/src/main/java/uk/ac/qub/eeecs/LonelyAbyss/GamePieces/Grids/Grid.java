package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;


/**
 * Created by James on 19/11/2016.
 */

public class Grid extends GameObject {
    private boolean gridSelected; //false if the grid has not been selected, true if the grid has
    private Bitmap realBitmap; //the actual bitmap displayed after the grid has been touched
    private GridType type; //type of grid square e.g battle, location, start
    private boolean active; //true - not yet selected, user can be transitioned upon touching this grid square.


    public Grid(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen, boolean gridSelected, Bitmap realBitmap, GridType type) {
        super(x, y, width, height, bitmap, gameScreen);
        this.gridSelected = gridSelected;
        this.realBitmap = realBitmap;
        this.type = type;
        this.active = true;

        if (gridSelected) { //if the grid is not hidden
            reveal(); //reveal the grid square
        }
    }

    //called when the grid has been touched or has been selected as not hidden
    public void reveal() {
        setGridSelected(true); //Change the hidden flag of the grid
        this.mBitmap = realBitmap; //set the actual bitmap of the grid to be the 'actual' bitmap
    }

    public boolean isGridSelected() {
        return this.gridSelected;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setGridSelected(boolean gridSelected) {
        this.gridSelected = gridSelected;
    }

    public GridType getType() {
        return this.type;
    }

}
