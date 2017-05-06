package uk.ac.qub.eeecs.LonelyAbyss.DiscontinuedCode;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
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
    UnimonCard testCard;

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        //touchActiveUnimon(t);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap,
        testCard.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }

    public CardTest(Game game) {
        super("CardTest", game);

        loadCardBitmaps();


        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        backgroundRect = new Rect(0, 0, game.getScreenWidth(),  game.getScreenHeight());
        background = mGame.getAssetManager().getBitmap("BACKGROUND");

        Bitmap cardImage = mGame.getAssetManager().getBitmap("CARD");

        testCard = new UnimonCard((mScreenViewport.width/2), (mScreenViewport.height/2), (mScreenViewport.width/3), (int)(mScreenViewport.height/1.5f), cardImage, this,
                "0", null, null, null, "Earth Dragon",
                UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
        20 ,30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);

    }

    public void loadCardBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("BACKGROUND", "img/MenuImages/MenuBackground2.png");
        getGame().getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Earth Dragon.png");
    }
}
