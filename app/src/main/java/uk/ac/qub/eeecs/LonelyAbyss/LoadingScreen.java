package uk.ac.qub.eeecs.LonelyAbyss;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.GridLevel;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.io.JSONReader;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by appcamp on 16/03/2017.
 */

public class LoadingScreen extends GameScreen {

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected String loadingString = "Loading...";
    protected volatile int cycleDots = 0;
    protected Thread loadingCardThread;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param game
     */
    public LoadingScreen(Game game) {
        super("LoadingScreen", game);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewport = new LayerViewport(mScreenViewport.width/2, mScreenViewport.height/2, mScreenViewport.width/2, mScreenViewport.height/2);

        //the background loading of card objects and their bitmaps.
        loadingCardThread = new Thread(new LoadCardsRunnable());
        loadingCardThread.start();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (loadingCardThread.isAlive()) {
            generateLoadingString();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            transitionToNextScreen();
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        Paint textFormat = formatText();
        drawLoadingText(graphics2D, textFormat);
    }

    //generates the string that is displayed to the user as the cards are loading
    public void generateLoadingString() {
        if (cycleDots == 0) {
            loadingString = "Loading";
            cycleDots++;
        } else if (cycleDots == 1) {
            loadingString = "Loading.";
            cycleDots++;
        } else if (cycleDots == 2) {
            loadingString = "Loading..";
            cycleDots++;
        } else {
            loadingString = "Loading...";
            cycleDots = 0;
        }
    }

    //the formatting for the 'loading' text
    public Paint formatText() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        return paint;
    }

    //drawing the 'loading' text
    public void drawLoadingText(IGraphics2D graphics2D, Paint textFormat) {
        Rect loadingTextRect = new Rect(mScreenViewport.left, mScreenViewport.top, mScreenViewport.right, mScreenViewport.bottom);
        graphics2D.drawText(loadingString , loadingTextRect.centerX(), loadingTextRect.centerY(), textFormat);
    }

    //
    public void transitionToNextScreen() {
        mGame.getScreenManager().removeScreen(this.getName());
        GridLevel gLevel = new GridLevel(mGame);
        mGame.getScreenManager().addScreen(gLevel);
    }


    class LoadCardsRunnable implements Runnable {
        @Override
        public void run() {
            loadCards();
        }

        public void loadCards() {
            AssetStore assets = getGame().getAssetManager();
            try {
                ArrayList<UnimonCard> unimonCards = JSONReader.loadJSONUnimon(getGame().getActivity());
                ArrayList<EnergyCard> energyCards = JSONReader.loadJSONEnergy(getGame().getActivity());

                unimonCards = loadUniCardBitmaps(assets, unimonCards);
                energyCards = loadEnergyCardBitmaps(assets, energyCards);

                assets.setUnimonCards(unimonCards);
                assets.setEnergyCards(energyCards);
            } catch (JSONException e) {
                loadingString = "Corrupt JSON File";
            } catch (IOException e) {
                loadingString = "File access failure.";
            }
        }

        public ArrayList<UnimonCard> loadUniCardBitmaps(AssetStore assets, ArrayList<UnimonCard> unimonCards) {
            for (UnimonCard uCard: unimonCards) {
                String cardID = uCard.getID();
                assets.loadAndAddBitmap(cardID, "img/Cards/Unimon/" + cardID + ".png");
                uCard.setmBitmap(assets.getBitmap(cardID));
            }
            return unimonCards;
        }

        public ArrayList<EnergyCard> loadEnergyCardBitmaps(AssetStore assets, ArrayList<EnergyCard> energyCards) {
            for (EnergyCard eCard: energyCards) {
                String cardID = eCard.getID();
                assets.loadAndAddBitmap(cardID, "img/Cards/Energy/" + cardID + ".png");
                eCard.setmBitmap(assets.getBitmap(cardID));
            }
            return energyCards;
        }
    }
}
