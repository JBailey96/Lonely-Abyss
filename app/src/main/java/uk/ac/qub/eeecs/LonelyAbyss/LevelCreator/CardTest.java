package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by jdevl on 27/01/2017.
 */

public class CardTest extends GameScreen {

    LayerViewport mLayerViewport;
    ScreenViewport mScreenViewport;
    Rect backgroundRect;
    Bitmap background;
    protected Input mInput;
    List<TouchEvent> touchEvents;

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
       // graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap,

    }

    public CardTest(Game game) {
        super("CardTest", game);

        //loadCardBitmaps();


        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        //backgroundRect = new Rect(0, 0, game.getScreenWidth(),  game.getScreenHeight());
       // background = mGame.getAssetManager().getBitmap("BACKGROUND");

    }

    public void loadCardBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("BACKGROUND", "img/Cards/dude.gif");

    }
}
