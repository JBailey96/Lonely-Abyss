package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.widget.TextView;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;


/**
 * Created by jdevl on 19/11/2016.
 */

public class Card extends GameObject {
    private Bitmap icon, background;
    private String name, type;


    public Card(float x, float y, float width, float height, Bitmap bitmap,
                GameScreen gameScreen, Bitmap icon, Bitmap background,
                String name, String type) {
        super(x, y, width, height, bitmap, gameScreen);

        this.icon = icon;
        this.background = background;
        this.name = name;
        this.type = type;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    Card card1 = new Card(10, 10, 10, 10, mBitmap, mGameScreen, icon, background, "Professor", "Unimon");

   // AssetManager assetManager = getBackground().getAssets();
   // Bitmap background = setBackground(assetManager, "img/Cards/genericCard.PNG");


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        graphics2D.drawBitmap(card1.getBackground(), null, null, null);
        graphics2D.drawBitmap(card1.getIcon(), null, null, null);
        graphics2D.drawText(card1.getName(),100, 50, null);
        graphics2D.drawText(card1.getType(),200, 50, null);


    }
}