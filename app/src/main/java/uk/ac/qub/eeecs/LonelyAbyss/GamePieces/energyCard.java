package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;
import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Created by jdevl on 25/11/2016.
 */

public class energyCard extends Card {

    private int phantomUnimon, demonUnimon, dragonUnimon, spiritUnimon;

    public energyCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen,
                      Bitmap cardGeneric, Bitmap icon, Bitmap background, String name, String type,
                      int phantomUnimon, int demonUnimon, int dragonUnimon, int spiritUnimon) {
        super(x, y, width, height, bitmap, gameScreen, icon, background, name, type);
        this.phantomUnimon = phantomUnimon;
        this.demonUnimon = demonUnimon;
        this.dragonUnimon = dragonUnimon;
        this.spiritUnimon = spiritUnimon;
    }

    public int getPhantomUnimon() {
        return phantomUnimon;
    }

    public void setPhantomUnimon(int phantomUnimon) {
        this.phantomUnimon = phantomUnimon;
    }

    public int getDemonUnimon() {
        return demonUnimon;
    }

    public void setDemonUnimon(int demonUnimon) {
        this.demonUnimon = demonUnimon;
    }

    public int getDragonUnimon() {
        return dragonUnimon;
    }

    public void setDragonUnimon(int dragonUnimon) {
        this.dragonUnimon = dragonUnimon;
    }

    public int getSpiritUnimon() {
        return spiritUnimon;
    }

    public void setSpiritUnimon(int spiritUnimon) {
        this.spiritUnimon = spiritUnimon;
    }
}
