package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids.Grid;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids.GridType;
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
    protected List<TouchEvent> touchEvents;

    public static Random rand = new Random();

    //number of grid squares to be generated
    protected int numBattleSquare;
    protected int numMoveSquare;
    protected int numTradeSquare;
    protected int maxRanCardSquare;
    protected int numEmptySquare;

    //define the number of grids
    protected int gridSize;

    //number of grids filled
    protected int gridsNFilled;

    //gridArray to hold all the grid squares
    protected Grid[][] gridArray;

    public GridLevel(Game game) {
        super("GridLevel", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());

        //4x4, 5x5, 6x6 - randomly sets the size of the grid
        gridSize = rand.nextInt(2)+4;
        gridsNFilled = (gridSize * gridSize) - 2; //number of grids filled to be filled minus the start grid square and end grid square

        //the number of grid squares generated
        numBattleSquare = (int) Math.ceil(gridSize * 0.5);
        numMoveSquare = (int) Math.ceil(gridSize * 0.25);
        numTradeSquare = (int) Math.ceil(gridSize * 0.25);
        maxRanCardSquare = (int) Math.ceil(gridSize * 0.5);
        numEmptySquare = (gridSize * gridSize) - (numBattleSquare + numMoveSquare + numTradeSquare + maxRanCardSquare + 2); //number of empty grid squares

        gridArray = new Grid[gridSize][gridSize]; //2-dimensional array to hold the grid squares in rows and columns
        loadGridBitmaps(); //load grid tile bitmaps into asset manager
        generateGrids(); //generate the grid tiles in preparation of being displayed
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        mInput = mGame.getInput(); //get the users multiple inputs
        touchEvents = mInput.getTouchEvents(); //get the touch events from the user's input
        touchGrid(touchEvents); //checks if the user has touched a grid.
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //sets the basic view of the screenviewport
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        //draw grid bitmap tiles
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridArray[i][j].draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }
    }

    //generate a number of grids
    public void generateGrids() {
        //the dimensions of the grid rectangles defined by the the size of the layer view port.
        float gridWidth = mLayerViewport.getWidth() / gridSize;
        float gridHeight = mLayerViewport.getHeight() / gridSize;

        //getting the dimensions of the square by the height of the card rectangle, and also make them smaller to have spacing
        float squareDimen = gridHeight * 0.95f;

        //the bitmap grid image that is revealed when the user touches a grid
        Bitmap hiddenBitmap = getGame().getAssetManager().getBitmap("HIDDEN");

        //initial position of the first grid tile
        float x = mLayerViewport.getLeft() + gridWidth / 2;
        float y = mLayerViewport.getTop() - gridHeight / 2;

        //hold the default values to be changed during the for loop operation
        GridType gType;
        boolean hideGrid;
        boolean terminus;

        //fill the 2d array grid objects
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                hideGrid = true;
                terminus = false;
                if (i == 0 && j == 0) { //assigns top left grid to start
                    gType = GridType.START;
                    hideGrid = false;
                } else if (i == gridSize - 1 && j == gridSize - 1) { //assigns bottom right grid to end
                    gType = GridType.END;
                    hideGrid = false;
                    terminus = true;
                } else { //if the grid square needs to be filled randomly
                    gType = typeGrid();
                }

                gridArray[i][j] = new Grid(x, y, squareDimen, squareDimen, hiddenBitmap, this, hideGrid, selectBitmap(gType), terminus, gType); //generate a new grid object
                x = x + gridWidth; //move the x co-ordinate by the width of the grid Rectangle
            }
            x = mLayerViewport.getLeft() + gridWidth / 2; //reset the x co-ordinate back to the initial position, drawing left from right
            y = y - gridHeight; //move the y co-ordinate down by the height of the rectangle
        }
    }

    // Select the bitmap for the grid
    private Bitmap selectBitmap(GridType g) {
        String bitmap = "EMPTY";
        switch (g) {
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
        return getGame().getAssetManager().getBitmap(bitmap); //return the grid square bitmap from assetmanager
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
        getGame().getAssetManager().loadAndAddBitmap("HIDDEN", "img/GridSquares/Hidden.jpg");
    }

    //handling a grid being touched
    public void touchGrid(List<TouchEvent> touchEvents) {

        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) {
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        if ((gridArray[i][j].getHidden()) && (gridArray[i][j].getBound().contains((int) t.x, (int) mLayerViewport.getTop() - t.y))) { //checks whether a grid is hidden and the user is touching a grid
                            if (validGridMove(gridArray[i][j], i, j)) {
                                gridArray[i][j].reveal(); //reveal the grid square
                                gridAction(gridArray[i][j]); //called to do action needs to happen when this grid is revealed.
                            }
                        }
                    }
                }
            }
        }

    }

    //method to validate that the grids to the top,bottom,right o§r left are not hidden and not the end grid (terminus)
    public boolean validGridMove(Grid g, int i, int j) {
        if ((i < gridSize - 1) && (!gridArray[i + 1][j].getHidden()) && (!gridArray[i + 1][j].getTerminus())) {
            return true;
        } else if ((j < gridSize - 1) && (!gridArray[i][j + 1].getHidden()) && (!gridArray[i][j + 1].getTerminus())) {
            return true;
        } else if ((j > 0) && (!gridArray[i][j - 1].getHidden()) && (!gridArray[i][j - 1].getTerminus())) {
            return true;
        } else if ((i > 0) && (!gridArray[i - 1][j].getHidden()) && (!gridArray[i - 1][j].getTerminus())) {
            return true;
        }

        return false;
    }


    //selects grid square randomly
    public GridType typeGrid() {
        double randNum = rand.nextDouble();

        //set the probability of selecting each
        double probBattle = 0;
        double probMove = 0;
        double probTrade = 0;
        double probRandCard = 0;
        double probEmpty = 0;

        //calculate the probability of selecting each grid square from the remaining grid squares
        probBattle = (double) numBattleSquare / gridsNFilled;
        probMove = (double) numMoveSquare / gridsNFilled;
        probTrade = (double) numTradeSquare / gridsNFilled;
        probRandCard = (double) maxRanCardSquare / gridsNFilled;
        probEmpty = (double) numEmptySquare / gridsNFilled;

        gridsNFilled--; //decrease the number of grids not filled in preparation before filling a grid square

        //randomly choose a grid square by the randomly generated number
        if (probBattle >= randNum) {
            numBattleSquare--; //decrement the randomly selected square
            return GridType.BATTLE; //return the square
        } else if (probBattle + probMove >= randNum) {
            numMoveSquare--;
            return GridType.MOVEMENT;
        } else if (probBattle + probMove + probTrade >= randNum) {
            numTradeSquare--;
            return GridType.TRADE;
        } else if (probBattle + probMove + probTrade + probRandCard >= randNum) {
            maxRanCardSquare--;
            return GridType.UNIMON;
        } else if (probBattle + probMove + probTrade + probRandCard + probEmpty >= randNum) {
            numEmptySquare--;
            return GridType.EMPTY;
        }
        return GridType.EMPTY;
    }


    //performs the action the revealed grid should perform
    public void gridAction(Grid selectGrid) {
        GridType gridT = selectGrid.getType(); //gets the type of grid revealed

        //selection of the grid type to perform the action.
        if (gridT == GridType.BATTLE) {
            //load the play area to begin battle.
            mGame.getScreenManager().removeScreen(this.getName());
            PlayScreen playS = new PlayScreen(mGame);
            mGame.getScreenManager().addScreen(playS);
        }
    }
}

