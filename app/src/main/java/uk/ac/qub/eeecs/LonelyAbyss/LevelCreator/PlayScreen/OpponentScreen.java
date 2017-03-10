package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;



/**
 * Created by Jordan and Kyle on 09/03/2017.
 */

        import android.graphics.Bitmap;
        import android.graphics.Paint;
        import android.graphics.Rect;

        import java.security.interfaces.ECKey;
        import java.util.ArrayList;
        import java.util.List;

        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
        import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
        import uk.ac.qub.eeecs.gage.Game;
        import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
        import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
        import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
        import uk.ac.qub.eeecs.gage.ui.ReleaseButton;
        import uk.ac.qub.eeecs.gage.world.GameScreen;
        import uk.ac.qub.eeecs.gage.world.LayerViewport;
        import uk.ac.qub.eeecs.gage.world.ScreenViewport;
        import uk.ac.qub.eeecs.gage.world.State;

public class OpponentScreen extends State {
    UnimonCard OpponentActiveCard; // Opponent card in the active slot

    ReleaseButton back; // a button to go back to the main screen
    ReleaseButton showDetailsButton; // a button to show deatils od the active unimon

    public OpponentScreen(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadDetailsBitmap();
        generateDetailsButton();
        loadTestCard();
    }

    @Override

    public void update(ElapsedTime elapsedTime) {
        if (active) {
            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();

            touchButton(touchEvents);
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





    //loads test card
    public void loadTestCard() {
        mGame.getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Demon Slayer.png");
        mGame.getAssetManager().loadAndAddBitmap("BLACK", "img/Particles/black.png");
        Bitmap cardImage = selectBitmap("CARD");
        OpponentActiveCard = new UnimonCard((mScreenViewport.width/2), (mScreenViewport.height/2), (mScreenViewport.width/2.3f), (int)(mScreenViewport.height), cardImage, mGameScreen,
                "0", null, null, null, "Earth Dragon",
                UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                20 ,30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);

    }







}



