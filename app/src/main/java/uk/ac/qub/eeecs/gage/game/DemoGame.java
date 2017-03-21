package uk.ac.qub.eeecs.gage.game;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.DeckManagement;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.MenuScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.io.JSONReader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Sample demo game that is create within the MainActivity class
 *
 * @version 1.0
 */
public class DemoGame extends Game {

    /**
     * Create a new demo game
     */
    public DemoGame() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.ac.qub.eeecs.gage.Game#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go with a default 20 UPS/FPS
        setTargetFramesPerSecond(20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call the Game's onCreateView to get the view to be returned.
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create and add a stub game screen to the screen manager. We don't
        // want to do this within the onCreate method as the menu screen
        // will layout the buttons based on the size of the view.
        ArrayList<UnimonCard> UnimonCards = null;
        ArrayList<EnergyCard> energyCards = null;

        try {
            UnimonCards = JSONReader.loadJSONUnimon(getActivity());
            energyCards = JSONReader.loadJSONEnergy(getActivity());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MenuScreen MS = new MenuScreen(this);
        getScreenManager().addScreen(MS);



        return view;
    }
    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if(mScreenManager.getCurrentScreen().getName().equals("MenuScreen"))
            return false;

        // Go back to the menu screen
        getScreenManager().removeScreen(mScreenManager.getCurrentScreen().getName());

        DeckManagement level = new DeckManagement(this);
        getScreenManager().addScreen(level);

       // MenuScreen level = new MenuScreen(this);
        // getScreenManager().addScreen(level);
        return true;
    }
}