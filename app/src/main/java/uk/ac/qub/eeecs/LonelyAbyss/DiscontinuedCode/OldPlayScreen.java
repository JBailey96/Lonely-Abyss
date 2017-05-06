package uk.ac.qub.eeecs.LonelyAbyss.DiscontinuedCode;
import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class OldPlayScreen extends GameScreen {
    public static Random randomCard = new Random();
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;
    GameObject[] gameObject;
    Bitmap[] numOfCards;
    ArrayList<String> handCards;
    List<GameObject> testingGame;
    protected int numCardsDisplayed = 5;
    public OldPlayScreen(Game game) {
        super("PlayScreen", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        loadPlayAreaBitmaps();
        handCreation();
    }
    /*
    Method that creates and places all bitmaps to their respective places.
     */
    public void handCreation() {
        numOfCards = new Bitmap[9];
        int handCard = 0;
        for(String item: handCards) {
            Bitmap a = (selectBitmap(item));
            numOfCards[handCard] = a;
            handCard++;
        }
        //gameObject = new GameObject[numCardsDisplayed];
        testingGame = new ArrayList<GameObject>();
        GameObject g;
        int counter = 0;
        Random rn = new Random();
        /*
        For loop to run through the array to then select randomly generated cards that will be placed into the user's hand.
         */
        for (int i = 0; i < numCardsDisplayed; i++) {
            g = new GameObject(mLayerViewport.x / 2 + 180 * i, mLayerViewport.y / 3, 150, 300, numOfCards[rn.nextInt(4)], this);
            testingGame.add(g);
        }
        /*
        Deck bitmap.
         */
        testingGame.add(new GameObject(mLayerViewport.getWidth() - 150, mLayerViewport.y / 3, 150, 300, numOfCards[5], this));
        /*
        For loop to run through the array to generate the bench bitmaps.
         */
        for (int i = 0; i < 3; i++) {
            testingGame.add(new GameObject(mLayerViewport.getLeft() + 90 + 180 * i, mLayerViewport.y / 2 + 220, 150, 300, numOfCards[4], this));
        }
        /*
        For loop to run through the array to generate the prize card bitmaps.
         */
        for (int i = 0; i < 3; i++) {
            testingGame.add(new GameObject(mLayerViewport.getRight() - 150, mLayerViewport.getTop() - 100 - 180 * i, 300, 150, numOfCards[6], this));
        }
        /*
        Active Unimon (Dragon King) bitmap
         */
        testingGame.add(new GameObject(mLayerViewport.getWidth() /2 , mLayerViewport.y / 2 + 450, 400, 700, numOfCards[7], this));
        /*
        Graveyard bitmap.
         */
        testingGame.add(new GameObject(mLayerViewport.getLeft() + 190, mLayerViewport.getTop() - 100, 300, 150, numOfCards[8], this));
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
        for (GameObject g : testingGame) {
            g.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }
    /*
    Loads all of the bitmaps onto the play area.
     */
    public void loadPlayAreaBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("UnimonCard", "img/PlayArea/UnimonCard.png");
        getGame().getAssetManager().loadAndAddBitmap("BenchUnimon", "img/PlayArea/BenchUnimon.png");
        getGame().getAssetManager().loadAndAddBitmap("HealthCard", "img/PlayArea/HealthCard.png");
        getGame().getAssetManager().loadAndAddBitmap("StaminaCard", "img/PlayArea/StaminaCard.png");
        getGame().getAssetManager().loadAndAddBitmap("ManaCard", "img/PlayArea/ManaCard.png");
        getGame().getAssetManager().loadAndAddBitmap("Deck", "img/PlayArea/Deck.png");
        getGame().getAssetManager().loadAndAddBitmap("PrizeCard", "img/PlayArea/PrizeCard.png");
        getGame().getAssetManager().loadAndAddBitmap("DragonKing", "img/PlayArea/Dragon_King.png");
        getGame().getAssetManager().loadAndAddBitmap("Graveyard", "img/PlayArea/Graveyard.png");
        handCards = new ArrayList<>();
        handCards.add("UnimonCard");
        handCards.add("HealthCard");
        handCards.add("StaminaCard");
        handCards.add("ManaCard");
        handCards.add("BenchUnimon");
        handCards.add("Deck");
        handCards.add("PrizeCard");
        handCards.add("DragonKing");
        handCards.add("Graveyard");
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