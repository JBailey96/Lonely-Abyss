package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;

/**
 * Created by Patrick on 15/01/2017.
 */

public class UnimonMoves {
    /**
     * Name of the move
     */
    private String name;

    /**
     * Description of the move
     */
    private String description;

    private MoveType moveType; //the type of move.

    /**
     * Bitmap image to represent the type of move
     */
    private Bitmap icon;

    private int baseDamage; //the base damage of the move before the damage is increased/reduced by the opponent's weakness/absorption/armour stats

    private Map<StatusEffect, Integer> statusEffects; ///The status effect the move inflicts upon the opponent's unimon card and how many turns it is to be applied for.




    /**
     * Map of the energy type to make the move and the amount of that energy to do so
     */
    private Map<MoveResource,Integer> movesReq;

    /**
     * This is a constructor method for the unimon card object.
     * @param name - the name of the move
     * @param icon - icon to represent the type of move
     * @param description - What the attack does
     * @param movesReq - the energy needed to make the move and how much of that energy is needed
     */
    public UnimonMoves(String name, Bitmap icon, String description,Map<MoveResource,Integer>movesReq, int baseDamage, MoveType moveType, Map<StatusEffect, Integer> statusEffects){
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.movesReq = new HashMap<MoveResource, Integer>(movesReq);
        this.baseDamage = baseDamage;
        this.statusEffects = statusEffects;
        this.moveType = moveType;
    }

    /**
     * This method sets the name of the move
     * @param name - move name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * This method sets the backGround image of the card
     * @param icon - the  icon image to represent the move
     */
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getIcon() {
        return this.icon;
    }

    public String getDescription() {
        return this.description;
    }

    //getters and setters for status effects
    public Map<StatusEffect, Integer> getStatusEffects() {
        return statusEffects;
    }

    public void setStatusEffects(Map<StatusEffect, Integer> statusEffects) {
        this.statusEffects = statusEffects;
    }

    /**
     * This method sets the description of the move
     * @param description - a description of what the move does
     */
    public void setDescription(String description) {
        this.description = description;
    }


    public MoveType getMoveType() { return moveType;}

    public Map<MoveResource, Integer> getMovesReq() {
        return this.movesReq;
    }
    /**
     * This method sets the energy type and energy need to reform move
     * @param moves - Map of the energy need to preform move
     */
    public void setMovesReq(Map<MoveResource, Integer> moves) {
        this.movesReq = new HashMap<MoveResource, Integer>(moves) ;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    //perform a physical attack on the opponent's card
    public void physicalAttack (UnimonCard opponentCard){
        int damage = opponentCard.processArmourDAM(getBaseDamage());
        opponentCard.decreaseHealth(damage);
    }

    //checks whether the weakness element of the opponent card matches the player's card
    public boolean checkWeakness(UnimonCard playerCard, UnimonCard opponentCard) {
            if (playerCard.getCardElement() ==  opponentCard.getWeaknessElement())  {
                return true;
            }
            return false;
    }

    //checks whether the absorption element of the opponent card matches the player's card
    public boolean checkAbsorption(UnimonCard playerCard, UnimonCard opponentCard){
            if(playerCard.getCardElement() == opponentCard.getAbsorptionElement()){
                return true;
            }
                return false;
    }

    //perform an elemental attack on the opponent's card
    public void elementalAttack (UnimonCard playerCard, UnimonCard opponentCard){
        if(checkWeakness(playerCard, opponentCard)) { //when the player's unimon card element attack is the opponent's weakness element
            int damage = opponentCard.processWeaknessDAM(baseDamage); //process how much damage the attack makes by the opponent's weakness to the element
            opponentCard.decreaseHealth(damage); //decrease health of opponent's card
        }
        else if (checkAbsorption(playerCard, opponentCard)) { //when the player's unimon card element attack is the opponent's absorb element
                int healthAbsorbed = opponentCard.processAbsorb(baseDamage); // gets the amount of health to be replenished based on the absorption stat and damage inflicted
                opponentCard.increaseHealth(healthAbsorbed);  //increase health of opponent's card
        }
        else{
                opponentCard.decreaseHealth(baseDamage);   } //when the element is neither a weakness nor absorption of the opponent
            }


    public void statusAttack(UnimonCard opponentCard){
        opponentCard.setStatusEffects(getStatusEffects());
    }


     //select which type of attack to use against the opponent's card
    public void selectAttack(UnimonCard playerCard, UnimonCard opponentCard, UnimonMoves generalMove) {
        switch (generalMove.moveType) {
            case PHYSICAL:
                physicalAttack(opponentCard);
                break;
            case ELEMENTAL:
                elementalAttack(playerCard, opponentCard);
                break;
            case STATUS:
                statusAttack(opponentCard);
                break;
            default:
                break;
        }
    }


    /**
     * This method sets up a temp array of moves and sets it equal to the array of moves the players card has
     * It then runs a loop through the temp array
     * It then checks that the hash map of this move does not contain the key mana. If it doesn't it calls the checkStamina method
     * If it returns true it calls the selectAttack method and deducts the stamina need for that move from the cards current stamina value.
     * It then does similar for mana and finally checks if the hash map of the move contains both keys stamina and mana.
     * then if so calls the selectAttack method and deducts both mana and stamina from there current values.
     * [THE REASON FOR !NOT MANA/STAMINA- if we call a move which requires both mana and stamina and we check to see if it contains the key mana,
     * which it would we would call the select attack method and then deduct the mana from the current value.
     * However this move required both stamina and mana and stamina would never have been taken into account.
     * (if not !NOT we never would access the last if condition)
     * @param playerCard - the players card
     * @param opponentCard - the opponents card
     * @param move - generalMove
     */
    public void doMove(UnimonCard playerCard, UnimonCard opponentCard, UnimonMoves move){
            if(!(move.getMovesReq().containsKey(MoveResource.MANA))){
               if(checkingStamina(playerCard, move)){
                   selectAttack(playerCard, opponentCard, move);
                   playerCard.decreaseStamina(move.getMovesReq().get(MoveResource.STAMINA));
               }
            }else if(!(move.getMovesReq().containsKey(MoveResource.STAMINA))) {
                if(checkingMana(playerCard,move)){
                    selectAttack(playerCard, opponentCard, move);
                    playerCard.decreaseStamina(move.getMovesReq().get(MoveResource.MANA));
                }
            }else if((move.getMovesReq().containsKey(MoveResource.STAMINA)) && (move.getMovesReq().containsKey(MoveResource.MANA))){
                if((checkingStamina(playerCard,move)) && (checkingMana(playerCard,move))){
                    selectAttack(playerCard, opponentCard, move);
                    playerCard.decreaseStamina(move.getMovesReq().get(MoveResource.STAMINA));
                    playerCard.decreaseStamina(move.getMovesReq().get(MoveResource.MANA));
                }
            }
    }

    /**
     * This method returns true if the player's card has enough stamina to do the move
     * @param playerCard - the players card
     * @param move - temp array of moves
     */
    public boolean checkingStamina(UnimonCard playerCard,UnimonMoves move){
            if(playerCard.getStamina() >= (move.getMovesReq().get(MoveResource.STAMINA))){
                return true;
            }
        return false;
    }

    /**
     * This method returns true if the players card has enough mana to do the move
     * @param playerCard - the players card
     * @param move - temp array of moves
     */
    public boolean checkingMana(UnimonCard playerCard, UnimonMoves move){
             if(playerCard.getMana() >= (move.getMovesReq().get(MoveResource.MANA))) {
                 return true;
             }
        return false;
    }
}
