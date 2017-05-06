package uk.ac.qub.eeecs.LonelyAbyss;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.GridLevel;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.MenuScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.CustomGage.JSONReader;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by James on 16/03/2017.
 */

public class LoadingScreen extends GameScreen {

    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected String loadingString = "Loading...";
    protected volatile int cycleDots = 0;
    protected Thread loadingCardThread;
    protected boolean showLoading = true;

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
            transitionToGridScreen();
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        Paint textFormat = formatText();
        drawLoadingText(graphics2D, textFormat);
    }

    //James Bailey 40156063
    //generates the string that is displayed to the user as the cards are loading
    public void generateLoadingString() {
        if (showLoading) {
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
    }

    //james Bailey 40156063
    //the formatting for the 'loading' text
    public Paint formatText() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    //James Bailey 40156063
    //drawing the 'loading' text
    public void drawLoadingText(IGraphics2D graphics2D, Paint textFormat) {
       /// Rect loadingTextRect = new Rect(mScreenViewport.left+mScreenViewport, mScreenViewport.top, mScreenViewport.right/2, mScreenViewport.bottom);
        graphics2D.drawText(loadingString , mScreenViewport.centerX(), mScreenViewport.centerY(), textFormat);
    }

    //James Bailey 40156063
    public void transitionToGridScreen() {
        mGame.getScreenManager().removeScreen(this.getName());
        GridLevel gLevel = new GridLevel(mGame);
        mGame.getScreenManager().addScreen(gLevel);
    }

    //James Bailey 40156063
    public void transitionToMenuScreen() {
        mGame.getScreenManager().removeScreen(this.getName());
        MenuScreen menuScreen = new MenuScreen(mGame);
        mGame.getScreenManager().addScreen(menuScreen);
    }


    //James Bailey 40156063
    //Thread that loads all the cards the game has and the cards the player owns into game
    // - from a JSON asset file
    class LoadCardsRunnable implements Runnable {
        @Override
        public void run() {
            loadCards();
        }

        public void loadCards() {
            AssetStore assets = getGame().getAssetManager(); //asset manager of the game
            try {
                //lists of the game's cards
                ArrayList<UnimonCard> unimonCards = JSONReader.loadJSONUnimon(getGame());
                ArrayList<EnergyCard> energyCards = JSONReader.loadJSONEnergy(getGame());

                //load the bitmaps of the game's cards
                unimonCards = loadUniCardBitmaps(assets, unimonCards);
                energyCards = loadEnergyCardBitmaps(assets, energyCards);

                //add the list of cards to the assetmanager
                assets.setUnimonCards(unimonCards);
                assets.setEnergyCards(energyCards);

                //lists of the cards the player owns
                ArrayList<UnimonCard> playerUnimonCards = JSONReader.loadPlayerUnimonCards(getGame());
                ArrayList<EnergyCard> playerEnergyCards = JSONReader.loadPlayerEnergyCards(getGame());

                //set the player's cards
                mGame.getPlayer().setUnimonCards(playerUnimonCards);
                mGame.getPlayer().setEnergyCards(playerEnergyCards);
            } catch (JSONException | IOException e) { //error handling - issues with file IO with the JSON or a corrupt JSON format
                showLoading = false;
                loadingString = ("ERROR LOADING JSON");
                try {
                    Thread.sleep(4000); //allow the user to see the error message
                    transitionToMenuScreen(); //transition back to the previous screen
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        //James Bailey 40156063
        //Add all the corresponding template bitmaps to the game's unimon cards
        public ArrayList<UnimonCard> loadUniCardBitmaps(AssetStore assets, ArrayList<UnimonCard> unimonCards) {
            for (UnimonCard uCard: unimonCards) {
                String cardID = uCard.getID();
                assets.loadAndAddBitmap("u"+cardID, "img/Cards/Unimon/u" + cardID + ".png");
                uCard.setmBitmap(assets.getBitmap("u"+cardID));
            }
            return unimonCards;
        }

        public ArrayList<EnergyCard> loadEnergyCardBitmaps(AssetStore assets, ArrayList<EnergyCard> energyCards) {
            for (EnergyCard eCard: energyCards) {
                String cardID = eCard.getID();
                assets.loadAndAddBitmap("e"+cardID, "img/Cards/Energy/e" + cardID + ".png");
                eCard.setmBitmap(assets.getBitmap("e"+cardID));
            }
            return energyCards;
        }
    }
}
