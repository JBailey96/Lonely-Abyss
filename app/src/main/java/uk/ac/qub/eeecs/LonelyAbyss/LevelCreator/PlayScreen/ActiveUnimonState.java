package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.ReleaseButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by appcamp on 08/03/2017.
 */

public class ActiveUnimonState extends State {
   // UnimonCard testActiveCard; //testCard used to test the active unimon view
    UnimonCard activeCard;

    protected final int numMoveButtons = 3; //the number of move buttons

    //Battle Option Buttons
    ReleaseButton applyEnergyButton;
    ReleaseButton evolveButton;
    ReleaseButton retreatButton;
    //Move battle options
    List<ReleaseButton> moveButtons = new ArrayList<>();

    public ActiveUnimonState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadButtonOptionBitmaps();
        generateBattleOptions();
        //loadTestCard();
        this.activeCard = battleSetup.getActiveCard().copyUnimonCard();
        loadActiveCard();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            activeCard.update(elapsedTime);
            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            touchButton(touchEvents);
        }
    }

    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                touchActiveUnimon(t);
            }
        }
    }

    //checks whether the active unimon has been touched
    public void touchActiveUnimon(TouchEvent t) {

        //load the energy card when the active unimon card is pressed
        if ((activeCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
            playScreen.getActiveEnergyState().active = true;

        }

        //hide the active unimon card when it outside the card is pressed
        if (!(activeCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            active = false;
        }

        //displaying the energy card when the energy button is pressed
            /*if (applyEnergyButton.isPushed()){
                PlayScreen playScreen = (PlayScreen) mGameScreen;
                playScreen.getActiveEnergyState().active = true;
            }*/
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (active) {
            drawActive(elapsedTime, graphics2D);
        }
    }

    //draw active unimon card and the battle options
    public void drawActive(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        drawActiveUnimon(elapsedTime, graphics2D);
        drawBattleOptionButtons(elapsedTime, graphics2D);
    }

    //draw active unimon card
    public void drawActiveUnimon(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        Rect paintRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height); //set background black semi-opaque background to fill whole screenviewport

        //create black background
        Bitmap blackBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        blackBitmap.eraseColor(Color.BLACK);

        Paint paint = new Paint();
        paint.setAlpha(200); //make the background behind the unimon card semi-opaque

        graphics2D.drawBitmap(blackBitmap, null, paintRect, paint);
        activeCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //method for drawing the battle option buttons
    public void drawBattleOptionButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (ReleaseButton moveButton : moveButtons) { //drawing the move buttons
            moveButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        //drawing the other buttons
        applyEnergyButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        evolveButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        retreatButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //generate battle button options
    public void generateBattleOptions() {
        generateEnergyButton();
        generateEvolveButton();
        generateRetreatButton();
        generateMoveButtons();
    }
    
    //generate the button to apply energy
    public void generateEnergyButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2 - mScreenViewport.height / 6;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        applyEnergyButton = new ReleaseButton(x, y, width, height, "ENERGYBUTTON", "ENERGYBUTTON", "", mGameScreen);
    }

    //generate the button to evolve the current active unimon
    public void generateEvolveButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        evolveButton = new ReleaseButton(x, y, width, height, "EVOLVEBUTTON", "EVOLVEBUTTON", "", mGameScreen);
    }

    //generate the button to retreat the current active unimon
    public void generateRetreatButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2 + mScreenViewport.height / 6;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        retreatButton = new ReleaseButton(x, y, width, height, "RETREATBUTTON", "RETREATBUTTON", "", mGameScreen);
    }

    //generate the buttons to use the card's moves
    public void generateMoveButtons() {
        float x = mScreenViewport.width * 0.85f;
        float y;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        for (int i = 0; i < numMoveButtons; i++) {
            y = mScreenViewport.height / 2 + ((i - 1) * mScreenViewport.height / 6);
            ReleaseButton unimonMoveButton = new ReleaseButton(x, y, width, height, "MOVE" + (i + 1) + "BUTTON", "MOVE" + (i + 1) + "BUTTON", "", mGameScreen);
            moveButtons.add(unimonMoveButton);
        }
    }

    //load the bitmaps for the battle option buttons.
    public void loadButtonOptionBitmaps() {
        mGame.getAssetManager().loadAndAddBitmap("ENERGYBUTTON", "img/PlayScreenButtons/button_energy (1).png");
        mGame.getAssetManager().loadAndAddBitmap("EVOLVEBUTTON", "img/PlayScreenButtons/button_evolve (1).png");
        mGame.getAssetManager().loadAndAddBitmap("RETREATBUTTON", "img/PlayScreenButtons/button_retreat (1).png");
        mGame.getAssetManager().loadAndAddBitmap("MOVE1BUTTON", "img/PlayScreenButtons/button_move-1.png");
        mGame.getAssetManager().loadAndAddBitmap("MOVE2BUTTON", "img/PlayScreenButtons/button_move-2.png");
        mGame.getAssetManager().loadAndAddBitmap("MOVE3BUTTON", "img/PlayScreenButtons/button_move-3.png");
    }

    /*//loads test card
    public void loadTestCard() {
        mGame.getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Demon Slayer.png");
        Bitmap cardImage = selectBitmap("CARD");
        testActiveCard = new UnimonCard((mScreenViewport.width / 2), (mScreenViewport.height / 2), (mScreenViewport.width / 2.3f), (int) (mScreenViewport.height), cardImage, mGameScreen,
                "0", null, null, null, "Earth Dragon",
                UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                20, 30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);
    }*/

    public void loadActiveCard() {
        activeCard.setmGameScreen(mGameScreen);

        BoundingBox mBound = new BoundingBox();
        mBound.x = mScreenViewport.width/2;
        mBound.y = mScreenViewport.height/2;
        mBound.halfWidth = (mScreenViewport.width / 2.3f)/2;
        mBound.halfHeight = mScreenViewport.height/2;

        activeCard.setPosition(mScreenViewport.width/2, mScreenViewport.height/2);

        activeCard.setmBound(mBound);
    }
}
