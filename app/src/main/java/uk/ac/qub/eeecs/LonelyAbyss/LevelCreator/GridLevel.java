package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grid;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by James on 19/11/2016.
 */

public class GridLevel extends GameScreen {
    //basic set up
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;


    //types of grid squares
    public enum GridType {
        START, END, EMPTY, UNIMON, BATTLE, LOCATION, MOVEMENT, TRADE, HIDDEN
    }

    //number of grid squares to be generated
    private final int numBattleSquare = 2;
    private final int numMoveSquare = 2;
    private final int numTradeSquare = 1;
    private final int maxRanCardSquare = 3;

    //define the number of grids
    private final int gridColumns = 4;
    private final int gridRows = 4;

    //gridArray to hold all the grid squares
    private Grid[][] gridArray;

    public GridLevel(Game game) {
        super("GridLevel", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth()/2, game.getScreenHeight()/2, game.getScreenWidth()/2, game.getScreenHeight()/2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        gridArray = new Grid[gridRows][gridColumns];
        loadGridBitmaps(); //load grid tile bitmaps into asset manager
        generateGrids(); //generate the grid tiles in preparation of being displayed
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        touchGrid(touchEvents); //checks if the user has touched a grid.
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        //draw grid bitmap tiles
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridColumns; j++) {
                gridArray[i][j].draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }
    }

    //generate a number of grids
    public void generateGrids() {
        float gridWidth = mLayerViewport.getWidth()/gridColumns;
        float gridHeight = mLayerViewport.getHeight()/gridRows;

        //float squareRectRegionWidth = gridWidth*0.95f;
        float squareRectRegionHeight = gridHeight*0.95f;

        //the bitmap grid image that is revealed when the user touches a grid
        Bitmap hiddenBitmap = getGame().getAssetManager().getBitmap("HIDDEN");

        //initial position of the first grid tile
        float x = mLayerViewport.getLeft() + gridWidth/2;
        float y = mLayerViewport.getTop() - gridHeight/2;

        //fill the 2d array grid objects
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridColumns; j++) {
                gridArray[i][j]  = new Grid(x, y, squareRectRegionHeight, squareRectRegionHeight, hiddenBitmap, this, true, selectBitmap(GridType.START)); //generate a new grid object
                x = x + gridWidth;
            }
            x = mLayerViewport.getLeft() + gridWidth/2;
            y = y - gridHeight;
        }
    }

    // Select the bitmap for the grid
    private Bitmap selectBitmap(GridType g) {
        String bitmap = "EMPTY";
        switch(g) {
            case START: {
                bitmap = "START";
                break;
            }
            case END: {
                bitmap = "END";
                break;
            }
            case UNIMON: {
                bitmap = "UNIMON";
                break;
            }
            case BATTLE: {
                bitmap = "BATTLE";
                break;
            }
            case LOCATION: {
                bitmap = "LOCATION";
                break;
            }
            case MOVEMENT: {
                bitmap = "MOVEMENT";
                break;
            }
            case TRADE: {
                bitmap = "TRADE";
                break;
            }
            case HIDDEN: {
                bitmap = "HIDDEN";
                break;
            }
        }
        return getGame().getAssetManager().getBitmap(bitmap);
    }

    //load the grid square bitmaps into the asset manager to use to create the grid squares.
    public void loadGridBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("START", "img/GridSquares/Start.png");
        getGame().getAssetManager().loadAndAddBitmap("END", "img/GridSquares/End.png");
        getGame().getAssetManager().loadAndAddBitmap("BATTLE", "img/GridSquares/Battle.png");
        getGame().getAssetManager().loadAndAddBitmap("EMPTY", "img/GridSquares/Empty.png");
        getGame().getAssetManager().loadAndAddBitmap("LOCATION", "img/GridSquares/LOCATION.png");
        getGame().getAssetManager().loadAndAddBitmap("MOVEMENT", "img/GridSquares/Movement.png");
        getGame().getAssetManager().loadAndAddBitmap("TRADE", "img/GridSquares/Trade.png");
        getGame().getAssetManager().loadAndAddBitmap("UNIMON", "img/GridSquares/Unimon.png");
        getGame().getAssetManager().loadAndAddBitmap("HIDDEN", "img/GridSquares/Hidden.png");
    }

    //handling a grid being touched
    public void touchGrid(List<TouchEvent> touchEvents) {
        int numTouchEvent = touchEvents.size();

        for  (TouchEvent t: touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) {
                for (int i = 0; i < gridRows; i++) {
                   for (int j = 0; j < gridColumns; j++) {
                       if ((gridArray[i][j].getHidden()) && (gridArray[i][j].getBound().contains((int) t.x, (int) mLayerViewport.getTop()-t.y))) { //checks whether a grid is hidden and the user is touching a grid
                           gridArray[i][j].reveal();
                       }
                   }
                }
            }
        }

    }

}
