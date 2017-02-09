package uk.ac.qub.eeecs.gage.game;

import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.CardTest;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.MenuScreen;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;
import uk.ac.qub.eeecs.gage.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
//        getScreenManager().removeScreen(mScreenManager.getCurrentScreen().getName());
//        GridLevel level = new GridLevel(this);
//        getScreenManager().addScreen(level);

//        DeckManagement DM = new DeckManagement(this);
 //       getScreenManager().addScreen(DM);
 //      GridLevel gl = new GridLevel(this);
 //       getScreenManager().addScreen(gl);

        /*PlayScreen PS = new PlayScreen(this);
        getScreenManager().addScreen(PS);*/

        /*MenuScreen MS = new MenuScreen(this);
        getScreenManager().addScreen(MS);*/

        CardTest CT = new CardTest(this);
        getScreenManager().addScreen(CT);


        return view;
    }

    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if(mScreenManager.getCurrentScreen().getName().equals("MenuScreen"))
            return false;

        // Go back to the menu screen
        getScreenManager().removeScreen(mScreenManager.getCurrentScreen().getName());
//        DeckManagement level = new DeckManagement(this);
        //GridLevel level = new GridLevel(this);
        MenuScreen level = new MenuScreen(this);
        getScreenManager().addScreen(level);
        return true;
    }
}