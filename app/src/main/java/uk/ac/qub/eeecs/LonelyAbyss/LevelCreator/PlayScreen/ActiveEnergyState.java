package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by JP on 11/03/2017.
 */

public class ActiveEnergyState extends State {

    //The test energy card
    EnergyCard testEnergyCard;

    public ActiveEnergyState(ScreenViewport mScreenViewport, LayerViewport mLayerViewport, Game mGame, GameScreen mGameScreen, Boolean active){
        super(mScreenViewport, mLayerViewport, mGame, mGameScreen, active);
        loadEnergyCard();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if(active){
            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            touchButton(touchEvents);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if(active) {
            drawEnergyCard(elapsedTime, graphics2D);
        }
    }

    public void loadEnergyCard(){
        mGame.getAssetManager().loadAndAddBitmap("HEALTHCARD", "img/Cards/Health Potion.png");
        Bitmap energyCardImage = selectBitmap("HEALTHCARD");
        Map<UnimonEvolveType, Map<EnergyType, Integer>> energy = new HashMap<>();
        testEnergyCard = new EnergyCard((0), (0), (mScreenViewport.width/4.6f), mScreenViewport.height/2, energyCardImage, mGameScreen, null, null, null, EnergyType.HEALTH, energy, "null", true, Container.ACTIVE);
        testEnergyCard.moveCard(mScreenViewport.width/2, mScreenViewport.height/2);
    }

    public void drawEnergyCard(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        testEnergyCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    public void touchButton(List<TouchEvent> touchEvents){
        for (TouchEvent t : touchEvents){
            if(t.type == TouchEvent.TOUCH_UP){
                touchActiveEnergy(t);
            }
        }
    }

    public void touchActiveEnergy(TouchEvent t){
        //hide the active energy card if the card itself has been pressed
        if(!(testEnergyCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))){
            active = false;
        }
    }


}
