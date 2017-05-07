package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;



import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.CustomGage.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.CustomGage.State;

/**
 * Created by Patrick on 04/05/2017.
 */

public class CoinState extends State {

    //The States of the a coin.
    public enum CoinSide{
        HEADS,TAILS
    }

    private ReleaseButton headsCoinButton;  //button of the head side of a coin
    private ReleaseButton tailsCoinButton;  //button of the tails side of a coin
    PlayScreen playScreen;  //the playScreen
    private CoinSide coinSide;  //to hold the coinSide randomly selected
    private CoinSide playersChoice; //the coinSide the user selected
    private boolean notSelected = true; //a boolean to say weather a user has selected heads or tails

    private Thread coinFlipThread; //to control the screen after the flip has taken place


    //Constructor for the State to set the viewPort and call methods to load Coin Bitmaps on the screen.
    public CoinState(ScreenViewport mScreenViewport, LayerViewport mLayerViewport, Game mGame, GameScreen mGameScreen,boolean active){
        super(mScreenViewport, mLayerViewport, mGame, mGameScreen, active);
        playScreen = (PlayScreen)mGameScreen;
        loadCoinBitmaps();
        loadCoins();
        DrawAssist.showMessage(mGame,"Select HEADS or TAILS");
        coinFlipThread = new Thread(new coinResultRunnable());
    }

    //To control any response on touch and check for change.
    //@param elapsedTime - the time elapsed from the last update
    @Override
    public void update(ElapsedTime elapsedTime) {
        if(notSelected) {
            headsCoinButton.update(elapsedTime);
            tailsCoinButton.update(elapsedTime);
        }else{
            updateAfterSelection(elapsedTime);
        }
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        touchButton(touchEvents);
    }

    /**
     * Checks if randomly selected coinSide is equal to Heads or tails and updates in respect to this.
     * @param elapsedTime - time elapsed from last update
     */
    public void updateAfterSelection(ElapsedTime elapsedTime){
        if(coinSide == CoinSide.HEADS){
            headsCoinButton.update(elapsedTime);
        }else{
            tailsCoinButton.update(elapsedTime);
        }
    }


    @Override
    /**
     * Checks if a coin has been selected and if so centers the buttons. Either way calls the draw method on the Buttons
     * @param elapsedTime - time elapsed from last update
     * @param graphics2D - drawing to the canvas
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (active) {
            DrawAssist.drawBlackBackground(mScreenViewport, graphics2D);
            if(notSelected) {
                headsCoinButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
                tailsCoinButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
            }else{
                centerCoins();
                drawAfterSelection(elapsedTime,graphics2D);
            }
        }
    }

    /**
     * Checks if randomly selected coinSide is equal to Heads or tails and calls the draw method on the buttons in respect to this.
     * @param elapsedTime - time elapsed from last update
     * @param graphics2D - drawing to the canvas
     */
    public void drawAfterSelection(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        if(coinSide == CoinSide.HEADS){
            headsCoinButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }else{
            tailsCoinButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }
    }


    /**
     * Checks if randomly selected coinSide is equal to Heads or tails and calls the draw method on the buttons in respect to this.
     * @return - 0 or 1 depending on the random outcome. 1 if the ranNumber is greater than or equal to 0.5 and 1 if less than.
     */
    public int selectingSideOnRandom(){
        Random rand = new Random();
        float ranNumber = rand.nextFloat();
        if(ranNumber >= 0.5){
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * Checks if the Random value and sets the coinSide variable to the required size
     */
    public void generatingSide(){
        if(selectingSideOnRandom() == 1){
            coinSide = CoinSide.HEADS;
        }else{
            coinSide = CoinSide.TAILS;
        }
    }

    /**
     * Checks for touch and then calls the method associated with the touch.
     * Also starts the coinFlipThread to start running.
     * @param touchEvents - List of touchEvents that will be iterated through.
     */
    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (headsCoinButton.pushTriggered()){ //the status of the button is 'pressed'
                    notSelected = false;
                    DrawAssist.clearMessage();
                    playersChoice = CoinSide.HEADS;
                    generatingSide();
                    coinFlipThread.start();
                    mInput.resetAccumulators();
                    break;
                } else if (tailsCoinButton.pushTriggered()) {
                    notSelected = false;
                    DrawAssist.clearMessage();
                    playersChoice = CoinSide.TAILS;
                    generatingSide();
                    coinFlipThread.start();
                    mInput.resetAccumulators();
                    break;
                }
            }
        }
    }

    /**
     *To run the transitionMethod and sleep the thread for 4sec so the user can view the result of the coin flip
     */
    class coinResultRunnable implements Runnable {
        @Override
        public void run() {
            whoGoesFirst();
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingFirstMovePlayer(); ;
        }
    }

    /**
     * Displays a toast, informing the user who gets the first move;
     */
    public void whoGoesFirst(){
        if(playersChoice == coinSide) {
            DrawAssist.showMessage(mGame, "YOU go first");
        }else {
            DrawAssist.showMessage(mGame,"OPPONENT goes first");
        }
    }


    /**
     * Sets the current states activity to false and sets the next states activity to true;
     */
    public void loadingFirstMovePlayer(){
        if(playersChoice == coinSide){
            DrawAssist.clearMessage();
            playScreen.getCoinState().active = false;
            playScreen.getPlayOverviewState().active = true;
            playScreen.getPlayOverviewState().showInitialHelpMessage();
        }else{
            DrawAssist.clearMessage();
            playScreen.getCoinState().active = false;
            playScreen.getPlayOverviewState().active = true;
            playScreen.getPlayOverviewState().showInitialHelpMessage();
        }
    }

    /**
     * Sets the Buttons locations on the screen and loads the Bitmaps to the buttons
     */
    public void loadCoins() {
        headsCoinButton = new ReleaseButton(mScreenViewport.width/4, mScreenViewport.height/2, mScreenViewport.width/2, mScreenViewport.height/1.5f, "CoinHeads", "CoinHeads", "", playScreen);
        headsCoinButton.setEnableGlow(false);
        tailsCoinButton = new ReleaseButton(((mScreenViewport.width/4)*3),mScreenViewport.height/2,mScreenViewport.width/2,mScreenViewport.height/1.5f,"CoinTails", "CoinTails","",playScreen);
        tailsCoinButton.setEnableGlow(false);
    }


    /**
     * Sets the Button locations to the center of the screen and loads the Bitmaps to the buttons
     */
    public void centerHeadsCoin(){
        headsCoinButton = new ReleaseButton(mScreenViewport.centerX(), mScreenViewport.centerY(), mScreenViewport.centerX(), mScreenViewport.centerX(), "CoinHeads", "CoinHeads", "", playScreen);
        headsCoinButton.setEnableGlow(false);
    }

    /**
     * Sets the Button locations to the center of the screen and loads the Bitmaps to the buttons
     */
    public void centerTailsCoin(){
        tailsCoinButton = new ReleaseButton(mScreenViewport.centerX(), mScreenViewport.centerY(), mScreenViewport.centerX(), mScreenViewport.centerX(), "CoinTails", "CoinTails", "", playScreen);
        tailsCoinButton.setEnableGlow(false);
    }

    /**
     * Calls the center methods for the buttons
     */
    public void centerCoins(){
        centerHeadsCoin();
        centerTailsCoin();
    }

    /**
     * loads the Bitmaps and adds a tag.
     */
    public void loadCoinBitmaps(){
        playScreen.getGame().getAssetManager().loadAndAddBitmap("CoinHeads", "img/DeckManagement/CoinHeads.png");
        playScreen.getGame().getAssetManager().loadAndAddBitmap("CoinTails", "img/DeckManagement/CoinTails.png");

    }
}
