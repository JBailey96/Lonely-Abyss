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
import uk.ac.qub.eeecs.gage.ui.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by Jordan on 25/11/2016.
 */

public class MenuScreen extends GameScreen {
    protected Bitmap background; // the background of the menu screen

    protected Bitmap logo; //the logo of the game

    protected Rect backgroundRect; //background of the menu screen

    protected Rect playButtonRect; //the play button dimensions
    protected ReleaseButton playButton; //play button the user to presses to begin the game

    protected Rect exitButtonRect; //the exit game button dimensions
    protected ReleaseButton exitButton; //exit button the user presses to exit the game

    protected Rect logoRect; //contains the game logo

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;


    protected Input mInput; //user input on the menu
    protected List<TouchEvent> touchEvents;

    public MenuScreen(Game game) {
        super("MainMenu", game);

        loadMenuBitmaps(); //load bitmaps needed for the various bitmaps used (buttons and logo)


        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        //Declaring the dimensions for the various Bitmap
        playButtonRect = new Rect((int) mLayerViewport.halfWidth-200, (int) mLayerViewport.halfHeight+300, (int) mLayerViewport.halfWidth+200, (int) mLayerViewport.halfHeight+400);
        logoRect = new Rect((int) mLayerViewport.halfWidth-800, (int) mLayerViewport.halfHeight-600, (int) mLayerViewport.halfWidth+800, (int) mLayerViewport.halfHeight);
        exitButtonRect = new Rect((int) mLayerViewport.getWidth()-110, 10, (int) mLayerViewport.getWidth()-10, 110);

        //the exit and play button constructed with the dimensions above and the bitmaps loaded
        exitButton = new ReleaseButton(exitButtonRect.exactCenterX(), exitButtonRect.exactCenterY(), exitButtonRect.width(), exitButtonRect.height(), "EXIT", "EXIT", "", this);
        playButton = new ReleaseButton(playButtonRect.exactCenterX(), playButtonRect.exactCenterY(), playButtonRect.width(), playButtonRect.height(), "PLAY", "PLAY", "", this);

        background = mGame.getAssetManager().getBitmap("BACKGROUND");
        logo = mGame.getAssetManager().getBitmap("LOGO");
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        //updates the toggle status of the buttons (whether the user has touched them)
        playButton.update(elapsedTime);
        exitButton.update(elapsedTime);

        //retrieve the input from the user
        mInput = mGame.getInput();
        //store the inputs
        touchEvents = mInput.getTouchEvents();

        touchButton(touchEvents);
    }

    //checks if one of the menu buttons is pressed
    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (playButton.pushTriggered()) { //the status of the button is 'pressed'
                    mGame.getScreenManager().removeScreen(this.getName());
                    GridLevel gLevel = new GridLevel(mGame);
                    mGame.getScreenManager().addScreen(gLevel);
                } else if (exitButton.pushTriggered()) {
                    //the user has chosen to exit the game, the game exits.
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap,
        exitButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport); //exit button
        playButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport); //play button
        graphics2D.drawBitmap(logo, null, logoRect, null); //the logo
    }

    //load the bitmaps needed to construct the interactive and non-interactive buttons
    public void loadMenuBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("BACKGROUND", "img/MenuImages/MenuBackground2.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAY", "img/MenuImages/PlayButton.png");
        getGame().getAssetManager().loadAndAddBitmap("LOGO", "img/MenuImages/Logo.png");
        getGame().getAssetManager().loadAndAddBitmap("EXIT", "img/MenuImages/ExitButton.png");
    }
}
