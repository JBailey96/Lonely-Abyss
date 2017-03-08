package uk.ac.qub.eeecs.gage.world;

import android.graphics.Bitmap;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;

/**
 * Created by appcamp on 08/03/2017.
 */

public abstract class State {
    public ScreenViewport mScreenViewport;
    public LayerViewport mLayerViewPort;
    public GameScreen mGameScreen;
    public Game mGame;
    public Input mInput;
    public List<TouchEvent> touchEvents;
    public Boolean active;

    public State(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active) {
        this.mScreenViewport = mScreenViewport;
        this.mLayerViewPort = mLayerViewPort;
        this.mGame = mGame;
        this.active = active;
        this.mGameScreen = mGameScreen;
    }

    public abstract void update(ElapsedTime elapsedTime);

    public abstract void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    //useful method for retrieving a specific bitmap from the asset manager using the string key
    public Bitmap selectBitmap(String cardKey) {
        return mGame.getAssetManager().getBitmap(cardKey);
    }
}
