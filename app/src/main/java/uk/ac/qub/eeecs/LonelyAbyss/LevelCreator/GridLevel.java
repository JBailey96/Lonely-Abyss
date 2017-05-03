package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids.Grid;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids.GridType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.PlayerSprite;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen.PlayScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.PlayerSprite.MovementDirection;


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

    final int minimumTiles = 4; //the minimum number of tiles
    final int numStartEndTiles = 2;

    //the dimensions of a single grid
    protected float gridWidth;
    protected float gridHeight;

    //the x, y positions of a single grid.
    protected float x;
    protected float y;

    //the grid's width and height, a minimum square from the grid's rectangle.
    protected float squareDimen;

    //define the number of grids
    protected int gridSize;

    //number of grids filled
    protected int gridsNFilled;

    //gridArray to hold all the grid squares
    protected Grid[][] gridArray;

    protected Bitmap hiddenBitmap; //the bitmap grid image that is revealed when the user touches a grid

    //sounds for the grid level
    protected Sound cardFlip;
    protected Sound battlecardMusic;
    protected Music gridMusic;

    protected Thread transitionBattle; //the thread that starts when the user encounters a battle tile
    protected Thread movePlayerAction;

    protected PlayerSprite playerSprite; //the sprite used to represent the player - moves around the grid squares.

    //the direction of movement of the player sprite traverses the grid square in a movement
    protected MovementDirection playerMoveDirection;

    //the position of the player sprite in the grid array
    protected int playerGridPosI = 0;
    protected int playerGridPosJ = 0;

    protected Grid selectGrid;

    public GridLevel(Game game) {
        super("GridLevel", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());createGrid(); //calculate the size of grid and create the grid

        loadGrid();
        loadPlayer();

        loadSounds(); //load sounds used in the screen
        playSounds(); //play the sounds

        transitionBattle = new Thread(new TransitionBattle()); //initalise the thread that handles the transition to the battle scree
        movePlayerAction = new Thread(new MovePlayerSprite());
    }

    //James Bailey 40156063
    //Generate a new grid for the player to navigate - called when the player enters the terminus grid square.
    public void generateNewGrid() {
        loadGrid();
        this.playerGridPosI = 0;
        this.playerGridPosJ = 0;
        loadPlayer();
    }

    //James Bailey 40156063
    //load the required assets and the variables to present the grid.
    private void loadGrid() {
        createGrid(); //calculate the size of grid and create the grid
        calculateNumGridsGenerate(); //calculate each type of grid tile to create
        loadGridBitmaps(); //load grid tile bitmaps into asset manager
        this.hiddenBitmap = getGame().getAssetManager().getBitmap("HIDDEN");
        generateGridDimension(); //generate the grid tiles in preparation of being displayed
        fillGridArray(); //fill the grid array with grid objects.
    }

    //James Bailey 40156063
    //load the required assets and variables to present the player.
    private void loadPlayer() {
        loadPlayerBitmaps();
        generatePlayerSprite();
    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        if (!isTransitionThreadAlive()) {
            mInput = mGame.getInput(); //get the users multiple inputs
            touchEvents = mInput.getTouchEvents(); //get the touch events from the user's input
            touchGrid(touchEvents); //checks if the user has touched a grid.
        }
        playerSprite.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //sets the basic view of the screenviewport
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());
        drawGridTiles(elapsedTime, graphics2D);
        playerSprite.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }

    //James Bailey 40156063
    //Draws the grid tiles onto the canvas
    private void drawGridTiles(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridArray[i][j].draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }
    }

    //James Bailey 40156063
    //generate the grid tile dimensions and fill the grid array.
    public void generateGridDimension() {
        //the dimensions of the grid rectangles defined by the the size of the layer view port.
        this.gridWidth = mLayerViewport.getWidth() / gridSize;
        this.gridHeight = mLayerViewport.getHeight() / gridSize;

        //getting the dimensions of the square by the height of the card rectangle, and also make them smaller to have spacing
        this.squareDimen = gridHeight * 0.95f;

        //initial position of the first grid tile
        this.x = mLayerViewport.getLeft() + gridWidth / 2;
        this.y = mLayerViewport.getTop() - gridHeight / 2;
    }

    //James Bailey 40156063
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
        return super.selectBitmap(bitmap);
    }

    //James Bailey 40156063
    //fill the 2d array with the grid objects
    private void fillGridArray() {
        GridType gType;
        boolean showGrid = true;
        boolean terminus = false;

        float x = this.x;
        float y = this.y;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (i == 0 && j == 0) { //assigns top left grid to start
                    gType = GridType.START;
                    showGrid = true;
                } else if (i == gridSize - 1 && j == gridSize - 1) { //assigns bottom right grid to end
                    gType = GridType.END;
                    showGrid = true;
                    terminus = true;
                } else {
                    //fill the grid array with a random type of grid object.
                    gType = typeGrid();
                }

                this.gridArray[i][j] = new Grid(x, y, squareDimen, squareDimen, hiddenBitmap, this, showGrid, selectBitmap(gType), terminus, gType); //generate a new grid object
                x = x + gridWidth; //move the x co-ordinate by the width of the grid Rectangle

                //reset the variables for the next iteration.
                showGrid = false;
                terminus = false;
            }
            x = mLayerViewport.getLeft() + gridWidth / 2; //reset the x co-ordinate back to the initial position, drawing left from right
            y = y - gridHeight; //move the y co-ordinate down by the height of the rectangle
        }
    }

    //James Bailey 40156063
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

    public void loadPlayerBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("PLAYERREST", "img/PlayerSprites/playerRest.png");

        getGame().getAssetManager().loadAndAddBitmap("PLAYERMOVEY1", "img/PlayerSprites/playermoveY1.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAYERMOVEY2", "img/PlayerSprites/playermoveY2.png");

        getGame().getAssetManager().loadAndAddBitmap("PLAYERMOVEX1", "img/PlayerSprites/playermoveX1.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAYERMOVEX2", "img/PlayerSprites/playermoveX2.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAYERMOVEX3", "img/PlayerSprites/playermoveX3.png");
        getGame().getAssetManager().loadAndAddBitmap("PLAYERMOVEX4", "img/PlayerSprites/playermoveX4.png");
    }

    //James Bailey 40156063
    //handles the touch events for this gamescreen
    public void touchGrid(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) {
                if ((handleGridTouch(t))) {
                    mInput.resetAccumulators();
                    break;
                }
            }
        }
    }

    //James Bailey 40156063
    //handles a touch event on the grid squares.
    private boolean handleGridTouch(TouchEvent t) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if ((gridArray[i][j].getBound().contains((int) t.x, (int) mLayerViewport.getTop() - t.y))) { //checks whether a grid is hidden and the user is touching a grid
                    touchSingleGridSquare(i, j); //a grid square has been touched - handle the event
                    return true;
                }
            }
        }
        return false;
    }

    //James Bailey 40156063
    //the action to be taken if a grid square is touched.
    private void touchSingleGridSquare(int i , int j) {
        if (validGridMove(i, j)) { //validates whether it is a valid grid move.
            calculateDirectionOfMove(i, j);

            this.playerGridPosI = i;
            this.playerGridPosJ = j;

            cardFlip.play(); //play the card flip sound

            this.selectGrid = gridArray[i][j]; //the grid the player has touched

            movePlayerAction = new Thread(new MovePlayerSprite());

                if (!this.selectGrid.isGridSelected()) {
                    this.selectGrid.reveal(); //reveal the grid square
                    movePlayerAction.start();
                } else {
                    movePlayerAction.start();
                }
        }
    }

    //James Bailey 40156063
    //method to validate that the grids to the top,bottom,right or left are not hidden and not the end grid (terminus)
    public boolean validGridMove(int i, int j) {
        if (!isSelectedGridAdjacent(i, j)) return false;
        return true;
    }

    //James Bailey 40156063
    //Validates whether the grid selected is not adjacent to the current selected grid - diagonal movements not allowed.
    private boolean isSelectedGridAdjacent(int i, int j) {
        if (Math.abs(i-this.playerGridPosI) >= 1 && (Math.abs(j-this.playerGridPosJ) >= 1)) {
            return false;
        } else if (Math.abs(i -this.playerGridPosI) > 1) {
            return false;
        } else if (Math.abs(j-this.playerGridPosJ) > 1) {
            return false;
        }

        return true;
    }

    //James Bailey 40156063
    //Validates whether the selected grid tile is terminus - not valid
    private boolean isSelectedGridTerminus(int i, int j) {
        if ((i < this.gridSize) && (gridArray[i + 1][j].getTerminus())) {
            return true;
        } else if (((j < this.gridSize) && (gridArray[i][j + 1].getTerminus()))) {
            return true;
        } else if ((j > 0) && (gridArray[i][j - 1].getTerminus())) {
            return true;
        } else if ((i > 0) && (gridArray[i - 1][j].getTerminus())) {
            return true;
        }
        return false;
    }

    //James Bailey 40156063
    //Validates whether the grid is selected - not valid.
    private boolean isSelectedGridSelected(int i , int j) {
        if ((i < this.gridSize) && gridArray[i + 1][j].isGridSelected()) {
            return true;
        } else if ((j < this.gridSize) && (gridArray[i][j + 1].isGridSelected())) {
            return true;
        } else if ((j > 0) && (gridArray[i][j - 1].isGridSelected())) {
            return true;
        } else if ((i > 0) && (gridArray[i - 1][j].isGridSelected())) {
            return true;
        }

        return false;
    }



    //James Bailey 40156063
    //selects grid square randomly
    public GridType typeGrid() {
        double randNum = rand.nextDouble();

        //calculate the probability of selecting each grid square from the remaining grid squares
        double probBattle = (double) numBattleSquare / gridsNFilled;
        double probMove = (double) numMoveSquare / gridsNFilled;
        double probTrade = (double) numTradeSquare / gridsNFilled;
        double probRandCard = (double) maxRanCardSquare / gridsNFilled;
        double probEmpty = (double) numEmptySquare / gridsNFilled;

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


    //James Bailey 40156063
    //performs the action the revealed grid should perform
    public void gridAction() {
        GridType selectGridType = this.selectGrid.getType(); //gets the type of grid revealed

        //selection of the grid type to perform the action.
        if (selectGridType == GridType.END) {
            generateNewGrid();
        } else if (selectGridType == GridType.BATTLE) {
            transitionBattle = new Thread(new TransitionBattle()); //initalise the thread that handles the transition to the battle screen
            transitionBattle.start();
        }
    }

    public void loadSounds(){
        getGame().getAssetManager().loadAndAddSound("CARDFLIP", "Sounds/CardFlip.mp3");
        cardFlip = mGame.getAssetManager().getSound("CARDFLIP");

        getGame().getAssetManager().loadAndAddMusic("GRIDSOUND", "Music/GridMusic2.mp3");
        gridMusic = mGame.getAssetManager().getMusic("GRIDSOUND");
    }


    //James Bailey 40156063
    //Generate the player sprite that traverses the grid squares.
    public void generatePlayerSprite() {
        Grid firstGrid = gridArray[playerGridPosI][playerGridPosJ]; //the grid square the player starts in, the top left 'start' grid.
        BoundingBox firstGridBound = firstGrid.getBound(); //the dimension and position of the grid the player starts in

        Bitmap playerRestBitmap =  mGame.getAssetManager().getBitmap("PLAYERREST"); //the rest bitmap of the player sprite.

        //the list of bitmaps for the player's sprite move animation.
        Bitmap[] playerPositionYBitmaps = generatePlayerYBitmaps();
        Bitmap[] playerPositionXBitmaps = generatePlayerXBitmaps();

        this.playerSprite = new PlayerSprite(firstGridBound.x, firstGridBound.y, firstGridBound.getWidth()/1.2f
                , firstGridBound.getHeight(), this, playerRestBitmap, playerPositionXBitmaps, playerPositionYBitmaps);
    }

    //James Bailey 40156063
    //Generates the movement bitmap animations the player sprite has when moving on the y axis
    private Bitmap[] generatePlayerYBitmaps() {
        final int numberOfPlayerMoveYBitmaps = 2;

        Bitmap[] playerPositionYBitmaps = new Bitmap[numberOfPlayerMoveYBitmaps];
        playerPositionYBitmaps[0] = mGame.getAssetManager().getBitmap("PLAYERMOVEY1");
        playerPositionYBitmaps[1] = mGame.getAssetManager().getBitmap("PLAYERMOVEY2");

        return playerPositionYBitmaps;
    }

    //James Bailey 40156063
    //Generates the movement bitmap animations on the x axis
    private Bitmap[] generatePlayerXBitmaps() {
        final int numberOfPlayerMoveXBitmaps = 4;

        Bitmap[] playerPositionXBitmaps = new Bitmap[numberOfPlayerMoveXBitmaps];
        playerPositionXBitmaps[0] = mGame.getAssetManager().getBitmap("PLAYERMOVEX1");
        playerPositionXBitmaps[1] = mGame.getAssetManager().getBitmap("PLAYERMOVEX2");
        playerPositionXBitmaps[2] = mGame.getAssetManager().getBitmap("PLAYERMOVEX3");
        playerPositionXBitmaps[3] = mGame.getAssetManager().getBitmap("PLAYERMOVEX4");

        return playerPositionXBitmaps;
    }

    //James Bailey 40156063
    //transitions to the play area upon the player touching a 'battle' grid tile.
    class TransitionBattle implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000); //pause before the transition takes place - allows the user to see the grid selected before it transitions.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gridMusic.stop(); //stop the grid music once battle commences

            //load the play area
            mGame.getScreenManager().removeScreen("GridLevel");
            PlayScreen playS = new PlayScreen(mGame);
            mGame.getScreenManager().addScreen(playS);
        }
    }

    //James Bailey 40156063
    //Move the player's sprite to the selected grid square.
    class MovePlayerSprite implements Runnable {

        @Override
        public void run() {
            BoundingBox selectGridBound = selectGrid.getBound(); //gets the dimensions and position of the grid selected.

            final long timeToMovePlayer = 1000; //time it takes to animate the player's move.

            //move the sprite to the destination x and y.
            playerSprite.moveSprite(selectGridBound.x, selectGridBound.y, timeToMovePlayer, playerMoveDirection);

            //waits for the the player move animation to finish.
            try {
                Thread.sleep(timeToMovePlayer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gridAction(); //called to do action needs to happen when this grid is revealed.
            }
    }

    //James Bailey 40156063
    //Method that validates whether there are transition threads currently active.
    public boolean isTransitionThreadAlive() {
        if (transitionBattle.isAlive()) {
            return true;
        } else if (movePlayerAction.isAlive()) {
            return true;
        }

        return false;
    }

    public void playSounds() {
        //custom settings for grid music
        gridMusic.setLopping(true);
        gridMusic.setVolume(10);

        //play the grid background music
        gridMusic.play();
    }

    //James Bailey 40156063
    //sets the number of each square to be filled
    public void calculateNumGridsGenerate() {
        //The proportion of each different type of grid square
        float propBattle = 0.5f;
        float propMove = 0.1f;
        float propTrade = 0.25f;
        float propRanCard = 0.5f;

        this.gridsNFilled = (this.gridSize * this.gridSize) - this.numStartEndTiles; //number of grids filled to be filled minus the start grid square and end grid square

        //the number of grid squares to be generated
        this.numBattleSquare = (int) Math.ceil(this.gridSize * propBattle);
        this.numMoveSquare = (int) Math.ceil(this.gridSize * propMove);
        this.numTradeSquare = (int) Math.ceil(this.gridSize * propTrade);
        this.maxRanCardSquare = (int) Math.ceil(this.gridSize * propRanCard);
        this.numEmptySquare = (this.gridSize * this.gridSize) - (this.numBattleSquare + this.numMoveSquare + this.numTradeSquare + this.maxRanCardSquare + 2); //number of empty grid squares
    }

    //James Bailey 40156063
    //Generates the two dimensional grid array
    public void createGrid() {
        //4x4, 5x5, 6x6 - randomly sets the size of the grid
        this.gridSize = rand.nextInt(2)+this.minimumTiles;
        this.gridArray = new Grid[gridSize][gridSize]; //2-dimensional array to hold the grid squares in rows and columns
    }

    //James Bailey 40156063
    //Sets the direction of movement from the current grid to the selected grid.
    public void calculateDirectionOfMove(int nextGridI, int nextGridJ) {
        if (nextGridI > this.playerGridPosI) {
            this.playerMoveDirection = MovementDirection.DOWN;
        } else if (this.playerGridPosI > nextGridI) {
            this.playerMoveDirection = MovementDirection.UP;
        } else if (nextGridJ > this.playerGridPosJ) {
            this.playerMoveDirection = MovementDirection.RIGHT;
        } else {
            this.playerMoveDirection = MovementDirection.LEFT;
        }
    }
}

