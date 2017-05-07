package uk.ac.qub.eeecs.LonelyAbyss;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

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
 * Created by JP on 30/04/2017.
 */

public class SplashScreen extends GameScreen {
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


    Paint logoPaint = new Paint();
    Paint cbLogoPaint = new Paint();
    int alphaX;
    protected Music splashMusic;
    protected Input mInput; //user input on the menu
    protected List<TouchEvent> touchEvents;

    public SplashScreen(Game game) {
        super("SplashScreen", game);
        loadAssets();
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        //Declaring the dimensions for the backgrounds
        backgroundRect = new Rect(0, 0, mScreenViewport.width,  mScreenViewport.height);
        splashMusic.play();
        splashMusic.isLooping();
        Thread waitThread = new Thread(new wait());
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
        MenuScreen MS = new MenuScreen(mGame);
        mGame.getScreenManager().addScreen(MS);
        splashMusic.stop();
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

    }

    public void logoFlicker(IGraphics2D graphics2D){
        logoRect = new Rect(logoLeftDimen, logoTopDimen, logoRightDimen, logoBottomDimen);
        logoRect2 = new Rect(logoLeftDimen, logoTopDimen+100, logoRightDimen, logoBottomDimen);
        graphics2D.drawBitmap(logo, null, logoRect, null); //the logo
        graphics2D.drawBitmap(logo, null, logoRect2, logoPaint);
        Thread flicker = new Thread(new splashDisplay());
        flicker.start();
        graphics2D.drawBitmap(logo, null, logoRect2, logoPaint);


    }

    public void showComebackLogo(IGraphics2D graphics2D){
        cbLogoRect = new Rect(logoLeftDimen+150, 700, logoRightDimen-150, 1000);
        graphics2D.drawBitmap(comebackLogo, null, cbLogoRect, cbLogoPaint);
    }




    class splashDisplay implements Runnable{
        @Override
        public void run() {
            try{
                logoPaint.setAlpha(20);
                Thread.sleep(100);
                logoPaint.setAlpha(300);

            }catch (InterruptedException e){

            }
        }
    }


    public void loadAssets(){
        getGame().getAssetManager().loadAndAddBitmap("SplashBack", "img/MenuImages/black.png");
        background = mGame.getAssetManager().getBitmap("SplashBack");
        getGame().getAssetManager().loadAndAddBitmap("LOGO", "img/MenuImages/Logo.png");
        logo = mGame.getAssetManager().getBitmap("LOGO");
        getGame().getAssetManager().loadAndAddMusic("SplashMusic", "Music/film-reel.mp3");
        splashMusic = mGame.getAssetManager().getMusic("SplashMusic");
        getGame().getAssetManager().loadAndAddBitmap("CBSTUDIOS", "img/Backgrounds/CBStudios.png");
        comebackLogo = mGame.getAssetManager().getBitmap("CBSTUDIOS");
    }

    class wait implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                changeScreen();
            } catch (InterruptedException e) {
                System.out.println("splash wait fail");
            }
        }
    }
}
