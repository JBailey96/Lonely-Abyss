package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

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
 * Created by Patrick on 21/11/2016.
 */

public class DeckManagement extends GameScreen {
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;
    GameObject[] gObject;
    Bitmap[] unimonCard;
    ArrayList<String> cardRef;

    protected final int numCardsDisplayed = 3;

    //Constructor for DeckManagement. Sets viewports and calls to methods
    public DeckManagement(Game game) {
        super("DeckManagement", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        loadCardBitmaps();
        createCard();

    }

    //For response to a touch event
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
    }

    // Sets up an array of type Bitmap which gets filled with the token of the Bitmaps using a foreach loop and counter.
    public void createCard() {
        unimonCard = new Bitmap[3];
        int uniCard = 0;
        for(String item: cardRef) {
            Bitmap b = (selectBitmap(item));
                unimonCard[uniCard] = b;
                uniCard++;

        }
        //Creates an Array of size numCardsDisplayed and of type GameObject
        gObject = new GameObject[numCardsDisplayed];

        GameObject g;
        int counter = 0;
        //Runs a for loop, creating a instance of GameObject, Which sets the cards location on the screen.
        //Uses modules as the check within the if Statement for positioning.
        //Adds all the gameObject to the gObject array.
        for (int i = 0; i < numCardsDisplayed; i++) {
            if (i == 0) {
                g = new GameObject(mLayerViewport.x, mLayerViewport.y, 450, 800, unimonCard[counter], this);
                gObject[i] = g;
            } else {
                if (i % 2 == 0) {
                    g = new GameObject((mLayerViewport.getLeft() + mLayerViewport.x / 3), mLayerViewport.y, 450, 800, unimonCard[counter + 2], this);
                    gObject[i] = g;
                } else {
                    g = new GameObject((mLayerViewport.getRight() - mLayerViewport.x / 3), mLayerViewport.y, 450, 800, unimonCard[counter + 1], this);
                    gObject[i] = g;
                }
            }
        }

    }


    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mScreenViewport.toRect());

        for (GameObject g : gObject) {
            g.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

    }

    //Loads the bitmaps from the asset manager. Adds the token of the Bitmap to an ArrayList
    public void loadCardBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("UC1","img/Cards/Earth Dragon.png");
        getGame().getAssetManager().loadAndAddBitmap("UC2","img/Cards/White Wizard.png");
        getGame().getAssetManager().loadAndAddBitmap("UC3","img/Cards/Knight of Fire - Spirit.png");
        cardRef = new ArrayList<>();
        cardRef.add("UC1");
        cardRef.add("UC2");
        cardRef.add("UC3");

    }
    // Will be passed a token to select bitmap. This Bitmap will then be returned.
    private Bitmap selectBitmap(String cardIndex) {

        return getGame().getAssetManager().getBitmap(cardIndex);

    }
}
