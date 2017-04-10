package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player;

import java.util.ArrayList;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;

/**
 * Created by James on 05/02/2017.
 */

public class Player {
    private BattleSetup playerBattleSetup; //the player's battle setup they bring into battle
    private ArrayList<EnergyCard> energyCards;
    private ArrayList<UnimonCard> unimonCards; // cards the player possesses

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
}
