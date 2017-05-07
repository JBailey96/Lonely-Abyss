package uk.ac.qub.eeecs.LonelyAbyss.DiscontinuedCode;

import java.util.ArrayList;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.BattleSetup;

/**
 * Created by JP on 06/05/2017.
 */

public class Opponent {

    private BattleSetup opponentBattleSetup; //the opponent's battle setup they bring into battle
    private ArrayList<EnergyCard> energyCards;
    private ArrayList<UnimonCard> unimonCards;

    public Opponent(){

    }

    public BattleSetup getOpponentBattleSetup() {
        return opponentBattleSetup;
    }

    public void setOpponentBattleSetup(BattleSetup opponentBattleSetup) {
        this.opponentBattleSetup = opponentBattleSetup;
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
