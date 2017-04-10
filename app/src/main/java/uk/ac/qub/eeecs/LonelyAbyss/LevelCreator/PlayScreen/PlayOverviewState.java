package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by kyle on 08/03/2017.
 */

public class PlayOverviewState extends State {
    protected final int numHandCards = 5;
    protected final int numBenchCards = 3;
    protected final int numPrizeCards = 3;

    private Card[] handCards; //the bench area cards displayed.
    private Stack<Card> deck; //
    private UnimonCard[] benchCards;
    private UnimonCard[] prizeCards;
    ReleaseButton deckButton; //button when pressed draws a card to the player's hand from the player's deck
    ReleaseButton graveyardButton; //button when pressed shows the unimoncards that have been knocked out

    private UnimonCard activeUnimonCard; //the unimon card that the player has currently active

    private GameObject opponentButton; // the button that will show the opponent's screen


    public PlayOverviewState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadPlayAreaBitmaps();
        initialiseCards(battleSetup);
        generateButtons();
        generateCards();
    }


    //James Bailey 40156063
    //Gets the variables needed from the player's battlesetup in order to draw the cards onto the play area screen
    public void initialiseCards(BattleSetup battleSetup) {
        activeUnimonCard = battleSetup.getActiveCard().copy();
        deck = battleSetup.getPlayAreaDeck();
        handCards = new Card[numHandCards];
        benchCards = battleSetup.getBenchCards();
        prizeCards = battleSetup.getPrizeCards();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            updateCards(elapsedTime);
            updateButtons(elapsedTime);
            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            touchButton(touchEvents);
        }
    }

    //James Bailey 40156063
    //update all the cards
    public void updateCards(ElapsedTime elapsedTime) {
        for (Card handCard : handCards) {
            handCard.update(elapsedTime);
        }

        activeUnimonCard.update(elapsedTime);

    }

    //update all the buttons
    public void updateButtons(ElapsedTime elapsedTime) {
        deckButton.update(elapsedTime);
        opponentButton.update(elapsedTime);
        graveyardButton.update(elapsedTime);
    }

    //checks for touch evetns on the buttons
    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                touchActiveUnimon(t);
                touchDeckButton(t);
                touchGraveyardButton(t);
                touchOpponentButton(t);
            }
        }
    }

    //James Bailey 40156063
    //checks whether the active unimon has been touched and activates the active unimon state
    public void touchActiveUnimon(TouchEvent t) {
        if ((activeUnimonCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
            playScreen.getActiveUnimonState().active = true;
        }
    }

    //checks whether the opponent button has been touched
    public void touchOpponentButton(TouchEvent t) {
        if ((opponentButton.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
            playScreen.getOpponentState().active = true;
        }
    }

    //checks whether the deck button has been touched and takes a card from the deck and puts into the hand if space available
    public void touchDeckButton(TouchEvent t) {
        if ((deckButton.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
        }
    }

    //James Bailey 40156063
    //checks whether the graveyard button has been touched and allows the player to view cards in the graveyard
    public void touchGraveyardButton(TouchEvent t) {
        if ((graveyardButton.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
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
        for (Card handCard : handCards) {
            handCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        for (UnimonCard benchCard : benchCards) {
            benchCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        for (UnimonCard prizeCard : prizeCards) {
            prizeCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport, null);
        }

        deckButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        graveyardButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        activeUnimonCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        opponentButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //generate all the cards
    public void generateCards() {
        generateShowOpponentsScreenButton();
        generateHandCards();
        generateBenchCard();
        generatePrizeCards();
        generateActiveUnimon();
    }

    //generate the buttons
    public void generateButtons() {
        generateDeckButton();
        generateGraveyardButton();
    }


    //James Bailey 40156063
    //generate the cards that go into the user's hand
    public void generateHandCards() {
        float width;
        float x;
        float height;
        float y;

        //takes cards from the top of the deck stack and puts them into the hand cards array - adjusting their position
        for (int i = 0; i < numHandCards; i++) {

            //set up the dimensions for the card to be drawn to
            width = mScreenViewport.width / 10;
            x = (width / 2 * (i + 1)) + ((width * i) * 0.75f) + mScreenViewport.width / 6;
            height = mScreenViewport.height / 4;
            y = height / 2;

            Card handCard = deck.pop();
            handCard.setPosition(mGameScreen, x, y, width, height);
            handCards[i] = handCard;
        }
    }

    //generate the button used to represent the top of the deck to be drawn from
    public void generateDeckButton() {
        float width = mScreenViewport.width / 10;
        float x = mScreenViewport.width - mScreenViewport.width / 10;
        float height = mScreenViewport.height / 4;
        float y = mScreenViewport.height-(height / 2 + mScreenViewport.height / 9);

        deckButton = new ReleaseButton(x, y, width, height, "Deck", "Deck", "", mGameScreen);
    }

    //James Bailey 40156063
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
            benchCard.setPosition(mGameScreen, x, y, width, height);
            benchCards[i] = benchCard;
        }
    }

    //James Bailey 40156063
    //generate the player's current prize cards.
    public void generatePrizeCards() {
        float width;
        float x;
        float height;
        float y;

        for (int i = 0; i < numPrizeCards; i++) {
            width = mScreenViewport.width / 8;
            height = mScreenViewport.height / 6;
            x = mScreenViewport.width - width/2;

            y = mScreenViewport.height - height*(i+1);
            UnimonCard prizeCard = prizeCards[i];

            prizeCard.setPosition(mGameScreen, x, y, width, height);

            //rotate bitmap by 90 degrees clockwise
            Bitmap rotatedBitmap = DrawAssist.rotateBitmap(prizeCard.getBitmap(), 90);
            prizeCard.setmBitmap(rotatedBitmap);

            prizeCards[i] = prizeCard;
        }
    }

    //James Bailey 40156063
    //generate the dimensions for the active unimon
    public void generateActiveUnimon() {
        float width = mScreenViewport.width / 4;
        float x = mScreenViewport.width / 2;
        float height = mScreenViewport.height / 1.7f;
        float y = ((mScreenViewport.height / 2) + (mScreenViewport.height/8));
        activeUnimonCard.setPosition(mGameScreen, x, y, width, height);
    }

    // generate the dimensions for the button that will show the opponent's screen
    public void generateShowOpponentsScreenButton() {
        float width = mScreenViewport.width / 7;
        float x = width / 2 + mScreenViewport.width / 5.6f;
        float height = mScreenViewport.height / 8;
        float y = mScreenViewport.height - mScreenViewport.height / 30 - height / 2;
        opponentButton = new GameObject(x, y, width, height, selectBitmap("Opponent"), mGameScreen);
    }

    //James Bailey 40156063
    //generate the cards not currently active in the game - graveyard
    public void generateGraveyardButton() {
        float width = mScreenViewport.width / 10;
        float height = mScreenViewport.height / 4;
        float x = width / 2 + mScreenViewport.width / 40;
        float y = mScreenViewport.height-(mScreenViewport.height- mScreenViewport.height / 40 - height / 2);
        graveyardButton = new ReleaseButton(x, y, width, height, "Graveyard", "Graveyard", "", mGameScreen);
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
        mGame.getAssetManager().loadAndAddBitmap("Graveyard", "img/PlayArea/Graveyard.png");
        mGame.getAssetManager().loadAndAddBitmap("Opponent", "img/PlayScreenButtons/opponent_screen.png");
    }




}
