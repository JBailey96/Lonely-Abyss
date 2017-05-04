package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;


import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.Player;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.DeckManagement;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.GridLevel;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by Kyle on 22/11/2016.
 */

public class PlayScreen extends GameScreen {
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewPort;
    protected Input mInput;
    public List<TouchEvent> touchEvents;


    //the states the play area can have
    protected PlayOverviewState playOverviewState;
    protected ActiveUnimonState activeUnimonState;
    protected OpponentState opponentState;
    protected ActiveEnergyState activeEnergyState;
    protected BenchState benchState;
    protected HandCardsState handCardsState;

    private BattleSetup playerBattleSetup; //the battle setup the player has

    //the background bitmap and the dimensions on the screen to be drawn in
    protected Bitmap background;
    protected Rect backgroundRect;


    public PlayScreen(Game game) {
        super("PlayScreen", game);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewPort = new LayerViewport(mScreenViewport.width/2, mScreenViewport.height/2, mScreenViewport.width/2, mScreenViewport.height/2);
        loadPlayScreenBitmaps();
        generateBackground();
        createTestBattleSetup();
        createInitialState();
    }

    //James Bailey 40156063
    //The first state that is called - used to select the active unimon card.
    public void createInitialState() {
        benchState = new BenchState(mScreenViewport, mLayerViewPort, mGame, this, true, playerBattleSetup);
    }

    //James Bailey 40156063
    //create the other states that require the initialisation of an active unimon card.
    public void createOtherStates() {
        playOverviewState = new PlayOverviewState(mScreenViewport, mLayerViewPort, mGame, this, true, playerBattleSetup); //state active initalised to true - first state
        activeUnimonState = new ActiveUnimonState(mScreenViewport, mLayerViewPort, mGame, this, false, playerBattleSetup);
        opponentState = new OpponentState(mScreenViewport, mLayerViewPort, mGame, this, playerBattleSetup, false);
        activeEnergyState = new ActiveEnergyState(mScreenViewport, mLayerViewPort, mGame, this, false);
        handCardsState = new HandCardsState(mScreenViewport, mLayerViewPort, mGame, this, false, playerBattleSetup);
    }


    //James Bailey 40156063
    //Creates a test battle setup - loads in the cards used in the playarea
    public void createTestBattleSetup() {
        ArrayList<UnimonCard> unimonCards = mGame.getPlayer().getUnimonCards();
        ArrayList<EnergyCard> energyCards = mGame.getPlayer().getEnergyCards();


        //copy the player's battle setup
        ArrayList<UnimonCard> copyUnimonCards = new ArrayList<>();
        ArrayList<EnergyCard> copyEnergyCards = new ArrayList<>();

        for (UnimonCard unimonCard : unimonCards) {
            copyUnimonCards.add(unimonCard.copy());
        }

        for (EnergyCard energyCard : energyCards) {
            copyEnergyCards.add(energyCard.copy());
        }

        UnimonCard[] prizeCard = selectPrizeCards(copyUnimonCards);
        UnimonCard[] benchCard = createBenchCards(copyUnimonCards);
        Stack<Card> deck = createDeck(copyUnimonCards, copyEnergyCards);

        playerBattleSetup = new BattleSetup(deck, benchCard, prizeCard);
    }

    //James Bailey 40156063
    //Selects 3 random Unimon cards from the user's entire collection of cards to be used as prize cards
    public UnimonCard[] selectPrizeCards(ArrayList<UnimonCard> playerUnimonCards) {
        final int numberOfPrizeCards = 3;

        UnimonCard[] prizeCards = new UnimonCard[numberOfPrizeCards];

        Random random = new Random();
        for (int i = 0; i < numberOfPrizeCards; i++) {
            int randomIndex = random.nextInt(playerUnimonCards.size());
            UnimonCard randomUnimonCard = playerUnimonCards.get(randomIndex);
            prizeCards[i] = randomUnimonCard.copy();
            playerUnimonCards.remove(randomIndex);
        }

        return prizeCards;
    }

    //James Bailey 40156063
    //Selects random 3 Unimon cards from the user's entire collection of cards to be on the bench
    public UnimonCard[] createBenchCards(ArrayList<UnimonCard> playerUnimonCards) {
        final int numberOfBenchCardsBeforePlay = 4;

        UnimonCard[] benchCards = new UnimonCard[numberOfBenchCardsBeforePlay];

        Random random = new Random();
        for (int i = 0; i < numberOfBenchCardsBeforePlay; i++) {
            int randomIndex = random.nextInt(playerUnimonCards.size());
            UnimonCard randomUnimonCard = playerUnimonCards.get(randomIndex);
            benchCards[i] = randomUnimonCard.copy();
            playerUnimonCards.remove(randomIndex);
        }

        return benchCards;
    }


    //James Bailey 40156063
    //Selects a single active Unimon card - active Unimon
    public UnimonCard selectActiveCard(ArrayList<UnimonCard> playerUnimonCards) {
            int maxIndexPlayerUnimonCards = playerUnimonCards.size()-1;
            int randomIndex = (int) (Math.random() * maxIndexPlayerUnimonCards);
            UnimonCard activeCard = playerUnimonCards.get(randomIndex).copy();
            playerUnimonCards.remove(randomIndex);

            return activeCard;
    }

    //James Bailey 40156063
    //Creates a deck that will be used to take cards off
    public Stack<Card> createDeck(ArrayList<UnimonCard> playerUnimonCards, ArrayList<EnergyCard> energyCards) {
        Random random = new Random();
        Stack<Card> deck = new Stack<>(); //the deck that the cards are pushed to

        final int deck_size = 25;

        int randomUnimonIndex;
        UnimonCard randomUnimonCard;

        int randomEnergyIndex;
        EnergyCard randomEnergyCard;

        boolean arraysEmpty = false;
        while ((!arraysEmpty) && (deck_size > deck.size())) {
            int randomIndex = random.nextInt(2);
            if ((randomIndex == 0) && (playerUnimonCards.size() > 0)) {
                randomUnimonIndex = random.nextInt(playerUnimonCards.size());
                randomUnimonCard = playerUnimonCards.get(randomUnimonIndex);
                deck.push(randomUnimonCard.copy());
                playerUnimonCards.remove(randomUnimonIndex);
            } else if (energyCards.size() > 0) {
                randomEnergyIndex = random.nextInt(energyCards.size());
                randomEnergyCard = energyCards.get(randomEnergyIndex);
                deck.push(randomEnergyCard.copy());
                energyCards.remove(randomEnergyIndex);
            } else {
                arraysEmpty = true;
            }
        }

        return deck;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        updateStates(elapsedTime);
    }


    //James Bailey 40156063
    //Updates all the states
    public void updateStates(ElapsedTime elapsedTime) {
        if (verifyState(activeUnimonState)) {
            activeUnimonState.update(elapsedTime);
        }
        if (verifyState(playOverviewState)) {
            playOverviewState.update(elapsedTime);
        }
        if (verifyState(opponentState)) {
            opponentState.update(elapsedTime);
        }
        if (verifyState(benchState)) {
            benchState.update(elapsedTime);
        }
        if (verifyState(handCardsState)) {
            handCardsState.update(elapsedTime);
        }
        //activeEnergyState.update(elapsedTime);
    }

    //James Bailey 40156063
    //verifies that the state has been initialised and is active
    public boolean verifyState(State state) {
        if (state != null) {
            if (state.active) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mScreenViewport.toRect());
        drawBackground(graphics2D);
        drawStates(elapsedTime, graphics2D);
    }


    //James Bailey 40156063
    //Draws all the states
    public void drawStates(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (verifyState(playOverviewState)) {
            playOverviewState.draw(elapsedTime, graphics2D);
        }
        if (verifyState(activeUnimonState)) {
            activeUnimonState.draw(elapsedTime, graphics2D);
        }
        if (verifyState(opponentState)) {
            opponentState.draw(elapsedTime, graphics2D);
        }
        if (verifyState(benchState)) {
            benchState.draw(elapsedTime, graphics2D);
        }
        if (verifyState(handCardsState)) {
            handCardsState.draw(elapsedTime, graphics2D);
        }
       //activeEnergyState.draw(elapsedTime, graphics2D);
    }

    //James Bailey 40156063
    //Generates the background dimensions and loads the bitmap
    public void generateBackground() {
        backgroundRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height);
        background = mGame.getAssetManager().getBitmap("PlayAreaBackground");
    }

    //James Bailey 40156063
    //draw the play area's background
    public void drawBackground(IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap
    }

    //James Bailey 40156063
    //Loads the bitmaps that are used in all screens - the background
    public void loadPlayScreenBitmaps() {
        mGame.getAssetManager().loadAndAddBitmap("PlayAreaBackground", "img/PlayArea/Background.jpeg");
    }

    //James Bailey 40156063
    //Method that handles the end of the battle and the transition back to the GridLevel.
    public void endBattleTransition() {
        ScreenManager screenManager = getGame().getScreenManager();

        screenManager.removeScreen(screenManager.getCurrentScreen().getName());
        GridLevel gridLevel = new GridLevel(mGame);
        screenManager.addScreen(gridLevel);
    }

    //getters and setters for the states
    public PlayOverviewState getPlayOverviewState() {
        return playOverviewState;
    }

    public void setPlayOverviewState(PlayOverviewState playOverviewState) {
        this.playOverviewState = playOverviewState;
    }

    public ActiveUnimonState getActiveUnimonState() {
        return activeUnimonState;
    }

    public ActiveEnergyState getActiveEnergyState() {
        return activeEnergyState;
    }

    public void setActiveEnergyState(ActiveEnergyState activeEnergyState){
        this.activeEnergyState = activeEnergyState;
    }

    public void setActiveUnimonState(ActiveUnimonState activeUnimonState) {
        this.activeUnimonState = activeUnimonState;
    }

    public OpponentState getOpponentState(){
        return opponentState;
    }
    public void setOpponentState(OpponentState opponentState){
        this.opponentState = opponentState;
    }

    public BenchState getBenchState() {
        return benchState;
    }

    public void setBenchState(BenchState benchState) {
        this.benchState = benchState;
    }

    public HandCardsState getHandCardsState() {
        return handCardsState;
    }

    public void setHandCardsState(HandCardsState handCardsState) {
        this.handCardsState = handCardsState;
    }

    public BattleSetup getBattleSetup() {
        return playerBattleSetup;
    }

    public void setBattleSetup(BattleSetup battleSetup) {
        this.playerBattleSetup = battleSetup;
    }
}
