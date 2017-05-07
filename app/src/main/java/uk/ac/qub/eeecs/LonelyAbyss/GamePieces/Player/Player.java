package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player;

import java.util.ArrayList;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Grids.Grid;

/**
 * Created by James on 05/02/2017.
 */

public class Player {
    private BattleSetup playerBattleSetup; //the player's battle setup they bring into battle
    private ArrayList<EnergyCard> energyCards;
    private ArrayList<UnimonCard> unimonCards; // cards the player possesses

    private Grid[][] gridLevelTiles; //grid level tiles the player is currently on
    //the position of the player on the grid
    private int playerGridPosI = 0;
    private int playerGridPosJ = 0;

    public Player() {

    }


    public BattleSetup getPlayerBattleSetup() {
        return playerBattleSetup;
    }

    public void setPlayerBattleSetup(BattleSetup playerBattleSetup) {
        this.playerBattleSetup = playerBattleSetup;
    }

    public ArrayList<EnergyCard> getEnergyCards() {
        return energyCards;
    }

    public void setEnergyCards(ArrayList<EnergyCard> energyCards) {
        this.energyCards = energyCards;
    }

    public ArrayList<UnimonCard> getUnimonCards() {
        return unimonCards;
    }

    public void setUnimonCards(ArrayList<UnimonCard> unimonCards) {
        this.unimonCards = unimonCards;
    }

    public Grid[][] getGridLevelTiles() {
        return gridLevelTiles;
    }

    public void setGridLevelTiles(Grid[][] gridLevelTiles) {
        this.gridLevelTiles = gridLevelTiles;
    }

    public int getPlayerGridPosI() {
        return playerGridPosI;
    }

    public void setPlayerGridPosI(int playerGridPosI) {
        this.playerGridPosI = playerGridPosI;
    }

    public int getPlayerGridPosJ() {
        return playerGridPosJ;
    }

    public void setPlayerGridPosJ(int playerGridPosJ) {
        this.playerGridPosJ = playerGridPosJ;
    }
}
