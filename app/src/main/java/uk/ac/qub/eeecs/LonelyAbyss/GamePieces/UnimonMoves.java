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

    /**
     * Enum class of card Energy types for moves
     */
    public enum energy{
        MANA,STAMINA
    }

    /**
     * Bitmap image to represent the type of move
     */
    private Bitmap icon;

    /**
     * Description of the move
     */
    private String description;

    /**
     * Map of the energy type to make the move and the amount of that energy to do so
     */
    private Map<energy,Integer>moves;

    /**
     * This is a constructor method for the unimon card object.
     * @param name - the name of the move
     * @param icon - icon to represent the type of move
     * @param description - What the attack does
     * @param moves - the energy needed to make the move and how much of that energy is needed
     */
    public UnimonMoves(String name, Bitmap icon, String description,Map<energy,Integer>moves){
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.moves = new HashMap<energy, Integer>(moves);
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
    /**
     * This method sets the description of the move
     * @param description - a description of what the move does
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Map<energy, Integer> getMoves() {
        return this.moves;
    }
    /**
     * This method sets the energy type and energy need to reform move
     * @param moves - Map of the energy need to preform move
     */
    public void setMoves(Map<energy, Integer> moves) {
        this.moves = new HashMap<energy, Integer>(moves) ;
    }


//!!NOT COMPLETED. WAITING ON CALCULATION METHODS TO APPLY!!(I "Patrick" will finish it when they are ready.
    public void doMove(UnimonCard playerCard, UnimonCard opponentCard){
        UnimonMoves[] tempArray = playerCard.getMoves();
        for(UnimonMoves move: tempArray) {
            if(!(moves.containsKey(energy.MANA))){
                if((move.getMoves().get(energy.STAMINA)) == playerCard.getStamina()){

                }
            }else if(!(moves.containsKey(energy.STAMINA))) {
                if((move.getMoves().get(energy.MANA)) == playerCard.getMana()){

                }
            }
                /*var = move.getMoves().get(energy.values());*/

        }
    }



}
