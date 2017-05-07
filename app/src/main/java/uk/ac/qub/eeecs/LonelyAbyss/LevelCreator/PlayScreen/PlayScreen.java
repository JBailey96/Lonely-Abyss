package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.List;


import uk.ac.qub.eeecs.LonelyAbyss.DiscontinuedCode.ActiveEnergyState;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.BattleSetup;
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
import uk.ac.qub.eeecs.gage.CustomGage.State;

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
    protected CoinState coinState;

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
        playerBattleSetup = mGame.getPlayer().getPlayerBattleSetup();
        createInitialState();
    }

    //James Bailey 40156063
    //The first state that is called - used to select the active unimon card.
    public void createInitialState() {
        this.benchState = new BenchState(mScreenViewport, mLayerViewPort, mGame, this, true, playerBattleSetup);
    }

    //James Bailey 40156063
    //create the other states that require the initialisation of an active unimon card.
    public void createOtherStates() {
        this.playOverviewState = new PlayOverviewState(mScreenViewport, mLayerViewPort, mGame, this, false, playerBattleSetup); //state active initalised to true - first state
        this.activeUnimonState = new ActiveUnimonState(mScreenViewport, mLayerViewPort, mGame, this, false, playerBattleSetup);
        this.opponentState = new OpponentState(mScreenViewport, mLayerViewPort, mGame, this, playerBattleSetup, false);
        this.activeEnergyState = new ActiveEnergyState(mScreenViewport, mLayerViewPort, mGame, this, false);
        this.handCardsState = new HandCardsState(mScreenViewport, mLayerViewPort, mGame, this, false, playerBattleSetup);
        this.coinState = new CoinState(mScreenViewport,mLayerViewPort,mGame,this,true);
    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        updateStates(elapsedTime);
    }


    //James Bailey 40156063
    //Updates all the states
    private void updateStates(ElapsedTime elapsedTime) {
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
        if(verifyState(coinState)){
            coinState.update(elapsedTime);
        }
        if (verifyState(handCardsState)) {
            handCardsState.update(elapsedTime);
        }
        //activeEnergyState.update(elapsedTime);
    }

    //James Bailey 40156063
    //verifies that the state has been initialised and is active
    private boolean verifyState(State state) {
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
    private void drawStates(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
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
        if(verifyState(coinState)){
            coinState.draw(elapsedTime,graphics2D);
        }
        if (verifyState(handCardsState)) {
            handCardsState.draw(elapsedTime, graphics2D);
        }
       //activeEnergyState.draw(elapsedTime, graphics2D);
    }

    //James Bailey 40156063
    //Generates the background dimensions and loads the bitmap
     private void generateBackground() {
        backgroundRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height);
        background = mGame.getAssetManager().getBitmap("PlayAreaBackground");
    }

    //James Bailey 40156063
    //draw the play area's background
    private void drawBackground(IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap
    }

    //James Bailey 40156063
    //Loads the bitmaps that are used in all screens - the background
    private void loadPlayScreenBitmaps() {
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
    public CoinState getCoinState(){
        return coinState;
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
