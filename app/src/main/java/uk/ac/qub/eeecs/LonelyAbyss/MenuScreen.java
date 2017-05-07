package uk.ac.qub.eeecs.LonelyAbyss;

import android.graphics.Bitmap;

import android.graphics.Rect;


import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by JP on 04/05/2017
 */
//Music: http://www.purple-planet.com
public class MenuScreen extends GameScreen {
    protected Music menuMusic;
    protected Sound buttonClick;

    protected Bitmap background; // the background of the menu screen

    protected Bitmap logo; //the logo of the game

    /*protected Bitmap uWotMate;
    protected Rect uWotMateRect;
    protected ReleaseButton uWotMateButton;*/

    //protected Rect rulesButtonRect;
    //protected ReleaseButton rulesButton;

    protected Rect backgroundRect; //background of the menu screen

    protected Rect playButtonRect; //the play button dimensions
    protected ReleaseButton playButton; //play button the user to presses to begin the game

    protected Rect exitButtonRect; //the exit game button dimensions
    protected ReleaseButton exitButton; //exit button the user presses to exit the game



    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;


    protected Input mInput; //user input on the menu
    protected List<TouchEvent> touchEvents;

    public MenuScreen(Game game) {
        super("MainMenu", game);

        loadMenuBitmaps(); //load bitmaps needed for the various bitmaps used (buttons and logo)
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        //Declaring the dimensions for the backgrounds
        backgroundRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height);

        loadMenuBitmaps();
        generateButtonsDimen();
        loadButtons();
        accessBitmaps();
        loadMusic();

        //play the menu music
        menuMusic.setVolume(10);
        menuMusic.play();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        //updates the toggle status of the buttons (whether the user has touched them)
        playButton.update(elapsedTime);
        exitButton.update(elapsedTime);
        //rulesButton.update(elapsedTime);

        //retrieve the input from the user
        mInput = mGame.getInput();
        //store the inputs
        touchEvents = mInput.getTouchEvents();

        touchButton(touchEvents);
    }

    //checks if one of the menu buttons is pressed
    private void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (playButton.pushTriggered()) { //the status of the button is 'pressed'
                    handlePlayButtonTouch();
                } /*else if (rulesButton.pushTriggered()) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    RulesScreen RS = new RulesScreen(mGame);
                    mGame.getScreenManager().addScreen(RS);
                    menuMusic.stop();
                    buttonClick.play();
                } */ else if (exitButton.pushTriggered()) {
                    //the user has chosen to exit the game, the game exits.
                    System.exit(0);
                }
            }
        }
    }

    //James Bailey 40156063
    //handle the player touching the playbutton, transitioning to loading the various elements of the playarea.
    private void handlePlayButtonTouch() {
        mGame.getScreenManager().removeScreen(this.getName());
        LoadingScreen lScreen = new LoadingScreen(mGame);
        mGame.getScreenManager().addScreen(lScreen);
        menuMusic.stop();
        buttonClick.play();
    }

    private void generateButtonsDimen() {
        int playLeftDimen = mScreenViewport.width/50;
        int playTopDimen = (int) (mScreenViewport.height*0.75)-mScreenViewport.height/3;
        int playRightDimen = (mScreenViewport.width/3);
        int playBottomDimen = (int) (mScreenViewport.height*0.6)+mScreenViewport.height/12;
        playButtonRect = new Rect(playLeftDimen, playTopDimen, playRightDimen, playBottomDimen);


        int exitLeftDimen = mScreenViewport.width - mScreenViewport.width / 18;
        int exitTopDimen = 0;
        int exitRightDimen = mScreenViewport.width;
        int exitBottomDimen = mScreenViewport.height / 12;
        exitButtonRect = new Rect(exitLeftDimen, exitTopDimen, exitRightDimen, exitBottomDimen);

       /* int rulesLeftDimen = (int) (mScreenViewport.width / 1.5);
        int rulesTopDimen = (int) (mScreenViewport.height * 0.75) - mScreenViewport.height / 8;
        int rulesRightDimen = (int) (mScreenViewport.width - 100);
        int rulesBottomDimen = (int) (mScreenViewport.height * 0.75) + mScreenViewport.height / 8;
        rulesButtonRect = new Rect(rulesLeftDimen, rulesTopDimen, rulesRightDimen, rulesBottomDimen); */

        /*exitLeftDimen = 0;
        exitTopDimen = mScreenViewport.width - 1000;
        exitRightDimen = mScreenViewport.width;
        exitBottomDimen = mScreenViewport.height / 12;
        uWotMateRect = new Rect(exitLeftDimen, exitTopDimen, exitRightDimen, exitBottomDimen);*/
    }

    private void loadButtons() {
        //the exit and play button constructed with the dimensions above and the bitmaps loaded
        exitButton = new ReleaseButton(exitButtonRect.exactCenterX(), exitButtonRect.exactCenterY(), exitButtonRect.width(), exitButtonRect.height(), "EXIT", "EXIT", "", this);
        playButton = new ReleaseButton(playButtonRect.exactCenterX(), playButtonRect.exactCenterY(), playButtonRect.width(), playButtonRect.height(), "PLAY", "PLAY", "", this);
        playButton.setEnableGlow(false);
        // rulesButton = new ReleaseButton(rulesButtonRect.exactCenterX(), rulesButtonRect.exactCenterY(), rulesButtonRect.width(), rulesButtonRect.height(), "PLAY", "PLAY", "", this);
        //uWotMateButton = new ReleaseButton(uWotMateRect.exactCenterX(), uWotMateRect.exactCenterY(), uWotMateRect.width(), uWotMateRect.height(), "EXIT", "EXIT", "", this);

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap,
        //  graphics2D.drawBitmap(logo, null, logoRect, null); //the logo
        exitButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport); //exit button
        playButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport); //play button
      //  rulesButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
       //uWotMateButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);


    }

    //load the bitmaps needed to construct the interactive and non-interactive buttons
    private void loadMenuBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("BACKGROUND", "img/Backgrounds/backgroundNew.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAY", "img/MenuImages/PlayButton.png");
       // getGame().getAssetManager().loadAndAddBitmap("LOGO", "img/MenuImages/Logo.png");
        getGame().getAssetManager().loadAndAddBitmap("EXIT", "img/MenuImages/ExitButton.png");
        //  getGame().getAssetManager().loadAndAddBitmap("PLAY", "img/MenuImages/PlayButton.png");
    }

    public void loadMusic() {
        getGame().getAssetManager().loadAndAddMusic("MENUMUSIC", "Music/MenuMusic.mp3");
        menuMusic = mGame.getAssetManager().getMusic("MENUMUSIC");

        getGame().getAssetManager().loadAndAddSound("BUTTON", "Sounds/button.mp3");
        buttonClick = mGame.getAssetManager().getSound("BUTTON");
    }

    public void accessBitmaps() {
        background = mGame.getAssetManager().getBitmap("BACKGROUND");
        //logo = mGame.getAssetManager().getBitmap("LOGO");
        //uWotMate = mGame.getAssetManager().getBitmap("LOGO");
    }


}
