package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Color;
import android.graphics.Path;

import java.util.List;
import java.util.Random;


import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.Player;
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



    public PlayScreen(Game game) {
        super("PlayScreen", game);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewPort = new LayerViewport(mScreenViewport.width/2, mScreenViewport.height/2, mScreenViewport.width/2, mScreenViewport.height/2);

        createStates();
    }


    //create the states
    public void createStates() {
        playOverviewState = new PlayOverviewState(mScreenViewport, mLayerViewPort, mGame, this, true); //state active initalised to true - first state
        activeUnimonState = new ActiveUnimonState(mScreenViewport, mLayerViewPort, mGame, this, false);
        opponentScreen = new OpponentScreen(mScreenViewport, mLayerViewPort, mGame, this, false);
    }



    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();

        updateStates(elapsedTime);
    }


    public void updateStates(ElapsedTime elapsedTime) {
        activeUnimonState.update(elapsedTime);
        playOverviewState.update(elapsedTime);
        opponentScreen.update(elapsedTime);
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
        opponentScreen.draw(elapsedTime, graphics2D);
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

    public void setActiveUnimonState(ActiveUnimonState activeUnimonState) {
        this.activeUnimonState = activeUnimonState;
    }

    public OpponentScreen getOpponentScreen(){
        return opponentScreen;
    }
    public void setOpponentScreen(OpponentScreen opponentScreen){
        this.opponentScreen = opponentScreen;
    }


}
