package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import android.graphics.Bitmap;
import android.graphics.Rect;

import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;


/**
 * Created by James on 19/11/2016.
 */

public class Grid extends GameObject {
    private boolean hidden; //true if the grid has not been selected, false if the grid has
    private Bitmap realBitmap; //the actual bitmap displayed after the grid has been touched
    private boolean terminus; //whether the grid square is an 'end' grid

    public Grid(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen, boolean hidden, Bitmap realBitmap, boolean terminus) {
        super(x, y, width, height, bitmap, gameScreen);
        this.hidden = hidden;
        this.realBitmap = realBitmap;
        this.terminus = terminus;

        if (!hidden) { //if the grid is hidden
            reveal();
        }
    }


    //called when the grid has been touched and is hidden
    public void reveal() {
        setHidden(false);
        this.mBitmap = realBitmap;
    }

    public boolean getHidden() {
        return this.hidden;
    }

    public boolean getTerminus() { return this.terminus; }

    public void setHidden(boolean b) {
        this.hidden = b;
    }



}
