package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.List;

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
 * Created by Jordan on 25/11/2016.
 */

public class MenuScreen extends GameScreen {
    protected Bitmap background; // the background of the menu screen
    protected Bitmap playButton; // the button the user can press to begin a new game
    protected Bitmap logo; //the logo of the game

    protected Rect backgroundRect; //background of the menu screen
    protected Rect playButtonRect; //the play button
    protected Rect logoRect; //contains the game logo

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;


    protected Input mInput; //user input on the menu
    List<TouchEvent> touchEvents;

    public MenuScreen(Game game) {
        super("MainMenu", game);

        //Define dimensions for the Bitmaps using Rect
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        backgroundRect = new Rect(0, 0, game.getScreenWidth(),  game.getScreenHeight());
        playButtonRect = new Rect((int) mLayerViewport.halfWidth-200, (int) mLayerViewport.halfHeight+200, (int) mLayerViewport.halfWidth+200, (int) mLayerViewport.halfHeight+400);
        logoRect = new Rect((int) mLayerViewport.halfWidth-800, (int) mLayerViewport.halfHeight-400, (int) mLayerViewport.halfWidth+800, (int) mLayerViewport.halfHeight+200);
        //Fill Bitmaps

        loadMenuBitmaps();
        background = game.getAssetManager().getBitmap("BACKGROUND");
        playButton = game.getAssetManager().getBitmap("PLAY");
        logo = game.getAssetManager().getBitmap("LOGO");
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        touchButton(touchEvents);
    }

    public void touchButton(List<TouchEvent> touchEvents) {

        for (TouchEvent t : touchEvents) {
            if ((t.type == TouchEvent.TOUCH_UP) && (playButtonRect.contains((int) t.x, (int) t.y))) {
                /*mGame.getScreenManager().removeScreen(this.getName());
                DeckManagement deckManage = new DeckManagement(mGame);
                mGame.getScreenManager().addScreen(deckManage);*/

                mGame.getScreenManager().removeScreen(this.getName());
                GridLevel gLevel = new GridLevel(mGame);
                mGame.getScreenManager().addScreen(gLevel);
            }
            }
        }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null);
        //.drawBitmap();
        graphics2D.drawBitmap(playButton, null, playButtonRect, null);
        graphics2D.drawBitmap(logo, null, logoRect, null);
    }

    public void loadMenuBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("BACKGROUND", "img/MenuImages/MenuBackground2.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAY", "img/MenuImages/PlayButton.png");
        getGame().getAssetManager().loadAndAddBitmap("LOGO", "img/MenuImages/Logo.png");
    }
}
