package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.MoveResource;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.Player;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen.PlayScreen;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;


/**
 * Created by Jordan McComb (40156883) and Kyle Bell (40158884) on 20/04/2017.
 */

//This class contains all the methods deal with AI choices
//These include what attack to make, when to evolve, who to sub in and when, when to attack and when to draw cards
//it also handles what happends to dead cards and used hand cards

public class OpponentPlayer extends Player {

    private boolean myTurn;


    //use to check the current number in the graveyard when adding to the graveyard
    private int currentNumberGraveyard = 0;


    public OpponentPlayer(){
        super();
    }

    public boolean getMyTurn(){
        return this.myTurn;
    }

    public void setMyTurn(boolean turn){
        this.myTurn = turn;
    }




    //Jordan McComb and Kyle Bell
    //40156883 / 40158884
    //this method sends Unimon cards to ther graveyard and updates the currentNumberGraveyard variable
    //this does not handle replacement of the card - handled elsewhere
    //This method is called most times cards are used
    public void sendToGraveyard(Card deadCard, BattleSetup AIBattleSetup){
        Card[] currentGraveyard = Arrays.copyOf(AIBattleSetup.getGraveyardCards(), currentNumberGraveyard + 1);
        currentGraveyard[currentNumberGraveyard] = deadCard;
        currentNumberGraveyard++;
        AIBattleSetup.setGraveyardCards(currentGraveyard);
    }

    //THE FOLLOWING METHODS ARE TO DO WITH CARD EVOLUTION, WHETHER OR NOT THEY CAN EVOLVE AND WHAT TO DO WITH THE CURRENT UNIMON
//
    // }
    //Jordan McComb and Kyle Bell
    //40156883/40158884

    //this method runs through the oppoenents bench cards, first selecting which are unimon cards
    //if a unimon card is found, it checks to see whether it is elgible for evoltuion
    //however for the AI insteads of choosing a card it runs all the hand cards through the handCardIsEvolve() method
    public  void checkForEvolution(BattleSetup AIBattleSetup){

        Card[] handCards = AIBattleSetup.getHandCard();
        UnimonCard activeCard = AIBattleSetup.getActiveCard();
        for(int i = 0; i < handCards.length; i++){

            //first check if the hand card is a unimon card
            if(handCards[i] instanceof UnimonCard){
                UnimonCard cardToCheck = (UnimonCard)handCards[i];
                if(handCardisEvolve(cardToCheck, activeCard)) {

                    evolveUnimon(AIBattleSetup, cardToCheck);
                    break;
                }


            }
        }
    }



    //Jordan McComb and Kyle Bell
    //40156883 / 40156884
    //Simliar method to battle.java evolve but now sends card to graveyard and uses AI's battle setup
    public void evolveUnimon(BattleSetup AIBattleSetup, UnimonCard evolveToCard) {
        sendToGraveyard(AIBattleSetup.getActiveCard(), AIBattleSetup);
        AIBattleSetup.setActiveCard(evolveToCard);

    }
    //used James' method from Battle.java
    public static boolean handCardisEvolve(UnimonCard handCard, UnimonCard activeCard) {
        if (activeCard.getEvolveType() == UnimonEvolveType.UNIQUE) {
            return false; //unique evolve type is the highest evolve type, cannot be evolved further.
        }

        //validates whether the active card and hand card share the same name
        if (activeCard.getName().equals(handCard.getName())) {
            if (activeCard.getEvolveType() == UnimonEvolveType.SPIRIT) {
                if (handCard.getEvolveType() == UnimonEvolveType.PHANTOM) {
                    return true;
                }
            } else if (activeCard.getEvolveType() == UnimonEvolveType.PHANTOM) {
                if (handCard.getEvolveType() == UnimonEvolveType.DEMON) {
                    return true;
                }
            }
        } else if (activeCard.getEvolveType() == UnimonEvolveType.DEMON) {
            //if the evolve is demon to unique the active and hand card do not need to share the same name.
            if (handCard.getEvolveType() == UnimonEvolveType.UNIQUE) {
                return true;
            }
        }
        return false; //the hand card is not an evolution of the player's active unimoncard
    }

    //JORDAN MCCOMB AND KYLE BELL
    //40156883 / 40158884
    //METHODS INVOLVING SUBBING OFF A UNIMON AND CHOOSING A SUITABLE REPLACEMENT, THIS CAN BE DONE BY FOR BOTH ALIVE AND DEAD UNIMON



    //jordan mccomb and kyle bell
    //40156883 / 4015884
    //substitute method - called by choose replacement method.
    //this will handle placing a chosen bench card into the active slot and placing the active card into the bench slot
    public void substitute(BattleSetup AIBattleSetup, UnimonCard[] benchCards, int indexOfReplacement) {
        UnimonCard activeCard = AIBattleSetup.getActiveCard(); //the active unimon card took from the bench cards

        AIBattleSetup.setActiveCard(benchCards[indexOfReplacement]); // set Active card from chosen bench card
        benchCards[indexOfReplacement] = activeCard; // place the previous active card where the bench card was

        AIBattleSetup.setBenchCards(benchCards); //updates benchcards to the battlesetup, with replaced active card7
    }

    //jordan mccomb and kyle bell
    //40156883 / 40158884
    //replace Dead  method - called by choose replacement method.
    //this will handle placing a chosen bench card into the active slot and placing the active card into the graveard
    //modified version of substitute method
    public void replaceDead(BattleSetup AIBattleSetup, UnimonCard[] benchCards, int indexOfReplacement) {
        sendToGraveyard(AIBattleSetup.getActiveCard(), AIBattleSetup); //active card sent to graveyard

        AIBattleSetup.setActiveCard(benchCards[indexOfReplacement]); // set Active card from chosen bench card
        benchCards[indexOfReplacement] = null; // no longer a bench card in that slot


        AIBattleSetup.setBenchCards(benchCards); //updates benchcards to the battlesetup, with replaced active card
    }


    //Jordan McComb Kyle Bell
    // 40156883 / 40158884
    //once a card is selected it places it in the active slot
    //checks the HP of the active card to see if it is dead or alive
    //if the card is dead it places the active in the graveyard, if it is alive it places it in the bench
    public void replacementObtained(BattleSetup AIBattleSetup, UnimonCard[] benchCards, int indexOfReplacement ){
        if (AIBattleSetup.getActiveCard().getHealth() > 0) {

            substitute(AIBattleSetup, benchCards, indexOfReplacement);
        } else {
            replaceDead(AIBattleSetup, benchCards, indexOfReplacement);
        }

    }


    //Jordan McComb and Kyle Bell - 40156883/40158884
    //when choosing to retreat an active card the AI will decide a suitable replacement
    //This will check the element of playerones active card and firstly choose a direct counter to that card
    //if a direct counter isn't available to that card, a card that is neutral to that card will bew chosen
    public void chooseReplacement(UnimonCard PlayersactiveCard, BattleSetup AIBattleSetup) {
        int indexOfReplacement = -1;
        UnimonCard[] benchCards = AIBattleSetup.getBenchCards();

        //the following checks if a card exists that is a direct counter to the players active card
        for (int i = 0; i < benchCards.length; i++) {
            Element playerWeaknessElement = PlayersactiveCard.getWeaknessElement();
            Element benchCardElement = benchCards[i].getCardElement();
            if (benchCardElement == playerWeaknessElement) {
                indexOfReplacement = i;
                break;
            }

        }

        if (indexOfReplacement >= 0) {
            replacementObtained(AIBattleSetup, benchCards, indexOfReplacement);
        }

        //if a direct counter doesn't exist, the method will check for a holy or dark card

        for (int i = 0; i < benchCards.length; i++) {

            Element benchCardElement = benchCards[i].getCardElement();
            if (benchCardElement == Element.DARK || benchCardElement == Element.HOLY) {
                indexOfReplacement = i;
                break;
            }
        }

        if (indexOfReplacement >= 0) {
            replacementObtained(AIBattleSetup, benchCards, indexOfReplacement);
        }

        //if holy or dark is not there, a neutral card can be selected (neither weak nor strong)
        //A card will be one that is the same element as the opponents active card
        //it will absorb the opponents active card element and the opponent will absorb its element
        //therefore to damage each other only physical moves can be applied

        for (int i = 0; i < benchCards.length; i++) {

            Element benchCardElement = benchCards[i].getCardElement();
            if (benchCardElement == PlayersactiveCard.getCardElement()) {
                indexOfReplacement = i;
                break;
            }
        }

        if (indexOfReplacement >= 0) {
            replacementObtained(AIBattleSetup, benchCards, indexOfReplacement);
        }

        //worst case scenario for the AI is to get a card that is weak to the players element
        //player one will do extra damage to this card

        for (int i = 0; i < benchCards.length; i++) {

            Element benchCardWeaknessElement = benchCards[i].getWeaknessElement();
            if (benchCardWeaknessElement == PlayersactiveCard.getCardElement()) {
                indexOfReplacement = i;
                break;
            }
        }


        if (indexOfReplacement >= 0) {
            replacementObtained(AIBattleSetup, benchCards, indexOfReplacement);
        }




    } // End of choose Replacement method


    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method applies mana to the active card, called by AutoManaApply
    //uses apply energy method in unimon moves
    public void playManaCard(UnimonCard activeCard, EnergyCard potion) {
        potion.applyEnergy(activeCard);
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method checks hand cards to see if there is an instance of energy Cards
    //then checks to see if that energy is mana
    //if so return the appropriate index where it is found in the hand cards array

    public int findManaCard(BattleSetup AIBattleSetup){
        Card[] handCards = AIBattleSetup.getHandCard();
        // checks if there are any energy cards in hand
        // if there is an energy card, compare that type to the type needed, in this case, mana

        for(int i = 0; i < handCards.length; i++) {
            if(handCards[i] instanceof EnergyCard) {
                EnergyCard potion = (EnergyCard) handCards[i];
                if (potion.getType() == EnergyType.MANA) {
                    return i;
                }
            }
        }

        return -1;
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method is called at the start of a unimons turn to see if it is low on mana
    //different values will be used for easy and hard difficulty
    //uses index found by findManaCard to locate a mana card in the hand cards then plays it using the playManaCard method
    //Mana card then sent to graveyard
    public void AutoManaApply(BattleSetup AIBattleSetup, double minimumPercent){

        double fraction = 100 / minimumPercent;
        double minimumValue = AIBattleSetup.getActiveCard().getHealth() / fraction;
        if(AIBattleSetup.getActiveCard().getMana() <= minimumValue) {
            int index = findManaCard(AIBattleSetup);
            if(index >= 0) {
                Card[] handCards = AIBattleSetup.getHandCard();
                EnergyCard cardToApply = (EnergyCard) handCards[index];
                playManaCard(AIBattleSetup.getActiveCard(), cardToApply);
                handCards[index] = null;
                sendToGraveyard(cardToApply, AIBattleSetup);


            }
        }

    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method is used when a unimon does not have enough mana to make a turn
    //uses index found by findManaCard to locate a mana card in the hand cards then plays it using the playManaCard method
    //Mana card then sent to graveyard
    public void ManualManaApply(BattleSetup AIBattleSetup){

        int index = findManaCard(AIBattleSetup);
        if(index >= 0) {
            Card[] handCards = AIBattleSetup.getHandCard();
            EnergyCard cardToApply = (EnergyCard) handCards[index];
            playManaCard(AIBattleSetup.getActiveCard(), cardToApply);
            handCards[index] = null;
            sendToGraveyard(cardToApply, AIBattleSetup);



        }

    }


    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method applies stamina to the active card, called by AutoStaminaApply
    //uses apply energy method in unimon moves

    public void playStaminaCard(UnimonCard activeCard, EnergyCard potion) {
        potion.applyEnergy(activeCard);
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method checks hand cards to see if there is an instance of energy Cards
    //then checks to see if that energy is stamina
    //if so return the appropriate index where it is found in the hand cards array

    public int findStaminaCard(BattleSetup AIBattleSetup) {
        Card[] handCards = AIBattleSetup.getHandCard();
        // checks if there are any energy cards in hand
        // if there is an energy card, compare that type to the type needed, in this case, stamina

        for (int i = 0; i < handCards.length; i++) {
            if(handCards[i] instanceof EnergyCard) {
                EnergyCard potion = (EnergyCard) handCards[i];
                if(potion.getType() == EnergyType.STAMINA) {
                    return i;
                }
            }
        }

        return -1;
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method is called at the start of a unimons turn to see if it is low on stamina
    //different values will be used for easy and hard difficulty
    //uses index found by findStaminaCard to locate a stamina card in the hand cards then plays it using the playStaminaCard method
    //stamina card then sent to graveyard

    public void AutoStaminaApply(BattleSetup AIBattleSetup, double minimumPercent) {
        double fraction = 100 / minimumPercent;
        double minimumValue = AIBattleSetup.getActiveCard().getHealth() / fraction;
        if(AIBattleSetup.getActiveCard().getStamina() <= minimumValue) {
            int index = findStaminaCard(AIBattleSetup);
            if(index >= 0) {
                Card[] handCards = AIBattleSetup.getHandCard();
                EnergyCard cardToApply = (EnergyCard) handCards[index];
                playStaminaCard(AIBattleSetup.getActiveCard(), cardToApply);
                handCards[index] = null;
                sendToGraveyard(cardToApply, AIBattleSetup);
            }
        }
    }


    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method is called when a unimon does not have enough stamina to make a move
    //uses index found by findStaminaCard to locate a stamina card in the hand cards then plays it using the playStaminaCard method
    //stamina card then sent to graveyard
    public void ManualStaminaApply(BattleSetup AIBattleSetup) {

        int index = findStaminaCard(AIBattleSetup);
        if(index >= 0) {
            Card[] handCards = AIBattleSetup.getHandCard();
            EnergyCard cardToApply = (EnergyCard) handCards[index];
            playStaminaCard(AIBattleSetup.getActiveCard(), cardToApply);
            handCards[index] = null;
            sendToGraveyard(cardToApply, AIBattleSetup);

        }
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method applies health to the active card, called by AutoHealthApply
    //uses apply energy method in unimon moves

    public void playHealthCard(UnimonCard activeCard, EnergyCard potion) {
        potion.applyEnergy(activeCard);
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method checks hand cards to see if there is an instance of energy Cards
    //then checks to see if that energy is health
    //if so return the appropriate index where it is found in the hand cards array

    public int findHealthCard(BattleSetup AIBattleSetup) {
        Card[] handCards = AIBattleSetup.getHandCard();

        for (int i = 0; i < handCards.length; i++) {
            if(handCards[i] instanceof EnergyCard) {
                EnergyCard potion = (EnergyCard) handCards[i];
                if(potion.getType() == EnergyType.HEALTH) {
                    return i;
                }
            }
        }

        return -1;
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method is called at the start of a unimons turn to see if it is low on health
    //different values will be used for easy and hard difficulty
    //uses index found by findHealthCard to locate a health card in the hand cards then plays it using the playHealthCard method
    //health card then sent to graveyard

    public void autoHealthApply(BattleSetup AIBattleSetup, double minimumPercent) {
        double fraction = 100 / minimumPercent;
        double minimumValue = AIBattleSetup.getActiveCard().getHealth() / fraction;
        if(AIBattleSetup.getActiveCard().getHealth() <= minimumValue) {
            int index = findHealthCard(AIBattleSetup);
            if(index >= 0) {
                Card[] handCards = AIBattleSetup.getHandCard();
                EnergyCard cardToApply = (EnergyCard) handCards[index];
                playHealthCard(AIBattleSetup.getActiveCard(), cardToApply);
                handCards[index] = null;
                sendToGraveyard(cardToApply, AIBattleSetup);
            }
        }
    }

    //Jordan McComb and Kyle Bell
    //40156883 and 40158884
    //This method is called when a unimon cannot attack, and it is not necessary to use stamina or mana
    //uses index found by findHealthCard to locate a health card in the hand cards then plays it using the playHealthCard method
    //health card then sent to graveyard

    public void ManualHealthApply(BattleSetup AIBattleSetup) {

        int index = findHealthCard(AIBattleSetup);
        if(index >= 0) {
            Card[] handCards = AIBattleSetup.getHandCard();
            EnergyCard cardToApply = (EnergyCard) handCards[index];
            playHealthCard(AIBattleSetup.getActiveCard(), cardToApply);
            handCards[index] = null;
            sendToGraveyard(cardToApply, AIBattleSetup);

        }
    }

    //Jordan McComb and Kyle Bell
    //40156883 / 40158884
    //This is the method in which the AI chooses its best option for attacking the player
    //Gets the best move and checks if it can be done, the the 2nd best and then the 3rd
    //if they cannot be done applies a potion
    //then checks the moves again
    //if still not available it replaces the active card


    public void chooseEfficientAttack(BattleSetup AIBattleSetup, UnimonCard playerActiveCard) {
        UnimonMoves[] cardMoves = AIBattleSetup.getActiveCard().getMoves();
        int bestMove = -1;
        int mediumMove = -2;
        int worstMove = -3;

        if(moveEfficiency(cardMoves[0]) >= moveEfficiency(cardMoves[1]) && moveEfficiency(cardMoves[0]) >= moveEfficiency(cardMoves[2]) ){ //in this case move 0 has the highest efficinecy (or equivalent to the others)
            bestMove = 0;
            if(moveEfficiency(cardMoves[1]) >= moveEfficiency(cardMoves[2])){
                mediumMove = 1;
                worstMove = 2;
            } else{
                mediumMove = 2;
                worstMove = 1;

            }
        } else if(moveEfficiency(cardMoves[1]) > moveEfficiency(cardMoves[0]) && moveEfficiency(cardMoves[1]) >= moveEfficiency(cardMoves[2])){ //in this case moves 1 has the highest efficinecy (or equal to 2 but greater than 0)
            bestMove = 1;
            if(moveEfficiency(cardMoves[0]) >= moveEfficiency(cardMoves[2])){
                mediumMove = 0;
                worstMove = 2;
            } else{
                mediumMove = 2;
                worstMove = 0;

            }

        }else{ //this is the case when move 2 has the highest efficiecny
            bestMove = 2;
            if(moveEfficiency(cardMoves[0]) >= moveEfficiency(cardMoves[1])){
                mediumMove = 0;
                worstMove = 1;
            } else{
                mediumMove = 1;
                worstMove = 0;

            }


        } //END OF SORTING EFFICIENCY

        if((cardMoves[bestMove].checkingStamina(AIBattleSetup.getActiveCard(), cardMoves[bestMove] )) && (cardMoves[bestMove].checkingMana(AIBattleSetup.getActiveCard(), cardMoves[bestMove] ))) {
            cardMoves[bestMove].doMove(AIBattleSetup.getActiveCard(), playerActiveCard, cardMoves[bestMove]);
            //if the best move is available it is done
        } else if((cardMoves[mediumMove].checkingStamina(AIBattleSetup.getActiveCard(), cardMoves[mediumMove] )) && (cardMoves[mediumMove].checkingMana(AIBattleSetup.getActiveCard(), cardMoves[mediumMove] ))) {
            cardMoves[mediumMove].doMove(AIBattleSetup.getActiveCard(), playerActiveCard, cardMoves[mediumMove]);
            //if not, the second best move is tried and done if available
        } else if ((cardMoves[worstMove].checkingStamina(AIBattleSetup.getActiveCard(), cardMoves[worstMove] )) && (cardMoves[worstMove].checkingMana(AIBattleSetup.getActiveCard(), cardMoves[worstMove] ))) {
            cardMoves[worstMove].doMove(AIBattleSetup.getActiveCard(), playerActiveCard, cardMoves[worstMove]);
            //if not, the third best move is tried and done if available
        } else{ //A potion will be applied and check the moves again
            if(AIBattleSetup.getActiveCard().getMana() <= AIBattleSetup.getActiveCard().getStamina()){
                ManualManaApply(AIBattleSetup);
            } else{
                ManualStaminaApply(AIBattleSetup);
            }
            if((cardMoves[bestMove].checkingStamina(AIBattleSetup.getActiveCard(), cardMoves[bestMove] )) && (cardMoves[bestMove].checkingMana(AIBattleSetup.getActiveCard(), cardMoves[bestMove] ))) {
                cardMoves[bestMove].doMove(AIBattleSetup.getActiveCard(), playerActiveCard, cardMoves[bestMove]);
                //if the best move is available it is done
            } else if((cardMoves[mediumMove].checkingStamina(AIBattleSetup.getActiveCard(), cardMoves[mediumMove] )) && (cardMoves[mediumMove].checkingMana(AIBattleSetup.getActiveCard(), cardMoves[mediumMove] ))) {
                cardMoves[mediumMove].doMove(AIBattleSetup.getActiveCard(), playerActiveCard, cardMoves[mediumMove]);
                //if not, the second best move is tried and done if available
            } else if ((cardMoves[worstMove].checkingStamina(AIBattleSetup.getActiveCard(), cardMoves[worstMove] )) && (cardMoves[worstMove].checkingMana(AIBattleSetup.getActiveCard(), cardMoves[worstMove] ))) {
                cardMoves[worstMove].doMove(AIBattleSetup.getActiveCard(), playerActiveCard, cardMoves[worstMove]);
                //if not, the third best move is tried and done if available
            } else{

                //if a move cannot be made after the potion was applied the card is substituted
                chooseReplacement(playerActiveCard, AIBattleSetup);
            }


        }




    } //End of chooseEfficientAttackMethod

    //Jordan McComb and Kyle Bell
    //40156883 / 40158884
    //This method totals up the requirement of stamina and mana that a move costs
    //It didvides the base damage by this to get how efficient the move is (damage/cost)
    //method will be called when deciding what move to play
    //method called by chooseEfficientAttack method
    public double moveEfficiency(UnimonMoves move){

        int manaCost = move.getMovesReq().get(MoveResource.MANA);
        int staminaCost = move.getMovesReq().get(MoveResource.STAMINA);
        double totalCost = (double) manaCost + (double) staminaCost;
        double moveEfficiency = move.getBaseDamage() / totalCost;
        return moveEfficiency;


    }

    //Jordan McComb and Kyle Bell
    //40156883 / 40158884
    // This method returns the opponents highest base damage potential
    //used to predict whether or not the HARD DIFFICULTY AI should use a pre-emptive health potion
    //implemented in AIFullTurnCycle method (battle.java)

    public int GetOpponentsHighestDamage(UnimonCard playersActiveCard){
        int highestDamage = -1;
        UnimonMoves[] playerMoves = playersActiveCard.getMoves();

        if(playerMoves[0].getBaseDamage() >= playerMoves[1].getBaseDamage() && playerMoves[0].getBaseDamage() >= playerMoves[2].getBaseDamage()){
            highestDamage = playerMoves[0].getBaseDamage();
        } else if(playerMoves[1].getBaseDamage() > playerMoves[0].getBaseDamage() && playerMoves[1].getBaseDamage() >= playerMoves[2].getBaseDamage()){
            highestDamage = playerMoves[1].getBaseDamage();
        } else{
            highestDamage = playerMoves[2].getBaseDamage();
        }

        return highestDamage;
    }




    //Jordan McComb and Kyle Bell
    //40156883 / 40158884
    //This method checks if there is an opening in the players hand cardws and if there is draws on the the deck
    //modified version of draw method in battle.java
    //now no longer an int return and sets the new play area deck and bench cards as the AI's
    //no longer requires a boolean as checks using the index value
    public void drawCard (BattleSetup AIBattleSetup){

        Card[] playerHandCards = AIBattleSetup.getHandCard();
        Stack<Card> playerDeck = AIBattleSetup.getPlayAreaDeck();

        int indexToAdd = -1; //the index of the card drawn from the deck to add to the list of hand cards


        for (int i = 0; i < playerHandCards.length; i++) {
            if (playerHandCards[i] == null) { //an empty hand card
                indexToAdd = i; //the index of the handcard to be added
                break; //adds to the first opening - no need to iterate further
            }
        }

        if (!playerDeck.isEmpty() && indexToAdd >= 0) { //there is a card to draw (deck is not empty) and an opening for a new hand card
            Card handCardToAdd = playerDeck.pop(); //draw the card from the top of the deck
            playerHandCards[indexToAdd] = handCardToAdd; //add the drawn card to the hand card list
        }

        AIBattleSetup.setHandCard(playerHandCards);
        AIBattleSetup.setPlayAreaDeck(playerDeck);



    }



}


