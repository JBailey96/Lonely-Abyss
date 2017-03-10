package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.text.UnicodeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
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

/**
 * Created by appcamp on 08/03/2017.
 */

public class ActiveUnimonState extends State {

    boolean energy = false;
    UnimonCard testActiveCard; //testCard used to test the active unimon view
    EnergyCard testEnergyCard;
    protected final int numMoveButtons = 3; //the number of move buttons

    //Battle Option Buttons
    ReleaseButton applyEnergyButton;
    ReleaseButton evolveButton;
    ReleaseButton retreatButton;
    //Move battle options
    List<ReleaseButton> moveButtons = new ArrayList<>();

    public ActiveUnimonState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadButtonOptionBitmaps();
        generateBattleOptions();
        loadTestCard();
        loadEnergyCard();

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

                touchActiveUnimon(t);
                //touchEnergyButton(t);
            }
        }
    }

    //checks whether the active unimon has been touched
    public void touchActiveUnimon(TouchEvent t) {
        if (!(testActiveCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
           // active = false;
        }

        if (!(applyEnergyButton.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            energy=true;
        }
    }




    @Override
    public void draw (ElapsedTime elapsedTime, IGraphics2D graphics2D){
        if (active) {
            drawActive(elapsedTime, graphics2D);
            //drawEnergyCard(elapsedTime, graphics2D);

        }
        if (energy) {
            drawEnergyCard(elapsedTime, graphics2D);
        }
    }


    //draw active unimon card and the battle options
    public void drawActive(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        drawActiveUnimon(elapsedTime, graphics2D);
        drawBattleOptionButtons(elapsedTime, graphics2D);
    }

    //draw active unimon card
    public void drawActiveUnimon(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        Paint myPaint = new Paint();
        Rect paintRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height);
        myPaint.setAlpha(200); //make the background behind the unimon card transparent


        graphics2D.drawBitmap(selectBitmap("BLACK"), null, paintRect, myPaint);
        testActiveCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    public void drawEnergyCard(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        testEnergyCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);


    }


    //method for drawing the battle option buttons
    public void drawBattleOptionButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (ReleaseButton moveButton: moveButtons) { //drawing the move buttons
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
        float x = mScreenViewport.width*0.15f;
        float y = mScreenViewport.height/2 - mScreenViewport.height/6;
        float width =  mScreenViewport.width/5;
        float height = mScreenViewport.height/8;

        applyEnergyButton = new ReleaseButton(x, y, width, height, "ENERGYBUTTON", "ENERGYBUTTON", "", mGameScreen);
    }

    //generate the button to evolve the current active unimon
    public void generateEvolveButton() {
        float x = mScreenViewport.width*0.15f;
        float y = mScreenViewport.height/2;
        float width =  mScreenViewport.width/5;
        float height = mScreenViewport.height/8;

        evolveButton = new ReleaseButton(x, y, width, height, "EVOLVEBUTTON", "EVOLVEBUTTON", "", mGameScreen);
    }

    //generate the button to retreat the current active unimon
    public void generateRetreatButton() {
        float x = mScreenViewport.width*0.15f;
        float y = mScreenViewport.height/2 + mScreenViewport.height/6;
        float width =  mScreenViewport.width/5;
        float height = mScreenViewport.height/8;

        retreatButton = new ReleaseButton(x, y, width, height, "RETREATBUTTON", "RETREATBUTTON", "", mGameScreen);
    }

    //generate the buttons to use the card's moves
    public void generateMoveButtons() {
        float x = mScreenViewport.width*0.85f;
        float y;
        float width = mScreenViewport.width/5;
        float height = mScreenViewport.height/8;

        for (int i = 0; i < numMoveButtons; i++) {
            y =  mScreenViewport.height/2 + ((i-1) * mScreenViewport.height/6);
            ReleaseButton unimonMoveButton = new ReleaseButton(x, y, width, height, "MOVE" + (i+1) + "BUTTON", "MOVE" + (i+1) + "BUTTON", "", mGameScreen);
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

    //loads test card
    public void loadTestCard() {
        mGame.getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Demon Slayer.png");
        mGame.getAssetManager().loadAndAddBitmap("BLACK", "img/Particles/black.png");
        Bitmap cardImage = selectBitmap("CARD");
        testActiveCard = new UnimonCard((mScreenViewport.width/2), (mScreenViewport.height/2), (mScreenViewport.width/2.3f), (int)(mScreenViewport.height), cardImage, mGameScreen,
                "0", null, null, null, "Earth Dragon",
                UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                20 ,30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);

    }

    //load energy card
    public void loadEnergyCard(){
        mGame.getAssetManager().loadAndAddBitmap("HEALTHCARD", "img/Cards/Health Potion.png");
        Bitmap energyCardImage = selectBitmap("HEALTHCARD");
        testEnergyCard = new EnergyCard((0), (0), (mScreenViewport.width/4.6f), mScreenViewport.height/2, energyCardImage, mGameScreen, null, null, null, EnergyType.HEALTH, null, true, Container.ACTIVE);
        //testEnergyCard.moveCard(mGame.getScreenWidth(), mGame.getScreenHeight());
        testEnergyCard.moveCard(mScreenViewport.width/2, mScreenViewport.height/2);

    }





}
