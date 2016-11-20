package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;

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
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;


    //types of grid squares
    public enum GridType {
        START, END, EMPTY, UNIMON, BATTLE, LOCATION, MOVEMENT, TRADE;
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
    private Grid[][] gridArray = new Grid[4][4];

    public GridLevel(Game game) {
        super("GridLevel", game);
        mLayerViewport = new LayerViewport(500, 500, game.getScreenWidth()/2, game.getScreenHeight()/2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        loadGridBitmaps();
        generateGrids();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mScreenViewport.toRect());

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                gridArray[i][j].draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }
        //Grid end = new Grid(50, 50, 100, 100, selectBitmap(GridType.END), this, true);


        //drawing background?
       // graphics2d.drawBitmap(background, null,
         //       DwarfHomelandImg, null);



    }

    public void generateGrids() {
        float gridWidth = mLayerViewport.getWidth()/gridColumns;
        float gridHeight = mLayerViewport.getHeight()/gridRows;

        float b = commonDivisor(mLayerViewport.getWidth(), mLayerViewport.getHeight());
        float x = mLayerViewport.getLeft();
        float y = mLayerViewport.getTop();

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                gridArray[i][j]  = new Grid(x, y, gridWidth, gridHeight, selectBitmap(GridType.START), this, true);
                x = x + gridWidth;
            }
            x = mLayerViewport.getLeft();
            y = y-gridHeight;
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
        }
        return getGame().getAssetManager().getBitmap(bitmap);
    }

    public void loadGridBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("START", "img/GridSquares/Start.png");
        getGame().getAssetManager().loadAndAddBitmap("END", "img/GridSquares/End.png");
        getGame().getAssetManager().loadAndAddBitmap("BATTLE", "img/GridSquares/Battle.png");
        getGame().getAssetManager().loadAndAddBitmap("EMPTY", "img/GridSquares/Empty.png");
        getGame().getAssetManager().loadAndAddBitmap("LOCATION", "img/GridSquares/LOCATION.png");
        getGame().getAssetManager().loadAndAddBitmap("MOVEMENT", "img/GridSquares/Movement.png");
        getGame().getAssetManager().loadAndAddBitmap("TRADE", "img/GridSquares/Trade.png");
        getGame().getAssetManager().loadAndAddBitmap("UNIMON", "img/GridSquares/Unimon.png");
    }

    public float commonDivisor(float x, float y) {
        if (y==0) return x;
        return commonDivisor(y,x%y);
    }
}
