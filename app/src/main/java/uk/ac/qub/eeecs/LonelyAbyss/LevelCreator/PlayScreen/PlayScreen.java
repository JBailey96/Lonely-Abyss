package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Color;
import android.graphics.Path;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;


import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.Player;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
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
    public List<TouchEvent> touchEvents;


    //the states the play area can have
    PlayOverviewState playOverviewState;
    ActiveUnimonState activeUnimonState;
    OpponentScreen opponentScreen;
    ActiveEnergyState activeEnergyState;

    private BattleSetup battleSetup;



    public PlayScreen(Game game) {
        super("PlayScreen", game);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewPort = new LayerViewport(mScreenViewport.width/2, mScreenViewport.height/2, mScreenViewport.width/2, mScreenViewport.height/2);

        createTestBattleSetup();
        createStates();
    }


    //create the states
    public void createStates() {
        playOverviewState = new PlayOverviewState(mScreenViewport, mLayerViewPort, mGame, this, true, battleSetup); //state active initalised to true - first state
        activeUnimonState = new ActiveUnimonState(mScreenViewport, mLayerViewPort, mGame, this, false, battleSetup);
       // inProgActiveUnimonState = new InProgActiveUnimonState(mScreenViewport, mLayerViewPort, mGame, this, false);
        opponentScreen = new OpponentScreen(mScreenViewport, mLayerViewPort, mGame, this, false);
        activeEnergyState = new ActiveEnergyState(mScreenViewport, mLayerViewPort, mGame, this, false);
    }


    //James Bailey 40156063
    //Creates a test battle setup - loads in the cards used in the playarea
    public void createTestBattleSetup() {
        ArrayList<UnimonCard> unimonCards = mGame.getPlayer().getUnimonCards();
        ArrayList<EnergyCard> energyCards = mGame.getPlayer().getEnergyCards();

        UnimonCard[] prizeCard = selectPrizeCards(unimonCards);
        UnimonCard[] benchCard = createBenchCards(unimonCards);
        UnimonCard activeCard = selectActiveCard(unimonCards);
        Stack<Card> deck = createDeck(unimonCards, energyCards);

        battleSetup = new BattleSetup(deck, activeCard, benchCard, prizeCard);
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
            prizeCards[i] = randomUnimonCard.copyUnimonCard();
            playerUnimonCards.remove(randomIndex);
        }

        return prizeCards;
    }

    //James Bailey 40156063
    //Selects random 3 Unimon cards from the user's entire collection of cards to be on the bench
    public UnimonCard[] createBenchCards(ArrayList<UnimonCard> playerUnimonCards) {
        final int numberOfBenchCards = 3;

        UnimonCard[] benchCards = new UnimonCard[numberOfBenchCards];

        Random random = new Random();
        for (int i = 0; i < numberOfBenchCards; i++) {
            int randomIndex = random.nextInt(playerUnimonCards.size());
            UnimonCard randomUnimonCard = playerUnimonCards.get(randomIndex);
            benchCards[i] = randomUnimonCard.copyUnimonCard();
            playerUnimonCards.remove(randomIndex);
        }

        return benchCards;
    }


    //James Bailey 40156063
    //Selects a single active Unimon card - active Unimon
    public UnimonCard selectActiveCard(ArrayList<UnimonCard> playerUnimonCards) {
            int maxIndexPlayerUnimonCards = playerUnimonCards.size()-1;
            int randomIndex = (int) (Math.random() * maxIndexPlayerUnimonCards);
            UnimonCard activeCard = playerUnimonCards.get(randomIndex).copyUnimonCard();
            playerUnimonCards.remove(randomIndex);

            return activeCard;
    }

    //Creates a deck that will be used to take cards off
    public Stack<Card> createDeck(ArrayList<UnimonCard> playerUnimonCards, ArrayList<EnergyCard> energyCards) {
        Random random = new Random();
        Stack<Card> deck = new Stack<>();

        final int deck_size = 25;

        boolean arraysEmpty = false;
        while ((!arraysEmpty) && (deck_size > deck.size())) {
            int randomIndex = random.nextInt(2);
            if ((randomIndex == 0) && (playerUnimonCards.size() > 0)) {
                int randomUnimonIndex = random.nextInt(playerUnimonCards.size());
                UnimonCard randomUnimonCard = playerUnimonCards.get(randomUnimonIndex);
                deck.push(randomUnimonCard.copyUnimonCard());
                playerUnimonCards.remove(randomUnimonIndex);
            } else if (energyCards.size() > 0) {
                int randomEnergyIndex = random.nextInt(energyCards.size());
                EnergyCard randomEnergyCard = energyCards.get(randomEnergyIndex);
                deck.push(randomEnergyCard.copyEnergyCard());
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


    public void updateStates(ElapsedTime elapsedTime) {
        activeUnimonState.update(elapsedTime);
       // inProgActiveUnimonState.update(elapsedTime);
        playOverviewState.update(elapsedTime);
        opponentScreen.update(elapsedTime);
        activeEnergyState.update(elapsedTime);
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mScreenViewport.toRect());

        drawStates(elapsedTime, graphics2D);
    }


    public void drawStates(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        playOverviewState.draw(elapsedTime, graphics2D);
        activeUnimonState.draw(elapsedTime, graphics2D);
       // inProgActiveUnimonState.draw(elapsedTime, graphics2D);
        opponentScreen.draw(elapsedTime, graphics2D);
        activeEnergyState.draw(elapsedTime, graphics2D);
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

    public OpponentScreen getOpponentScreen(){
        return opponentScreen;
    }
    public void setOpponentScreen(OpponentScreen opponentScreen){
        this.opponentScreen = opponentScreen;
    }

    public BattleSetup getBattleSetup() {
        return battleSetup;
    }

    public void setBattleSetup(BattleSetup battleSetup) {
        this.battleSetup = battleSetup;
    }
}
