package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Created by James on 19/11/2016.
 */

public class Grid extends GameObject {
    private boolean hidden; //true if the grid has not been selected, false if the grid has

    public Grid(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen, boolean hidden) {
        super(x, y, width, height, bitmap, gameScreen);
        this.hidden = hidden;
    }




}
