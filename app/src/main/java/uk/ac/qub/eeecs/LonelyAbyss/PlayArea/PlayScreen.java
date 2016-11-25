package uk.ac.qub.eeecs.LonelyAbyss.PlayArea;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;


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
 * Created by Kyle on 22/11/2016.
 */

public class PlayScreen extends GameScreen {

//    public static Random randomCard = new Random();

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;
    GameObject[] gameObject;
    Bitmap[] numOfCards;
    ArrayList<String> handCards;

    protected final int numCardsDisplayed = 5;

    public PlayScreen(Game game) {
        super("PlayScreen", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        loadPlayAreaBitmaps();
        handCreation();
    }

    public void handCreation() {
        numOfCards = new Bitmap[5];
        int handCard = 0;
        for(String item: handCards) {
            Bitmap a = (selectBitmap(item));
            numOfCards[handCard] = a;
            handCard++;
        }
        gameObject = new GameObject[numCardsDisplayed];

        GameObject g;
        int counter = 0;

        //Runs a for loop, creating a instance of GameObject, Which sets the cards location on the screen.
        //Uses modules as the check within the if Statement for positioning.
        //Adds all the gameObject to the gObject array.
        for (int i = 0; i < numCardsDisplayed; i++) {
            g = new GameObject(mLayerViewport.x / 2 + 225 * i, mLayerViewport.y / 3, 150, 300, numOfCards[counter], this);
            gameObject[i] = g;
        }



    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        graphics2D.clipRect(mScreenViewport.toRect());

        for (GameObject g : gameObject) {
            g.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    public void loadPlayAreaBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("UnimonCard", "img/PlayArea/UnimonCard.png");
        getGame().getAssetManager().loadAndAddBitmap("BenchUnimon", "img/PlayArea/BenchUnimon");
        handCards = new ArrayList<>();
        handCards.add("UnimonCard");
        handCards.add("UnimonCard");
        handCards.add("UnimonCard");
        handCards.add("UnimonCard");
        handCards.add("UnimonCard");
    }

    private Bitmap selectBitmap(String cardIndex) {

        return getGame().getAssetManager().getBitmap(cardIndex);

    }
}
