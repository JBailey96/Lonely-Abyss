package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Battle;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.CustomGage.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.CustomGage.State;

/**
 * Created by kyle on 08/03/2017.
 */

public class PlayOverviewState extends State {
    //the quantity of cards the player has
    protected final int numHandCards = 5;
    protected final int numBenchCards = 3;
    protected final int numPrizeCards = 3;

    protected boolean touchActive; //true - the user can interact with the state by touching the screen, false no touch events registered

    private Card[] handCards; //the cards in the players hand displayed
    private Stack<Card> deck; //the deck the player has chosen that can be drawn from when there is an opening in deck cards
    private UnimonCard[] benchCards; //the bench cards to be displayed
    private UnimonCard[] prizeCards; //the prize cards to be displayed

    ReleaseButton deckButton; //button when pressed draws a card to the player's hand from the player's deck
    ReleaseButton graveyardButton; //button when pressed shows the unimoncards that have been knocked out

    private UnimonCard activeUnimonCard; //the unimon card that the player has currently active

    private GameObject opponentButton; // the button that will show the opponent's screen

    protected BattleSetup playerBattleSetup; //the player's battle setup

    protected PlayScreen playScreen;


    public PlayOverviewState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        this.playerBattleSetup = battleSetup;
        this.playScreen = (PlayScreen) mGameScreen;
        touchActive = true;

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
        battleSetup.setHandCard(handCards); //initialise the player's hand cards

        benchCards = battleSetup.getBenchCards();
        prizeCards = battleSetup.getPrizeCards();
    }

    //James Bailey 40156063
    //Refreshes the state - called when the battlesetups' objects could have changed
    public void refresh() {
        benchCards = playerBattleSetup.getBenchCards(); //store a reference to the player battlesetup's bench cards
        generateBenchCards();
        setActiveUnimonCard(playerBattleSetup.getActiveCard().copy());
        generateActiveUnimon();
    }

    //James Bailey 40156063
    //Refreshes the hand cards that are displayed to the user
    public void refreshHandCards() {
        this.handCards = playerBattleSetup.getHandCard();

        for (int i = 0; i < numHandCards; i++) {
            Card handCard = handCards[i];
            if (handCard != null) {
                generateSingleHandCard(handCard, i);
            }
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            updateCards(elapsedTime);
            updateButtons(elapsedTime);


            if (touchActive) {
                mInput = mGame.getInput();
                touchEvents = mInput.getTouchEvents();
                touchButton(touchEvents);
                touchCards(touchEvents);
            }
        }
    }

    //James Bailey 40156063
    //update all the cards
    public void updateCards(ElapsedTime elapsedTime) {
        for (Card handCard : handCards) {
            if (handCard != null) { //ensures the hand card exists
                handCard.update(elapsedTime);
            }
        }
        activeUnimonCard.update(elapsedTime);
    }

    //James Bailey 40156063
    //update all the buttons
    public void updateButtons(ElapsedTime elapsedTime) {
        deckButton.update(elapsedTime);
        opponentButton.update(elapsedTime);
        graveyardButton.update(elapsedTime);
    }

    //James Bailey 40156063
    //validates whether a card has been touched
    public void touchCards(List<TouchEvent> touchEvents) {
        for (TouchEvent t: touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) {
                touchBenchCards(t);
            }
        }
    }

    //James Bailey 40156063
    //Handles touching one of the bench cards
    public void touchBenchCards(TouchEvent t) {
        for (UnimonCard benchCard : benchCards) {
            if (benchCard != null) {
                if ((benchCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) { //validates whether the user has touched a bench card
                    playScreen.getBenchState().setCurrentStateType(BenchState.StateType.VIEW_BENCH); //set the benchstate state's type in order to present to the user to bench cards
                    this.touchActive = false;
                    playScreen.getBenchState().active = true;
                    playScreen.getBenchState().showMessage();
                    mInput.resetAccumulators();
                    break;
                }
            }
        }
    }

    //James Bailey 40156063
    //checks for touch events on the buttons or active unimon card
    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                touchActiveUnimon(t);
                touchDeckButton();
                touchGraveyardButton();
            }
        }
    }

    //James Bailey 40156063
    //checks whether the active unimon has been touched and activates the active unimon state
    public void touchActiveUnimon(TouchEvent t) {
        if ((activeUnimonCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            this.touchActive = false;
            playScreen.getActiveUnimonState().active = true;
        }
    }

    //checks whether the opponent button has been touched
    public void touchOpponentButton(TouchEvent t) {
        if ((opponentButton.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            playScreen.getOpponentState().active = true;
        }
    }

    //James Bailey 40156063
    //checks whether the deck button has been touched and takes a card from the deck and puts into the hand if space available
    public void touchDeckButton() {
        if (deckButton.isPushed()) {
            int indexNewHandCard = Battle.drawFromDeck(playerBattleSetup); //the index of the new hand card to be added

            if (indexNewHandCard != -1) { //there is an 'opening' for a hand card to be added
                generateSingleHandCard(handCards[indexNewHandCard], indexNewHandCard); //add a new hand card to the list
            }

            playScreen.getHandCardsState().refresh();
        }
    }



    //James Bailey 40156063
    //checks whether the graveyard button has been touched and allows the player to view cards in the graveyard
    public void touchGraveyardButton() {
//        if (graveyardButton.isPushed()) {
//            indexToRemove++;
//            indexToRemove = indexToRemove % 5;
//            Battle.removeFromHand(handCards, indexToRemove);
//        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (active) {
            drawCards(elapsedTime, graphics2D);
            drawButtons(elapsedTime, graphics2D);
        }
    }

    //James Bailey 40156063
    //draw all the cards
    public void drawCards(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //draw all the hand cards
        for (int i = 0; i < handCards.length; i ++) {
            Card handCard = handCards[i];

            if (handCard != null) { //hand card exists - then draw
                handCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
            } else { //draw an empty placeholder for the hand card
                drawEmptyCard(graphics2D, i);
            }
        }

        //draw all the bench cards
        for (UnimonCard benchCard : benchCards) {
            if (benchCard != null) {
                benchCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
            }
        }

        //draw all the prize cards
        for (UnimonCard prizeCard : prizeCards) {
            prizeCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport, null);
        }

        //draw the active unimon card
        activeUnimonCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //James Bailey 40156063
    //Draw all the empty cards
    public void drawEmptyCard(IGraphics2D graphics2D, int indexEmptyCard) {
        Rect emptyHandCardDimen = generateEmptyHandCardDimension(indexEmptyCard);
        graphics2D.drawBitmap(selectBitmap("EmptyCard"), null, emptyHandCardDimen, null);
    }

    //James Bailey 40156063
    //draw all the buttons
    public void drawButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        deckButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        graveyardButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);

        opponentButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //James Bailey 40156063
    //generate all the cards
    public void generateCards() {
        generateShowOpponentsScreenButton();
        generateHandCards();
        generateBenchCards();
        generatePrizeCards();
        generateActiveUnimon();
    }

    //James Bailey 40156063
    //generate the buttons
    public void generateButtons() {
        generateDeckButton();
        generateGraveyardButton();
    }


    //James Bailey 40156063
    //generate the cards that go into the user's hand - initial set up
    public void generateHandCards() {
        //takes cards from the top of the deck stack and puts them into the hand cards array - adjusting their position
        for (int i = 0; i < numHandCards; i++) {
            Card handCard = deck.pop();
            generateSingleHandCard(handCard, i); //sets dimensions and position for the hand card - depends upon it's position in the index
            handCards[i] = handCard; //add handcard to the list
        }
    }

    //James Bailey 40156063
    //sets the dimensions and position of a hand card.
    public void generateSingleHandCard(Card handCard, int index) {
        float width;
        float x;
        float height;
        float y;

        //set up the dimensions and position for the card to be drawn to
        width = mScreenViewport.width / 10;
        x = calculateHandCardX(width, index);
        height = mScreenViewport.height / 4;
        y = height / 2;

        handCard.setPosition(mGameScreen, x, y, width, height);
    }

    //James Bailey 40156063
    //calculates the x position of individual the hand card
    private float calculateHandCardX(float width, int index) {
        float x = (width / 2 * (index + 1)) + ((width * index) * 0.75f) + mScreenViewport.width / 6;
        return x;
    }

    //James Bailey 40156063
    //sets the dimensions and position of a placeholder for an empty hand card.
    public Rect generateEmptyHandCardDimension(int index) {
        float width;
        float x;
        float height;
        float y;

        width = mScreenViewport.width / 10;
        x = calculateHandCardX(width, index);
        height = mScreenViewport.height / 4;
        y = height / 2;

        Rect emptyHandCardRect = new Rect((int) (x-width/2), (int) (mScreenViewport.height-height), (int) (x+width/2), (mScreenViewport.height));
        return emptyHandCardRect;
    }

    //James Bailey 40156063
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
    public void generateBenchCards() {
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
        mGame.getAssetManager().loadAndAddBitmap("EmptyCard", "img/Cards/EmptyCard.png");
    }

    public UnimonCard getActiveUnimonCard() {
        return activeUnimonCard;
    }

    public void setActiveUnimonCard(UnimonCard activeUnimonCard) {
        this.activeUnimonCard = activeUnimonCard;
    }

    public boolean isTouchActive() {
        return touchActive;
    }

    public void setTouchActive(boolean touchActive) {
        this.touchActive = touchActive;
    }

    //James Bailey 40156063
    //Display a help message to introduce the user to the game's functionality - where to begin
    public void showInitialHelpMessage() {
        DrawAssist.showMessage(mGame, "To begin, please touch the ACTIVE UNIMON in the centre.");
    }
}

