package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Rect;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.CustomGage.State;

/**
 * Created by JP on 04/05/2017.
 */

public class DeadState extends State {

    protected PlayScreen playScreen;
    protected Bitmap backgroundDead;
    protected Rect backgroundRect;
    protected Rect logoRect;
    protected Bitmap skull;
    protected Bitmap logo;
    protected Rect skullRect;
    protected Music deadMusic;



    public DeadState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);


        this.playScreen = (PlayScreen) mGameScreen;
        music();

    }


    @Override
    public void update(ElapsedTime elapsedTime) {

    }
    class transition implements Runnable{

        @Override
        public void run() {

           if (active){

                try{
                    //deadMusic.stop();
                    Thread.sleep(3500);
                  // deadMusic.stop();
                    active = false;
                    playScreen.getPlayOverviewState().refresh();
                    playScreen.getActiveUnimonState().refresh();
                    playScreen.getBenchState().refresh();

                    playScreen.getPlayOverviewState().active = true;
                    playScreen.getPlayOverviewState().touchActive = true;
                }catch (InterruptedException e){
                }
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        loadBackground(graphics2D);
        loadLogo(graphics2D);
        /*Thread skullThread = new Thread(new skullTimer());
        skullThread.start();*/
        loadSkull(graphics2D);
       // moveSkull(graphics2D);
        Thread thread = new Thread(new transition());
        thread.start();
    }

    public void loadBackground(IGraphics2D graphics2D){
        mGame.getAssetManager().loadAndAddBitmap("DEADBACK", "img/Backgrounds/blackDead.png");
        backgroundDead = mGame.getAssetManager().getBitmap("DEADBACK");
        backgroundRect = new Rect(0, 0, mScreenViewport.width,  mScreenViewport.height);
        graphics2D.drawBitmap(backgroundDead, null, backgroundRect, null);
        music();

       // deadMusic.play();
    }

    public void loadLogo(IGraphics2D graphics2D)
    {
        mGame.getAssetManager().loadAndAddBitmap("LOGO", "img/MenuImages/Logo.png");
        logoRect = new Rect(mScreenViewport.width/8, mScreenViewport.height/8, mScreenViewport.width-mScreenViewport.width/8, mScreenViewport.height/8+mScreenViewport.height/4);
        logo = mGame.getAssetManager().getBitmap("LOGO");
        graphics2D.drawBitmap(logo, null, logoRect, null);

    }
    protected int left = 525;
    protected int top = mScreenViewport.height/3+100;
    protected int right = 1250;
    protected int bottom = 1000;

    public void loadSkull(IGraphics2D graphics2D)
    {

        mGame.getAssetManager().loadAndAddBitmap("SKULL", "img/Backgrounds/skull.png");
        skull = mGame.getAssetManager().getBitmap("SKULL");
        skullRect = new Rect (left, top, right, bottom);
        graphics2D.drawBitmap(skull, null, skullRect, null);

    }
/*
    public void moveRight(IGraphics2D graphics2D){

        left= left + 20;
        right= right + 20;
        skullRect.set(left, top, right, bottom);
        graphics2D.drawBitmap(skull, null, skullRect, null);

        if(right<mScreenViewport.width - 200 ){
            moveSkull(graphics2D);
        }
       // moveSkull(graphics2D);
    }

    public void moveLeft(IGraphics2D graphics2D){


            skullRect.set(left, top, right, bottom);
            graphics2D.drawBitmap(skull, null, skullRect, null);

    }

    public void moveSkull(IGraphics2D graphics2D){

        moveRight(graphics2D);
            *//*if(left>300){
                moveLeft(graphics2D);
            } else if(left>=300){
                moveRight(graphics2D);
            }*//*


    }*/

    public void music(){
        mGame.getAssetManager().loadAndAddMusic("YOUREDEAD", "Music/deadNotSurprise.mp3");
        deadMusic = mGame.getAssetManager().getMusic("YOUREDEAD");
        //deadMusic.play();
       /* try{
            deadMusic.play();
            Thread.sleep(3500);
            deadMusic.stop();
        }catch (InterruptedException e){

        }*/

        //deadMusic.stop();
    }

}
