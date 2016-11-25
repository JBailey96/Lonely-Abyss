package uk.ac.qub.eeecs.LonelyAbyss.MenuScreen;

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
    protected Bitmap background;
    protected Bitmap playButton;
    protected Bitmap manageDeckButton;
    protected Bitmap settingsButton;
    protected Bitmap aboutButton;

    protected Rect backgroundRect;
    protected Rect playButtonRect;
    protected Rect manageDeckButtonRect;
    protected Rect settingsButtonRect;
    protected Rect aboutButtonRect;

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;

    public MenuScreen(Game game) {
        super("MainMenu", game);

        //Define dimensions for the Bitmaps using Rect

        backgroundRect = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        playButtonRect = new Rect(100, 150, 700, 300);
        manageDeckButtonRect = new Rect(800, 150, 700, 300);
        settingsButtonRect = new Rect(100, 550, 700, 450);

        //Fill Bitmaps

        loadMenuBitmaps();

        background = game.getAssetManager().getBitmap("BACKGROUND");
        playButton = game.getAssetManager().getBitmap("PLAY");
        manageDeckButton = game.getAssetManager().getBitmap("MANAGE DECK");
        settingsButton = game.getAssetManager().getBitmap("SETTINGS");


    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();


    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null);
        graphics2D.drawBitmap(playButton, null, playButtonRect, null);
        graphics2D.drawBitmap(manageDeckButton, null, manageDeckButtonRect, null);
        graphics2D.drawBitmap(settingsButton, null, settingsButtonRect, null);


    }

    public void loadMenuBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("BACKGROUND", "img/MenuImages/MenuBackground2.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAY", "img/MenuImages/PlayButton.png");
        getGame().getAssetManager().loadAndAddBitmap("MANAGE DECK", "img/MenuImages/ManageDeckButton.png");
        getGame().getAssetManager().loadAndAddBitmap("SETTINGS", "img/MenuImages/SettingsButton.png");
        getGame().getAssetManager().loadAndAddBitmap("ABOUT", "img/MenuImages/AboutButton.png");

    }
}
