package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.MenuScreen;
import uk.ac.qub.eeecs.LonelyAbyss.SplashScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by JP on 07/05/2017.
 */

public class THXScreen extends GameScreen {

    protected LayerViewport mLayerViewport;
    protected ScreenViewport mScreenViewport;
    protected Rect backgroundRect;
    protected Rect logoRect;
    protected Rect logoRect2;
    protected Rect cbLogoRect;
    protected Bitmap background;
    protected  Bitmap logo;
    protected Bitmap comebackLogo;
    int logoTopDimen, logoLeftDimen, logoRightDimen, logoBottomDimen;
    int alpha = 0;


    Paint logoPaint = new Paint();
    Paint cbLogoPaint = new Paint();

    protected Music thxMusic;
    protected Input mInput; //user input on the menu
    protected List<TouchEvent> touchEvents;

    public THXScreen(Game game) {
        super("THX", game);
        loadAssets();
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        //Declaring the dimensions for the backgrounds
        backgroundRect = new Rect(0, 0, mScreenViewport.width,  mScreenViewport.height);
        thxMusic.play();
        thxMusic.isLooping();
        Thread waitThread = new Thread(new THXScreen.wait());
        waitThread.start();

    }

    /*public void touchScreen(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                    mGame.getScreenManager().removeScreen(this.getName());
                MenuScreen MS = new MenuScreen(mGame);
                mGame.getScreenManager().addScreen(MS);
                splashMusic.dispose();
            }
        }
    }*/

    public void changeScreen(){
        mGame.getScreenManager().removeScreen(this.getName());
        SplashScreen SS  = new SplashScreen(mGame);
        mGame.getScreenManager().addScreen(SS);
        thxMusic.stop();
    }
    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        //store the inputs
        touchEvents = mInput.getTouchEvents();
        //touchScreen(touchEvents);

    }



    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(background, null, backgroundRect, null); //draw the background bitmap,
        logoLeftDimen = mScreenViewport.width/8;
        logoRightDimen = mScreenViewport.width-mScreenViewport.width/8;
        logoBottomDimen = logoTopDimen+(mScreenViewport.height/3);
        logoTopDimen = mScreenViewport.height/4+30;
        //showComebackLogo(graphics2D);
        logoFlicker(graphics2D);

        //showComebackLogo(graphics2D);

    }

    public void logoFlicker(IGraphics2D graphics2D){
        //logoRect = new Rect(logoLeftDimen, logoTopDimen, logoRightDimen, logoBottomDimen);
        cbLogoRect = new Rect(logoLeftDimen+150, logoTopDimen, logoRightDimen-150, logoBottomDimen);

            Thread flicker = new Thread(new THXScreen.thxDisplay());
            flicker.start();
            graphics2D.drawBitmap(comebackLogo, null, cbLogoRect, logoPaint);
    }

    public void showComebackLogo(IGraphics2D graphics2D){
        graphics2D.drawBitmap(comebackLogo, null, cbLogoRect, cbLogoPaint);
    }




    class thxDisplay implements Runnable {
        @Override
        public void run() {

                try {
                    logoPaint.setAlpha(alpha);
                    Thread.sleep(14000);
                    alpha = alpha + 2;
                    Thread.sleep(2000);
                    logoPaint.setAlpha(alpha);

                    //  logoPaint.setAlpha(alpha);
                    //alpha= alpha +10;
                    //Thread.sleep(10000);

                } catch (InterruptedException e) {

                }

        }
    }

        public void loadAssets() {
            getGame().getAssetManager().loadAndAddBitmap("SplashBack", "img/MenuImages/black.png");
            background = mGame.getAssetManager().getBitmap("SplashBack");
        /*getGame().getAssetManager().loadAndAddBitmap("LOGO", "img/MenuImages/Logo.png");
        logo = mGame.getAssetManager().getBitmap("LOGO");*/
            getGame().getAssetManager().loadAndAddMusic("THX", "Music/thxsound.mp3");
            thxMusic = mGame.getAssetManager().getMusic("THX");
            getGame().getAssetManager().loadAndAddBitmap("CBSTUDIOS", "img/Backgrounds/CBStudios.png");
            comebackLogo = mGame.getAssetManager().getBitmap("CBSTUDIOS");
        }

        class wait implements Runnable {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                    changeScreen();
                } catch (InterruptedException e) {
                    System.out.println("splash wait fail");
                }
            }
        }
    }
