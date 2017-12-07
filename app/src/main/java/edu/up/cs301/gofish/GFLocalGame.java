package edu.up.cs301.gofish;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * The LocalGame class for a Go Fish game.  Defines and enforces
 * the game rules and handles interactions between players.
 * 
 * @author Alex Costa, Jackson Brooke, Logan Crawford
 * @version November 2017
 */

public class GFLocalGame extends LocalGame {

	/*Instance Variables*/

	//instance variable for the state of the game
    GFState state;


    /**
     * Constructor for the GFLocalGame.
     */
    public GFLocalGame(int numberOfPlayers) {
        Log.i("GFLocalGame", "creating game");
        // create the state for the beginning of the game
        state = new GFState(numberOfPlayers);
    }


    /**
     * Checks whether the game is over; if so, returns a string giving the result. Else,
	 * the method returns null, signifying that the game is not over.
     * 
     * @result
     * 		the end-of-game message, or null if the game is not over
     */
    @Override
    protected String checkIfGameOver() {

    	//if the number of cards in the discard pile is 52, the game is over.
		//else, the game is not and the function will return null
    	if(state.getHand(5).size() == 52){

    		int player1Score = state.getScore(0);
    		int player2Score = state.getScore(1);
			int player3Score = 0;	//initialized to 0 to avoid warnings
			int player4Score = 0;	//initialized to 0 to avoid warnings
    		if(state.getNumPlayers() > 2){player3Score = state.getScore(2);}
    		if(state.getNumPlayers() > 3){player4Score = state.getScore(3);}

    		switch(state.getNumPlayers()){
    			//if only two players are playing, only a couple situations can play out
				//either player 1 wins, player 2 wins, or they happen to tie
				case 2:
					//individual winners
					if(player1Score > player2Score) return this.playerNames[0]+" is the winner!";
					if(player1Score < player2Score) return this.playerNames[1]+" is the winner!";

					//case where everyone tied
					return this.playerNames[0]+" and "+this.playerNames[1]+" have tied...";

				//with three players, either one player wins outright, or two players tie,
				//or in a rare situation, all three players tie at the same time
				case 3:
					//individual winners
					if(player1Score > player2Score && player1Score > player3Score) return this.playerNames[0]+" is the winner!";
					if(player2Score > player1Score && player2Score > player3Score) return this.playerNames[1]+" is the winner!";
					if(player3Score > player1Score && player3Score > player2Score) return this.playerNames[2]+" is the winner!";

					//case where two people tied
					if(player1Score == player2Score && player1Score > player3Score) return this.playerNames[0]+" and "+this.playerNames[1]+" have tied...";
					if(player1Score == player3Score && player1Score > player2Score) return this.playerNames[0]+" and "+this.playerNames[2]+" have tied...";
					if(player2Score == player3Score && player2Score > player1Score) return this.playerNames[1]+" and "+this.playerNames[2]+" have tied...";

					//case where everyone tied
					return this.playerNames[0]+", "+this.playerNames[1]+", and "+this.playerNames[2]+" have all tied...";

				//with four players, either one player wins outright, two players tie, three
				//players tie, or in the most rare situation, all four players tie at the same time
				case 4:
					//individual winners
					if(player1Score > player2Score && player1Score > player3Score && player1Score > player4Score) return this.playerNames[0]+" is the winner!";
					if(player2Score > player1Score && player2Score > player3Score && player2Score > player4Score) return this.playerNames[1]+" is the winner!";
					if(player3Score > player1Score && player3Score > player2Score && player3Score > player4Score) return this.playerNames[2]+" is the winner!";
					if(player4Score > player1Score && player4Score > player2Score && player4Score > player3Score) return this.playerNames[3]+" is the winner!";

					//case where two people tied
					if(player1Score == player2Score && player1Score > player3Score && player1Score > player4Score)
						return this.playerNames[0]+" and "+this.playerNames[1]+" have tied...";						//player 1 and 2 tie
					if(player1Score == player3Score && player1Score > player2Score && player1Score > player4Score)
						return this.playerNames[0]+" and "+this.playerNames[2]+" have tied...";						//player 1 and 3 tie
					if(player1Score == player4Score && player1Score > player2Score && player1Score > player3Score)
						return this.playerNames[0]+" and "+this.playerNames[3]+" have tied...";						//player 1 and 4 tie
					if(player2Score == player3Score && player2Score > player1Score && player2Score > player4Score)
						return this.playerNames[1]+" and "+this.playerNames[2]+" have tied...";						//player 2 and 3 tie
					if(player2Score == player4Score && player2Score > player1Score && player2Score > player3Score)
						return this.playerNames[1]+" and "+this.playerNames[3]+" have tied...";						//player 2 and 4 tie
					if(player3Score == player4Score && player3Score > player1Score && player3Score > player2Score)
						return this.playerNames[2]+" and "+this.playerNames[3]+" have tied...";						//player 3 and 4 tie

					//case where three people tied
					if(player1Score == player2Score && player1Score == player3Score && player1Score > player4Score)
						return this.playerNames[0]+", "+this.playerNames[1]+", and "+this.playerNames[2]+" have all tied...";	//players 1, 2, 3 tie
					if(player1Score == player2Score && player1Score == player4Score && player1Score > player3Score)
						return this.playerNames[0]+", "+this.playerNames[1]+", and "+this.playerNames[3]+" have all tied...";	//players 1, 2, 4 tie
					if(player1Score == player3Score && player1Score == player4Score && player1Score > player2Score)
						return this.playerNames[0]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";	//players 1, 3, 4 tie
					if(player2Score == player3Score && player2Score == player4Score && player2Score > player1Score)
						return this.playerNames[1]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";	//players 2, 3, 4 tie

					//case where everyone tied
					return this.playerNames[0]+", "+this.playerNames[1]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";
			}

		}

    	return null;
    }

    /**
     * sends the updated state to the given player. In our case, we need to
     * make a copy of the Deck and null out all cards that the player should
	 * not be able to see. This includes the draw pile (hand[4]) and all
	 * other players hands.
     * 
     * @param player
     * 		the player to which the state is to be sent
     */
	@Override
	protected void sendUpdatedStateTo(GamePlayer player) {
		//if there is no updated state, return
		if (state == null) return;

		//make a copy of the state and null out all card that are irrelevant to the player
		GFState stateForPlayer = new GFState(state); // copy of state
		
		//send the modified copy of the state to the player
		player.sendInfo(stateForPlayer);
	}
	
	/**
	 * Determines whether or not a specific playerIdx is allowed to move or not.
	 * Returns true or false.
	 * 
	 * @param playerIdx
	 * 		the player-number of the player in question
	 */
	protected boolean canMove(int playerIdx) {
		if(playerIdx < 0 || playerIdx > state.getNumPlayers()) {
			//the the playerIdx is out of range (not within our number of players) return false
			return false;
		}else{
			//else, the player can only move if it is their turn
			return state.whoseTurn() == playerIdx;
		}
	}

	/**
	 * makes a move on behalf of a player
	 * 
	 * @param action
	 * 		the action denoting the move to be made
	 * @return
	 * 		true if the move was legal; false otherwise
	 */
	@Override
	protected boolean makeMove(GameAction action) {
		
		//if the action is NOT a Go Fish action, return false
		if (!(action instanceof GFMoveAction)) return false;

		//else, continue and cast the action as a GFMoveAction
		GFMoveAction moveAction = (GFMoveAction) action;
		
		//get the index of the player making the move, and return false if the player
		//is out of the bounds (i.e. not a legal player)
		int playerIdx = getPlayerIdx(moveAction.getPlayer());
		if (playerIdx < 0 || playerIdx > state.getNumPlayers()) return false;

		//if we have a brook request action, check for that first
		if (moveAction.isBrook()) {

			//if we are checking an empty deck, return false. Cannot possibly be a brook, but the
			//player should be dealt another card from the deck provided some are left
			if (state.getHand(playerIdx).size() == 0) {
				dealCardToPlayer(playerIdx);
				return false;
			}else{
				//else, we will attempt to find a brook and remove/score those cards
				state.findBrook(playerIdx);

				//successful action, return true
				return true;
			}

		//else, we have a request for a particular card, so we'll go through all the checks
		}else if (moveAction.isRequest()) {

			//if the player is requesting when it is not their turn, return false
			if (playerIdx != state.whoseTurn()) {
				return false;

			//else if the player is attempting to request from an empty deck
			}else if (state.getHand(playerIdx).size() == 0){
				//set the turn to be the next player in line, and return true
				state.setWhoseTurn( (playerIdx+1)%state.getNumPlayers() );
				return true;

			//else the correct player is playing, and they will request a card from another
			//players hand
			}else{

				//check if the targetPlayer has that card in their hand
				if(!checkTargetDeck(state.getHand(moveAction.getTargetPlayer()), moveAction.getTargetCard())){

					//if they DON'T have that card, change whose turn it currently is to the
					//next person, draw a card from the draw pile (provided there are some left)
					//and sort the current players deck
					state.setWhoseTurn( (playerIdx+1)%state.getNumPlayers() );
					state.getHand(4).moveTopCardTo(state.getHand(playerIdx));
					state.getHand(playerIdx).sort();

					//since the target player DID NOT have the card we were looking for, post to
					//the history object with a success==false
					state.postHistory(playerIdx, moveAction.getTargetPlayer(),
							moveAction.getTargetCard().getRank().value(14), -1, false);

					return true;
				}

				//if the target player DOES have the target card, pull all similarly ranked cards
				//out of that players deck and into the current players deck
				moveTargetCards(playerIdx, moveAction.getTargetPlayer(), moveAction.getTargetCard());
				state.getHand(playerIdx).sort();
				state.getHand(moveAction.getTargetPlayer()).sort();

				//since the target player DID have the cards we were looking for, post to the history
				//object with success==true
				state.postHistory(playerIdx, moveAction.getTargetPlayer(),
						moveAction.getTargetCard().getRank().value(14), -1, true);

				return true;
			}


		//some unexpected action, return false
		}else{
			return false;
		}
	}

	/**
	 * Helper method that gives a card from the dealing hand (provided there are some left)
	 * to the player specified by the parameter
	 *
	 * @param idx
	 * 		index of the player to whom the cards should be given
	 */
	private void dealCardToPlayer(int idx) {
		//if the index of the player is not in the range, return immediately
		if (idx < 0 || idx > state.getNumPlayers()) return;

		//else, if the dealing hand is larger than 0, move the top card of that hand
		//into the hand of the player specified by idx
		if(state.getHand(4).size() > 0) state.getHand(4).moveTopCardTo(state.getHand(idx));
	}

	/**
	 * Helper method to check the targetPlayers deck for a specific card
	 */
	private boolean checkTargetDeck(Deck hand, Card c){
		int i;
		for(i = 0; i < hand.size(); i++){
			if(hand.cards.get(i).getRank().value(14) == c.getRank().value(14)){
				return true;
			}
		}

		return false;
	}

	/**
	 * Helper method that will check the target players deck for the requested cards rank,
	 * and if it is present, move all of that type into the calling players hand
	 *
	 * @param player
	 * 		Requesting player
	 * @param targetPlayer
	 * 		Player being requested from
	 * @param targetCard
	 * 		Card that is being searched for
	 */
	private void moveTargetCards(int player, int targetPlayer, Card targetCard) {
		int i;
		//search through all cards in the target players deck
		for(i = 0; i < state.getHand(targetPlayer).size(); i++){
			//if we found a card that has the same rank as the card we are looking for...
			if(state.getHand(targetPlayer).cards.get(i).getRank().value(14) == targetCard.getRank().value(14)){
				//add that card to the current players hand
				state.getHand(player).add(state.getHand(targetPlayer).cards.get(i));
				//and remove it from the target players deck
				state.getHand(targetPlayer).cards.remove(i);
				i--;
			}
		}
		//sort both decks afterwards to keep them in order
		state.getHand(player).sort();
		state.getHand(targetPlayer).sort();
	}

}
