package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Battle;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by James on 12/04/2017.
 */

public class BenchState extends State {

    public enum StateType { //the type of states the state can have - controls touch handling and presentation of cards.
        RETREAT, VIEW_BENCH, CHOOSE_ACTIVE
    }

    protected StateType currentStateType;

    protected BattleSetup battleSetup;
    protected UnimonCard[] benchCards; //the battle setup's bench cards
    protected UnimonCard[] copyBenchCards; //a copy of the battlesetup's bench cards - has to be copy as different dimensions and position in different state types.

    protected PlayScreen playScreen; //the controlling GameScreen

    //black bitmap drawn in the background of the presented cards.
    protected Bitmap blackBitmap;
    protected Paint blackBitmapPaint;
    protected Rect blackBitmapRect; //the position and dimensions to draw the black bitmap to

    protected final int numBenchCardsBeforePlay = 4;
    protected final int numBenchCardsInPlay = 3;

    public BenchState(ScreenViewport mScreenViewport, LayerViewport mLayerViewport, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup){
        super(mScreenViewport, mLayerViewport, mGame, mGameScreen, active);

        this.playScreen = (PlayScreen) mGameScreen;
        this.battleSetup = battleSetup;
        this.benchCards = battleSetup.getBenchCards();

        this.currentStateType = StateType.CHOOSE_ACTIVE; //first state type - the user must choose an active unimon first in battle

        copyBenchCards();
        generateBenchUnimon();
    }

    //James Bailey 40156063
    //resets the state in the event of the battlesetup's bench cards potentially changing in an external class.
    public void refresh() {
        this.benchCards = battleSetup.getBenchCards();

        copyBenchCards();
        generateBenchUnimon();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            updateBenchCards(elapsedTime);

            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            handleTouch(touchEvents);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        if (active) {
            drawBlackBackground(graphics2D);
            drawBenchCards(elapsedTime, graphics2D);
        }
    }

    //James Bailey 40156063
    //draws the black bitmap in the background of the foreground bench cards
    public void drawBlackBackground(IGraphics2D graphics2D) {
        if (blackBitmapRect == null) {
            blackBitmapRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height); //set black bitmap to fill whole screenviewport
        }
        if (blackBitmap == null) {
            //creates the black bitmap
            blackBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            blackBitmap.eraseColor(Color.BLACK);
        }
        if (blackBitmapPaint == null) {
            blackBitmapPaint = new Paint();
            blackBitmapPaint.setAlpha(200); //make the background behind the unimon card semi-opaque
        }

        graphics2D.drawBitmap(blackBitmap, null, blackBitmapRect, blackBitmapPaint);
    }

    //James Bailey 40156063
    //draws all the presented bench cards
    public void drawBenchCards(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (UnimonCard unimonCard: copyBenchCards) {
            unimonCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }
    }


    //James Bailey 40156063
    //updates all the presented bench cards
    public void updateBenchCards(ElapsedTime elapsedTime) {
        for (UnimonCard unimonCard: copyBenchCards) {
            unimonCard.update(elapsedTime);
        }
    }

    //James Bailey 40156063
    //handles a touch event while the state is active
    public void handleTouch(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (touchBenchUnimon(t)) break;
            }
        }
    }

    //James Bailey 40156063
    //handles a touch on a bench unimon or dismissing the bench unimon by clicking the black bitmap space.
    public boolean touchBenchUnimon(TouchEvent t) {
        for (int i = 0; i < copyBenchCards.length; i++) {
            UnimonCard benchCard = copyBenchCards[i];
            if (benchCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y)) { //checks whether the touch event is in the bench unimon's touch boundary
                if (currentStateType == StateType.RETREAT) {
                    touchRetreat(i); //handles the user selecting a bench unimon to swap with the current active unimon
                } else if (currentStateType == StateType.CHOOSE_ACTIVE) {
                    touchActive(i); //handles the user selecting a bench unimon to be th initial active unimon
                }
                return true; //bench card has been touched - touch handled
            }
        }

        touchDismiss(); //bench unimon has not been touched, dismiss the bench state
        return true; //user dismissed bench cards - touch handled
    }

    //James Bailey 40156063
    //handles touching away from any of the bench unimon to dismiss the bench state.
    public void touchDismiss() {
        active = false;
        mInput.resetAccumulators();

        if (currentStateType == StateType.VIEW_BENCH) {
            playScreen.getPlayOverviewState().setTouchActive(true); //presents the play overview state the user can interact with.
        } else if (currentStateType == StateType.RETREAT) {
            playScreen.getActiveUnimonState().active = true; //presents the active unimon state
        }
    }

    //James Bailey 40156063
    //handles the user touching a bench unimon to be the new active unimon card after the current unimon card is retreated.
    public void touchRetreat(int index) {
        Battle.retreatUnimon(battleSetup, copyBenchCards, index); //swaps the current active unimon card with the selected bench card

        mGame.getPlayer().setPlayerBattleSetup(battleSetup); //set the new battlesetup after the retreat unimon changes both the bench and active unimon card.

        //refresh the states - needed as new battlesetup may have changed objects that need to be recopied
        refresh();
        playScreen.getPlayOverviewState().refresh();
        playScreen.getActiveUnimonState().refresh();

        active = false;
        mInput.resetAccumulators();

        playScreen.getPlayOverviewState().setTouchActive(true); //presents the playoveriew the user can interact with.
    }

    //James Bailey 40156063
    //handles the initial choosing of an active unimon card to begin battle from the presented bench cards
    public void touchActive(int index) {
        Battle.chooseActive(battleSetup, copyBenchCards, index); //sets the selected bench unimon as the active card in the player's battlesetup, removing it from the bench card list.

        mGame.getPlayer().setPlayerBattleSetup(battleSetup);

        playScreen.createOtherStates(); //create the other states that require the initialisation of an active unimon card.
        setCurrentStateType(StateType.VIEW_BENCH);

        refresh(); //needs to refresh state - bench card list changes in Battle's chooseActive method.

        active = false;
        mInput.resetAccumulators();
    }

    //James Bailey 40156063
    //generates bench cards' position and dimensions to be able to draw it onto the screen
    public void generateBenchUnimon() {
        for (int i = 0; i < copyBenchCards.length; i++) {
            UnimonCard benchCard = copyBenchCards[i];
            generateSingleBenchUnimon(benchCard, i);
        }
    }

    //James Bailey 40156063
    //generate a single unimon card's position and dimensions
    public void generateSingleBenchUnimon(UnimonCard benchUnimon, int index) {
        //two different methods to generate a single unimon card/
        //user chooses 4 bench cards to be the initial active card, 3 to retreat to or viewing the bench
        if (currentStateType == StateType.CHOOSE_ACTIVE) {
            generateSingleBenchUnimonActive(benchUnimon, index);
        } else if (currentStateType == StateType.RETREAT || currentStateType == StateType.VIEW_BENCH) {
            generateSingleBenchUnimonBench(benchUnimon, index);
        }
    }

    //James Bailey 40156063
    //generate a single bench card the player can choose to be the initial active unimon card
    public void generateSingleBenchUnimonActive(UnimonCard benchUnimon, int index) {
        float x;
        float y;
        float width;
        float height;

        y = mScreenViewport.height/2;
        width = mScreenViewport.width /5f;
        x = mScreenViewport.width/8f + (mScreenViewport.width/4f * index);
        height = mScreenViewport.height /1.8f;

        benchUnimon.setPosition(mGameScreen, x, y, width, height);
    }


    //James Bailey 40156063
    //generate a single unimon card the player can choose to retreat to or be presented as the bench
    public void generateSingleBenchUnimonBench(UnimonCard benchUnimon, int index) {
        float x;
        float y;
        float width;
        float height;

        y = mScreenViewport.height/2;
        width = mScreenViewport.width /3.5f;
        x = mScreenViewport.width/6f + (mScreenViewport.width/3f * index);
        height = mScreenViewport.height /1.5f;

        benchUnimon.setPosition(mGameScreen, x, y, width, height);
    }

    //James Bailey 40156063
    //Method that initalises a list that stores all the bench cards in the battlesetup
    public void copyBenchCards() {
        //different sizes to initalise the bench card list depending on the state type
        if (currentStateType == StateType.VIEW_BENCH || currentStateType == StateType.RETREAT) {
            copyBenchCards = new UnimonCard[numBenchCardsInPlay];
        } else if (currentStateType == StateType.CHOOSE_ACTIVE) {
            copyBenchCards = new UnimonCard[numBenchCardsBeforePlay];
        }

        //iterate through and copy each unimon card to the newly initalised list.
        for (int i = 0; i < benchCards.length; i++) {
            UnimonCard benchCard = benchCards[i];
            copyBenchCards[i] = benchCard.copy();
        }
    }

    public StateType getCurrentStateType() {
        return currentStateType;
    }

    public void setCurrentStateType(StateType currentStateType) {
        this.currentStateType = currentStateType;
    }
}