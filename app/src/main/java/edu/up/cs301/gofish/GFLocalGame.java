package edu.up.cs301.gofish;

import android.util.Log;

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
    public GFLocalGame() {
        Log.i("GFLocalGame", "creating game");
        // create the state for the beginning of the game
        state = new GFState();
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
		stateForPlayer.nullCards(player.getPlayerNum());
		
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
		GFMoveAction GFma = (GFMoveAction) action;
		
		//get the index of the player making the move, and return false if the player
		//is out of the bounds
		int thisPlayerIdx = getPlayerIdx(GFma.getPlayer());
		if (thisPlayerIdx < 0 || thisPlayerIdx > state.getNumPlayers()) return false;

		if (GFma.isBrook()) {
			//if we have a brook

			/************GAME RULES NEED TO BE IMPLEMENTED PASSED THIS POINT**************/

			if (state.getHand(2).size() == 0) {
				// empty deck: return false, as move is illegal
				return false;
			}
			else {
				// a non-Jack was slapped: give all cards to non-slapping player
				giveMiddleCardsToPlayer(1-thisPlayerIdx);
			}
		}
		else if (GFma.isRequest()) { // we have a "play" action
			if (thisPlayerIdx != state.whoseTurn()) {
				// attempt to play when it's the other player's turn
				return false;
			}
			else {
				// it's the correct player's turn: move the top card from the
				// player's deck to the top of the middle deck
				state.getHand(thisPlayerIdx).moveTopCardTo(state.getHand(2));
				// if the opponent has any cards, make it the opponent's move
				if (state.getHand(1-thisPlayerIdx).size() > 0) {
					state.setWhoseTurn(1-thisPlayerIdx);
				}
			}
		}
		else { // some unexpected action
			return false;
		}

		// return true, because the move was successful if we get her
		return true;

		/************END GAME RULES NEED TO BE IMPLEMENTED PASSED THIS POINT**************/
	}
	
	/**
	 * helper method that gives the top card of the dealing deck to the
	 * player that is specified by the parameter
	 * 
	 * @param idx
	 * 		the index of the player to whom the cards should be given
	 */
	private void giveMiddleCardsToPlayer(int idx) {
		//if the index of the player is not in the range, return immediately
		if (idx < 0 || idx > state.getNumPlayers()) return;

		//else, if the dealing hand is larger than 0, move the top card of that hand
		//into the hand of the player specified by idx
		if(state.getHand(4).size() > 0) state.getHand(4).moveTopCardTo(state.getHand(idx));
	}
}
