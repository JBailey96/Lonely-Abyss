package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;



/**
 * Created by Jordan and Kyle on 09/03/2017.
 */

        import android.graphics.Bitmap;
        import android.graphics.Paint;
        import android.graphics.Rect;

        import java.util.List;

        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Battle;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
        import uk.ac.qub.eeecs.gage.Game;
        import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
        import uk.ac.qub.eeecs.gage.engine.audio.Music;
        import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
        import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
        import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
        import uk.ac.qub.eeecs.gage.world.GameScreen;
        import uk.ac.qub.eeecs.gage.world.LayerViewport;
        import uk.ac.qub.eeecs.gage.world.ScreenViewport;
        import uk.ac.qub.eeecs.gage.CustomGage.State;

public class OpponentState extends State {

    public enum OpponentStateType { //the statetypes the state can have - control the validity of a user's input primarily
        VIEW_OPPONENT, MOVE_APPLIED
    }

    protected OpponentStateType opponentStateType; //the current state type of the state

    protected UnimonCard OpponentActiveCard; // Opponent card in the active slot

    ReleaseButton back; // a button to go back to the main screen
    ReleaseButton showDetailsButton; // a button to show deatils od the active unimon



    protected PlayScreen playScreen;
    protected BattleSetup battleSetup;

    protected UnimonCard playerCard; //the player's card they can use to apply moves onto the opponent car dpresented

    protected int opponentCardHealthBefore; //the health of the opponent's card before a move has been applied.
    protected int opponentCardHealthAfter; //the health after the player has applied a move.

    //J Devlin 40150554
    protected Music heartBeat;
    protected Music deadMusic;
    boolean decreasingHealth = false;
    protected Bitmap bloodSplatter;
    protected Rect bloodSplatterRect;
    protected Paint bloodSplatterPaint;

    public OpponentState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, BattleSetup battleSetup, Boolean active) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);

        this.playScreen = (PlayScreen) mGameScreen;
        this.battleSetup = battleSetup;

        loadDetailsBitmap();
        generateDetailsButton();
        loadTestCard();

        opponentStateType = OpponentStateType.VIEW_OPPONENT; //initial state is to view the opponent.
    }

    @Override

    public void update(ElapsedTime elapsedTime) {
        if (active) {
            if (opponentStateType == OpponentStateType.VIEW_OPPONENT) {
                mInput = mGame.getInput();
                touchEvents = mInput.getTouchEvents();
                touchButton(touchEvents);
            }
        }
    }

    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                touchOpponentActiveCard(t);
            }
        }
    }

    public void touchOpponentActiveCard(TouchEvent t){
        if(!(OpponentActiveCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))){
            active = false;
        }
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        if(active){
            drawActive(elapsedTime, graphics2D);
        }
        showBloodSplatter(graphics2D);
    }

    //draw opponents active unimon and show deatils button
    public void drawActive(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        drawOpponentsActive(elapsedTime, graphics2D);
        drawDetailsButton(elapsedTime, graphics2D);
    }

    public void drawOpponentsActive(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        Paint myPaint = new Paint();
        Rect paintRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height);
        myPaint.setAlpha(200); // transparent background


        graphics2D.drawBitmap(selectBitmap("BLACK"), null, paintRect, myPaint);
        OpponentActiveCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    public void drawDetailsButton(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        showDetailsButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //generate the button

    public void generateDetailsButton(){
        float x = mScreenViewport.width*0.15f;
        float y = mScreenViewport.height/2 - mScreenViewport.height/6;
        float width =  mScreenViewport.width/5;
        float height = mScreenViewport.height/8;

        showDetailsButton = new ReleaseButton(x, y, width, height, "DETAILSBUTTON", "DETAILSBUTTON", "", mGameScreen);
    }


    //Load bitmap for detailsButton

    public void loadDetailsBitmap(){
        mGame.getAssetManager().loadAndAddBitmap("DETAILSBUTTON","img/PlayScreenButtons/card_details.png");
    }

    //James Bailey 40156063
    //Applies the active unimon move to the opponent's card
    public void applyMove(UnimonCard playerCard, UnimonMoves unimonMove) {
        this.playerCard = playerCard; //stores a reference to the player card - used to set the battlesetup

        opponentCardHealthBefore = OpponentActiveCard.getHealth(); //store the opponent's health before the player's move is applied on it
        Battle.applyMove(playerCard, OpponentActiveCard, unimonMove); //apply the player's move to the opponent active card
        opponentCardHealthAfter = OpponentActiveCard.getHealth(); //store the opponent's health after the move is applied

        //set the health of the opponent to what it was before the move is applied
        OpponentActiveCard.setHealth(opponentCardHealthBefore);

        Thread thread = new Thread(new DecreaseHealth());
        thread.start();

        decreaseHealthSound();
        decreasingHealth = true;

    }

    //J Devlin 40150554
    public void decreaseHealthSound(){
        mGameScreen.getGame().getAssetManager().loadAndAddMusic("EKGHEART", "Sounds/ekgHeart.mp3");
        heartBeat = mGameScreen.getGame().getAssetManager().getMusic("EKGHEART");
        heartBeat.play();
        heartBeat.setLopping(true);
        heartBeat.isLooping();

    }
    //J Devlin 40150554

    public void showBloodSplatter(IGraphics2D graphics2D){
        if( decreasingHealth){
            mGameScreen.getGame().getAssetManager().loadAndAddBitmap("BLOODSPLATTER", "img/Backgrounds/bloodSplatter.png");
            bloodSplatter = mGameScreen.getGame().getAssetManager().getBitmap("BLOODSPLATTER");
            bloodSplatterPaint = new Paint();
            bloodSplatterPaint.setAlpha(60);
            bloodSplatterRect = new Rect (0, 0, mScreenViewport.width, mScreenViewport.height);
            graphics2D.drawBitmap(bloodSplatter, null, bloodSplatterRect, bloodSplatterPaint);
            /*Thread bloodSplatterThread = new Thread(new splatterFade());
            bloodSplatterThread.start();
            graphics2D.drawBitmap(bloodSplatter, null, bloodSplatterRect, bloodSplatterPaint);*/
        }
    }

    //J Devlin 40150554
     /*class splatterFade implements Runnable{
        @Override
        public void run() {
            try
            {
                   bloodSplatterPaint.setAlpha(150);
                Thread.sleep(10);
                    bloodSplatterPaint.setAlpha(50);
            }catch (InterruptedException e){
            }
        }
    }*/

    //James Bailey 40156063
    //Thread that decreases the health of the opponent card gradually to present the impact of the player's move
    class DecreaseHealth implements Runnable {
        @Override
        public void run() {
            try {
                final int totalTimeInMiliSeconds = 4000; //the time to show the decrease in health to the player
                int healthDifference = opponentCardHealthBefore-opponentCardHealthAfter; //the damage the move did to the opponent's health

                if (healthDifference != 0) { //validates whether the health actually did some damage
                    int timeEachDecrement = totalTimeInMiliSeconds/healthDifference; //the time for each decrement in health

                    //decreases health until it reaches the opponent's health after the move.
                    for (int i = opponentCardHealthBefore; i >= opponentCardHealthAfter; i--) {
                        OpponentActiveCard.decreaseHealth(1);
                        OpponentActiveCard.setPositionChanged(true); //update the stat bar dimensions
                        Thread.sleep(timeEachDecrement);
                    }
                    battleSetup.setActiveCard(playerCard); //set the battlesetup's active card to the now updated health's card
                }
                heartBeat.setLopping(false);
                Thread.sleep(2000); //provides the user a short time to view the health of the opponent after the move.

                active = false;

                //J Devlin 40150554: Transitioning to the dead notification screen.
                deadStateTransition();

                //need to refresh states that use the player's active unimon card - stats changed
                playScreen.getPlayOverviewState().refresh();
                playScreen.getActiveUnimonState().refresh();
                playScreen.getBenchState().refresh();

                playScreen.battleSong.play();

                //present the playoverview state to the user again and allow the user to interact with it
                playScreen.getPlayOverviewState().active = true;
                playScreen.getPlayOverviewState().touchActive = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //J Devlin 40150554: Transitioning to the dead notification screen.
    public void deadStateTransition(){
        if(opponentCardHealthAfter <= 0){
            playScreen.getDeadState().active = true;
            heartBeat.stop();
            mGame.getAssetManager().loadAndAddMusic("YOUREDEAD", "Music/deadNotSurprise.mp3");
            deadMusic = mGame.getAssetManager().getMusic("YOUREDEAD");
            playScreen.battleSong.stop();
            deadMusic.play();
        }
    }

    //loads test card
    public void loadTestCard() {
        mGame.getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Demon Slayer.png");
        mGame.getAssetManager().loadAndAddBitmap("BLACK", "img/Particles/black.png");
        Bitmap cardImage = selectBitmap("CARD");
        OpponentActiveCard = new UnimonCard((mScreenViewport.width / 2), (mScreenViewport.height / 2), (mScreenViewport.width / 2.3f), (mScreenViewport.height), cardImage, mGameScreen,
                "0", null, null, null, "Demon Slayer",
                UnimonEvolveType.DEMON, Element.EARTH, null, 250, 600, 700, "test Description",
                20, 2, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);
    }



}



