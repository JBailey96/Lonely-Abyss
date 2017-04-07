package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by kyle on 08/03/2017.
 */

public class PlayOverviewState extends State {
    List<GameObject> playAreaOverviewCards = new ArrayList<GameObject>(); //the play area cards displayed.
    Card[] handCards; //the bench area cards displayed.
    Stack<Card> deck;
    UnimonCard[] benchCards;
    UnimonCard[] prizeCards;
    GameObject deckCard; //card used to represent the top of the deck

    UnimonCard activeUnimonCard; //the unimon card that the player has currently active
    GameObject opponentButton; // the button that will show the opponent's screen

    //this will be replaced by the player's battlesetup class being called - but just for clarity
    private int numHandCards = 5;
    private int numBenchCards = 3;
    private int numPrizeCards = 3;

    public PlayOverviewState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadPlayAreaBitmaps();

        activeUnimonCard = battleSetup.getActiveCard().copyUnimonCard();
        deck = battleSetup.getPlayAreaDeck();
        handCards = new Card[numHandCards];
        benchCards = battleSetup.getBenchCards();
        prizeCards = battleSetup.getPrizeCards();

        generateCards();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            updateOverviewCards(elapsedTime);
            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            touchButton(touchEvents);
        }
    }

    //update all the overview cards.
    public void updateOverviewCards(ElapsedTime elapsedTime) {
        for (GameObject overviewCard : playAreaOverviewCards) {
            overviewCard.update(elapsedTime);
        }

        for (Card handCard : handCards) {
            handCard.update(elapsedTime);
        }

        deckCard.update(elapsedTime);
        activeUnimonCard.update(elapsedTime);
        opponentButton.update(elapsedTime);

    }

    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                touchOpponentButton(t);
                touchActiveUnimon(t);

            }
        }
    }

    //checks whether the active unimon has been touched
    public void touchActiveUnimon(TouchEvent t) {
        if ((activeUnimonCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
           playScreen.getActiveUnimonState().active = true;
            //playScreen.getInProgActiveUnimonState().active = true;
        }
    }

    //checks whether the opponent button has been touched
    public void touchOpponentButton(TouchEvent t) {
        if ((opponentButton.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
            playScreen.getOpponentScreen().active = true;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (active) {
            drawCards(elapsedTime, graphics2D);
        }
    }

    //draw all the cards
    public void drawCards(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (GameObject card : playAreaOverviewCards) {
            card.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        for (Card handCard : handCards) {
            handCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        for (UnimonCard benchCard : benchCards) {
            benchCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        for (UnimonCard prizeCard : prizeCards) {
            prizeCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        deckCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        activeUnimonCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        opponentButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //generate all the overview cards
    public void generateCards() {
        generateShowOpponentsScreenButton();
        generateHandCards();
        generateDeckCard();
        generateBenchCard();
        generatePrizeCards();
        generateActiveUnimon();
        generateGraveyardCards();
        //generateShowOpponentsScreenButton();
    }


    //generate the cards that go into the user's hand
    public void generateHandCards() {
        float width;
        float x;
        float height;
        float y;

        for (int i = 0; i < numHandCards; i++) {

            //set up the dimensions for the card to be drawn to
            width = mScreenViewport.width / 10;
            x = (width / 2 * (i + 1)) + ((width * i) * 0.75f) + mScreenViewport.width / 6;
            height = mScreenViewport.height / 4;
            y = height / 2;

            Card handCard = deck.pop();
            handCard.setmGameScreen(mGameScreen);

            BoundingBox mBound = new BoundingBox();
            mBound.x = x;
            mBound.y = y;
            mBound.halfWidth = width/2;
            mBound.halfHeight = height/2;

            handCard.setPosition(new Vector2(x, y));
            handCard.setmBound(mBound);

            handCards[i] = handCard;
        }
    }

    //generate the card used to represent the top of the deck
    public void generateDeckCard() {
        float width = mScreenViewport.width / 10;
        float x = mScreenViewport.width - mScreenViewport.width / 10;
        float height = mScreenViewport.height / 4;
        float y = height / 2 + mScreenViewport.height / 9;

        deckCard = new GameObject(x, y, width, height, selectBitmap("Deck"), mGameScreen);
    }

    //generate the player's bench cards
    public void generateBenchCard() {
        float width;
        float x;
        float height;
        float y;

         /*
        For loop to run through the array to generate the bench bitmaps.
         */
        for (int i = 0; i < numBenchCards; i++) {
            width = mScreenViewport.width / 10;
            height = mScreenViewport.height / 4;
            x = (width / 2 * (i + 1)) + ((width * i) * 0.6f) + mScreenViewport.width / 40;
            y = mScreenViewport.height / 2;

            UnimonCard benchCard = benchCards[i];
            benchCard.setmGameScreen(mGameScreen);

            BoundingBox mBound = new BoundingBox();
            mBound.x = x;
            mBound.y = y;
            mBound.halfWidth = width/2;
            mBound.halfHeight = height/2;

            benchCard.setPosition(new Vector2(x, y));
            benchCard.setmBound(mBound);

            benchCards[i] = benchCard;
        }
    }

    //generate the player's current prize cards.
    public void generatePrizeCards() {
        float width;
        float x;
        float height;
        float y;

        for (int i = 0; i < numPrizeCards; i++) {
            width = mScreenViewport.width / 10;
            height = mScreenViewport.height / 4;
            x = mScreenViewport.width - width;
            y = mScreenViewport.height - ((height / 2 * (i + 1)) + ((height * i) * 0.2f));

            UnimonCard prizeCard = prizeCards[i];
            prizeCard.setmGameScreen(mGameScreen);

            BoundingBox mBound = new BoundingBox();
            mBound.x = x;
            mBound.y = y;
            mBound.halfWidth = width/2;
            mBound.halfHeight = height/2;

            prizeCard.setPosition(new Vector2(x, y));
            prizeCard.setmBound(mBound);

            prizeCards[i] = prizeCard;
        }
    }

    //generate the dimensions for the active unimon
    public void generateActiveUnimon() {
        float width = mScreenViewport.width / 4;
        float x = mScreenViewport.width / 2;
        float height = mScreenViewport.height / 1.7f;
        float y = ((mScreenViewport.height / 2) + (mScreenViewport.height/8));
        activeUnimonCard.setmGameScreen(mGameScreen);

        BoundingBox mBound = new BoundingBox();
        mBound.x = x;
        mBound.y = y;
        mBound.halfWidth = width/2;
        mBound.halfHeight = height/2;

        activeUnimonCard.setPosition(new Vector2(x, y));
        activeUnimonCard.setmBound(mBound);
    }

    // generate the dimensions for the button that will show the opponent's screen
    public void generateShowOpponentsScreenButton() {
        float width = mScreenViewport.width / 7;
        float x = width / 2 + mScreenViewport.width / 5.6f;
        float height = mScreenViewport.height / 8;
        float y = mScreenViewport.height - mScreenViewport.height / 30 - height / 2;
        opponentButton = new GameObject(x, y, width, height, selectBitmap("Opponent"), mGameScreen);
    }

    //generate the cards not currently active in the game - graveyard
    public void generateGraveyardCards() {
        float width = mScreenViewport.width / 10;
        float height = mScreenViewport.height / 4;
        float x = width / 2 + mScreenViewport.width / 40;
        float y = mScreenViewport.height - mScreenViewport.height / 40 - height / 2;

        playAreaOverviewCards.add(new GameObject(x, y, width, height, selectBitmap("Graveyard"), mGameScreen));
    }

    //rotate a bitmap 90 degrees clockwise
    public Bitmap rotateBitmap(String bitmapKey) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(selectBitmap(bitmapKey), 0, 0, selectBitmap(bitmapKey).getWidth(), selectBitmap(bitmapKey).getHeight(), matrix, true);
    }

    /*
 Loads all of the bitmaps onto the play area.
  */
    private void loadPlayAreaBitmaps() {
        mGame.getAssetManager().loadAndAddBitmap("UnimonCard", "img/PlayArea/UnimonCard.png");
        mGame.getAssetManager().loadAndAddBitmap("BenchUnimon", "img/PlayArea/BenchUnimon.png");
        mGame.getAssetManager().loadAndAddBitmap("HealthCard", "img/PlayArea/HealthCard.png");
        mGame.getAssetManager().loadAndAddBitmap("StaminaCard", "img/PlayArea/StaminaCard.png");
        mGame.getAssetManager().loadAndAddBitmap("ManaCard", "img/PlayArea/ManaCard.png");
        mGame.getAssetManager().loadAndAddBitmap("Deck", "img/PlayArea/Deck.png");
        mGame.getAssetManager().loadAndAddBitmap("PrizeCard", "img/PlayArea/PrizeCard.png");
        mGame.getAssetManager().loadAndAddBitmap("DragonKing", "img/PlayArea/Dragon_King.png");
        mGame.getAssetManager().loadAndAddBitmap("Graveyard", "img/PlayArea/Graveyard.png");
        mGame.getAssetManager().loadAndAddBitmap("Opponent", "img/PlayScreenButtons/opponent_screen.png");
    }




}
