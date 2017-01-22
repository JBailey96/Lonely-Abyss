package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrick on 15/01/2017.
 */

public class UnimonMoves {
    /**
     * Name of the move
     */
    private String name;

    private MoveResource moveResource;
    /**
     * Bitmap image to represent the type of move
     */
    private Bitmap icon;

    private int baseDamage;
    private StatusEffect statusEffect1;
    private int statusEffectCounter1;
    private StatusEffect statusEffect2;
    private int statusEffectCounter2;
    private StatusEffect statusEffect3;
    private int statusEffectCounter3;

    private MoveType moveType;


    /**
     * Description of the move
     */
    private String description;


    /**
     * Map of the energy type to make the move and the amount of that energy to do so
     */
    private Map<MoveResource,Integer>moves;

    /**
     * This is a constructor method for the unimon card object.
     * @param name - the name of the move
     * @param icon - icon to represent the type of move
     * @param description - What the attack does
     * @param moves - the energy needed to make the move and how much of that energy is needed
     */
    public UnimonMoves(String name, Bitmap icon, String description,Map<MoveResource,Integer>moves, int baseDamage, StatusEffect statusEffect1, int statusEffectCounter1, StatusEffect statusEffect2, int statusEffectCounter2, StatusEffect statusEffect3, int statusEffectCounter3, MoveType moveType ){
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.moves = new HashMap<MoveResource, Integer>(moves);
        this.baseDamage = baseDamage;
        this.statusEffect1 = statusEffect1;
        this.statusEffectCounter1 = statusEffectCounter1;
        this.statusEffect2 = statusEffect2;
        this.statusEffectCounter2 = statusEffectCounter2;
        this.statusEffect3 = statusEffect3;
        this.statusEffectCounter3 = statusEffectCounter3;
        this.moveType = moveType;
    }

    public String getName() {
        return this.name;
    }
    /**
     * This method sets the name of the move
     * @param name - move name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIcon() {
        return this.icon;
    }
    /**
     * This method sets the backGround image of the card
     * @param icon - the  icon image to represent the move
     */
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return this.description;
    }

    //getters and setters for status effects

    public StatusEffect getStatusEffect1(){ return statusEffect1; }

    public void setStatusEffect1( StatusEffect statusEffect1){ this.statusEffect1 = statusEffect1; }

    public StatusEffect getStatusEffect2(){ return statusEffect2; }

    public void setStatusEffect2( StatusEffect statusEffect2){ this.statusEffect2 = statusEffect2; }

    public StatusEffect getStatusEffect3(){ return statusEffect3; }

    public void setStatusEffect3( StatusEffect statusEffect3){ this.statusEffect3 = statusEffect3; }



    public MoveType getMoveType() { return moveType;}
    /**
     * This method sets the description of the move
     * @param description - a description of what the move does
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Map<MoveResource, Integer> getMoves() {
        return this.moves;
    }
    /**
     * This method sets the energy type and energy need to reform move
     * @param moves - Map of the energy need to preform move
     */
    public void setMoves(Map<MoveResource, Integer> moves) {
        this.moves = new HashMap<MoveResource, Integer>(moves) ;
    }

    public void physicalAttack (UnimonCard playerCard, UnimonCard opponentCard){
        int damage = (int) (baseDamage * ((100 - opponentCard.getArmourValue()) / 100));
        opponentCard.decreaseHealth(damage);
    }

    public boolean checkWeakness(UnimonCard playerCard, UnimonCard opponentCard){

        if ( playerCard.getWeaknessElement() == Element.FIRE && opponentCard.getWeaknessElement() == Element.EARTH)  { return true; }
            else if  ( playerCard.getWeaknessElement() == Element.EARTH && opponentCard.getWeaknessElement() == Element.WATER)  { return true; }
            else if  ( playerCard.getWeaknessElement() == Element.WATER && opponentCard.getWeaknessElement() == Element.FIRE)  { return true; }
            else if  ( playerCard.getWeaknessElement() == Element.DARK && opponentCard.getWeaknessElement() == Element.HOLY)  { return true; }
            else if  ( playerCard.getWeaknessElement() == Element.HOLY && opponentCard.getWeaknessElement() == Element.DARK)  { return true; }
                else { return false; }
    }

    public boolean checkAbsorbtion(UnimonCard playerCard, UnimonCard opponentCard){
            if(playerCard.getAbsorbtionElement() == opponentCard.getAbsorbtionElement()){  return true; }
                else { return false; }
    }

    public void elementalAttack (UnimonCard playerCard, UnimonCard opponentCard){
        if(checkWeakness(playerCard, opponentCard)) {
            int damage = (int) (baseDamage * opponentCard.getWeaknessValue());
            opponentCard.decreaseHealth(damage);    }
        else if (checkAbsorbtion(playerCard, opponentCard)) {
                int healthAbsorbed = (int) (baseDamage * (opponentCard.getAbsorptionValue() / 100));
                opponentCard.increaseHealth(healthAbsorbed); }
        else{
                opponentCard.decreaseHealth(baseDamage);   } //case when the element is neither a weakness nor absorbtion

            }

    public void statusAttack(UnimonCard playerCard, UnimonCard opponentCard){
        opponentCard.setStatusEffect1(this.getStatusEffect1());
        opponentCard.setStatusEffect2(this.getStatusEffect2());
        opponentCard.setStatusEffect3(this.getStatusEffect3());

    }


    //PATRICK, THIS IS HOW I WOULD GO ABOUT DOING IT, BUT FEEL FREE TO DO IT DIFFERENTLY IF YOU FEEL A HASHMAP WOULD BE BETTER. NOTE; IF YOU ARE TO CHANGE THIS, THE THREE METHODS (PHYSATTACK,ELEMATTACK AND STATUSATTACK) ARE THE WAY TO MATHEMATICALLY TAKE INTO ALL STATS INTO CONSIDERATION AND HOW WEAKNESS AND ABSORTBTION WORK

    public void selectAttack(UnimonCard playerCard, UnimonCard opponentCard, UnimonMoves generalMove){
        switch (generalMove.moveType){
            case PHYSICAL:
                physicalAttack(playerCard, opponentCard);
                break;
            case ELEMENTAL:
                elementalAttack(playerCard, opponentCard);
                break;
            case STATUS:
                statusAttack(playerCard, opponentCard);
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
     * @param generalMove - generalMove
     */
    public void doMove(UnimonCard playerCard, UnimonCard opponentCard,UnimonMoves generalMove){
        UnimonMoves[] tempArray = playerCard.getMoves();
        for(UnimonMoves move: tempArray) {
            if(!(move.getMoves().containsKey(MoveResource.MANA))){
               if(checkingStanmina(playerCard, move)){
                   selectAttack(playerCard, opponentCard,generalMove);
                   playerCard.decreaseStamina(move.getMoves().get(MoveResource.STAMINA));
               }
            }else if(!(moves.containsKey(MoveResource.STAMINA))) {
                if(checkingMana(playerCard,move)){
                    selectAttack(playerCard, opponentCard, generalMove);
                    playerCard.decreaseStamina(move.getMoves().get(MoveResource.MANA));
                }
            }else if((moves.containsKey(MoveResource.STAMINA)) && (moves.containsKey(MoveResource.MANA))){
                if((checkingStanmina(playerCard,move)) && (checkingMana(playerCard,move))){
                    selectAttack(playerCard, opponentCard, generalMove);
                    playerCard.decreaseStamina(move.getMoves().get(MoveResource.STAMINA));
                    playerCard.decreaseStamina(move.getMoves().get(MoveResource.MANA));
                }
            }
        }
    }

    /**
     * This method returns true if the players card has enough stamina to do the move
     * @param playerCard - the players card
     * @param move - temp array of moves
     */
    public boolean checkingStanmina(UnimonCard playerCard,UnimonMoves move){
            if(playerCard.getStamina() >= (move.getMoves().get(MoveResource.STAMINA))){
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
             if(playerCard.getMana() >= (move.getMoves().get(MoveResource.MANA))) {
                 return true;
             }
        return false;
    }




}
