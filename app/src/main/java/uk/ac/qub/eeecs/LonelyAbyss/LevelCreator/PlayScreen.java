package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.KeyEvent;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by Kyle on 22/11/2016.
 */

public class PlayScreen extends GameScreen {

    public static Random randomCard = new Random(); //used to generate random cards for the player's hand

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewPort;
    protected Input mInput;

    List<TouchEvent> touchEvents;

    List<GameObject> playAreaOverviewCards = new ArrayList<GameObject>(); //the play area cards displayed.

    public enum PlayAreaState { //the state of the player area, decides what to draw
        NONE, ACTIVEUNIMON, ENERGYCARD
    }

    private PlayAreaState currentState = PlayAreaState.NONE;

    //this will be replaced by the player's battlesetup class being called - but just for clarity
    private int numHandCards = 5;
    private int numBenchCards = 3;
    private int numPrizeCards = 3;

    UnimonCard testCard; //testCard used to test the active unimon view

    protected LayerViewport activeUnimonLayerViewPort;

    public PlayScreen(Game game) {
        super("PlayScreen", game);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewPort = new LayerViewport(mScreenViewport.width/2, mScreenViewport.height/2, mScreenViewport.width/2, mScreenViewport.height/2);
        handlePlayAreaBitmaps();
        generateCards();
        loadTestCard();
    }

    /*
    Method that creates and places all bitmaps to their respective places.
     */

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();

        if (currentState == PlayAreaState.ACTIVEUNIMON) {

        } else {
            for (GameObject card : playAreaOverviewCards) {
                card.update(elapsedTime);
            }
        }

        touchButton(touchEvents);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mScreenViewport.toRect());

        for (GameObject card : playAreaOverviewCards) {
            card.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        if (currentState == PlayAreaState.ACTIVEUNIMON) {
            Paint myPaint = new Paint();

            Rect paintRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height);
            myPaint.setAlpha(200);

            graphics2D.drawBitmap(selectBitmap("BLACK"), null, paintRect, myPaint);

            testCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }
    }

    public void generateCards() {
        generateHandCards();
        generateDeckCard();
        generateBenchCard();
        generatePrizeCards();
        generateActiveUnimon();
        generateGraveyardCards();
    }

    //generate the cards that go into the user's hand
    public void generateHandCards() {
        GameObject handCard;
        float width;
        float x;
        float height;
        float y;

        for (int i = 0; i < numHandCards; i++) {
            //set up the dimensions for the card to be drawn to
            width = mScreenViewport.width/10;
            x = (width/2 * (i+1))+((width*i)*0.75f)+mScreenViewport.width/6;
            height = mScreenViewport.height/4;
            y = height/2;

            handCard = new GameObject(x, y, width, height, selectBitmap("StaminaCard") , this); //create the card
            playAreaOverviewCards.add(handCard);
        }
    }

    public void generateDeckCard() {
        float width = mScreenViewport.width/10;
        float x = mScreenViewport.width-mScreenViewport.width/10;
        float height = mScreenViewport.height/4;
        float y = height/2+mScreenViewport.height/9;

        playAreaOverviewCards.add(new GameObject(x, y, width, height, selectBitmap("Deck"), this));
    }

    public void generateBenchCard() {
        /*
        For loop to run through the array to generate the bench bitmaps.
         */
        float width;
        float x;
        float height;
        float y;

        for (int i = 0; i < numBenchCards; i++) {
            width = mScreenViewport.width/10;
            x = (width/2 * (i+1))+((width*i)*0.6f) + mScreenViewport.width/40;
            height = mScreenViewport.height/4;
            y = mScreenViewport.height/2;

            playAreaOverviewCards.add(new GameObject(x, y, width, height, selectBitmap("BenchUnimon"), this));
        }

    }

    public void generatePrizeCards() {
        float width;
        float x;
        float height;
        float y;

        for (int i = 0; i < numPrizeCards; i++) {
            width = mScreenViewport.width/10;
            height = mScreenViewport.height/4;
            x = mScreenViewport.width-width;
            y = mScreenViewport.height-((height/2 * (i+1))+((height*i)*0.2f));

            Bitmap rotatedImage = rotateBitmap("PrizeCard"); //rotate the prize card's bitmap 90 degrees clockwise
            playAreaOverviewCards.add(new GameObject(x, y, height, width, rotatedImage, this));
        }
    }

    //rotate a bitmap 90 degrees clockwise
    public Bitmap rotateBitmap(String bitmapKey) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(selectBitmap(bitmapKey), 0, 0, selectBitmap(bitmapKey).getWidth(), selectBitmap(bitmapKey).getHeight(), matrix, true);
    }

    //generate the dimensions for the active unimon
    public void generateActiveUnimon() {
         /*
        Active Unimon (Dragon King) bitmap
         */
        float width = mScreenViewport.width/4;
        float x = mScreenViewport.width/2;
        float height = mScreenViewport.height/1.5f;
        float y = mScreenViewport.height/2 + mScreenViewport.height/8;
        playAreaOverviewCards.add(new GameObject(x, y, width, height, selectBitmap("UnimonCard"), this));
    }

    public void generateGraveyardCards() {
        float width = mScreenViewport.width/10;
        float x = width/2 + mScreenViewport.width/40;
        float height = mScreenViewport.height/4;
        float y = mScreenViewport.height-mScreenViewport.height/40 - height/2;

        playAreaOverviewCards.add(new GameObject(x, y, width, height, selectBitmap("Graveyard"), this));
    }

    /*
    Loads all of the bitmaps onto the play area.
     */
    public void handlePlayAreaBitmaps() {
        loadPlayAreaBitmaps();
//        addPlayAreaBitmaps();
    }

    private void loadPlayAreaBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("UnimonCard", "img/PlayArea/UnimonCard.png");
        getGame().getAssetManager().loadAndAddBitmap("BenchUnimon", "img/PlayArea/BenchUnimon.png");
        getGame().getAssetManager().loadAndAddBitmap("HealthCard", "img/PlayArea/HealthCard.png");
        getGame().getAssetManager().loadAndAddBitmap("StaminaCard", "img/PlayArea/StaminaCard.png");
        getGame().getAssetManager().loadAndAddBitmap("ManaCard", "img/PlayArea/ManaCard.png");
        getGame().getAssetManager().loadAndAddBitmap("Deck", "img/PlayArea/Deck.png");
        getGame().getAssetManager().loadAndAddBitmap("PrizeCard", "img/PlayArea/PrizeCard.png");
        getGame().getAssetManager().loadAndAddBitmap("DragonKing", "img/PlayArea/Dragon_King.png");
        getGame().getAssetManager().loadAndAddBitmap("Graveyard", "img/PlayArea/Graveyard.png");
    }

//    private void addPlayAreaBitmaps() {
//        handCards.add("UnimonCard");
//        handCards.add("HealthCard");
//        handCards.add("StaminaCard");
//        handCards.add("ManaCard");
//        handCards.add("BenchUnimon");
//        handCards.add("Deck");
//        handCards.add("PrizeCard");
//        handCards.add("DragonKing");
//        handCards.add("Graveyard");
//    }

    //useful method for retrieving a specific bitmap from the asset manager using the string key
    private Bitmap selectBitmap(String cardKey) {
        return getGame().getAssetManager().getBitmap(cardKey);
    }

    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                for (GameObject card: playAreaOverviewCards) { //(for now) if the user presses any of the cards
                    if (card.getBound().contains((int) t.x, (int) mLayerViewPort.getTop()-t.y)) {
                        currentState = PlayAreaState.ACTIVEUNIMON;
                    } else if (currentState == PlayAreaState.ACTIVEUNIMON) {
                        if (!(testCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop()-t.y))) {
                            currentState = PlayAreaState.NONE;
                        }
                    }
                }
            }
        }
    }

    public void loadTestCard() {
    getGame().getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Earth Dragon.png");
        getGame().getAssetManager().loadAndAddBitmap("BLACK", "img/Particles/black.png");
        Bitmap cardImage = selectBitmap("CARD");
        testCard = new UnimonCard((mScreenViewport.width/2), (mScreenViewport.height/2), (mScreenViewport.width/3), (int)(mScreenViewport.height/1.3f), cardImage, this,
                "0", null, null, null, "Earth Dragon",
                UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                20 ,30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);
    }

    /*
    This is to draw a random card from the deck that will then be placed into the hand. Messed around with the code and could not get it working :(
     */

    /*
    //@Override
    public void onKeyUp(int keyCode, KeyEvent event) {
        if (event.keyChar == 'k') {
            Random rn = new Random();
           testingGame.add(new GameObject(mLayerViewport.x / 2 + 225 * numCardsDisplayed, mLayerViewport.y / 3, 150, 300, numOfCards[rn.nextInt(4)], this));
            numCardsDisplayed++;
        }
    }
    */
}
