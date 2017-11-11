package edu.up.cs301.gofish;

import android.util.Log;

import edu.up.cs301.card.Rank;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * The LocalGame class for a slapjack game.  Defines and enforces
 * the game rules; handles interactions between players.
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */

public class GFLocalGame extends LocalGame {

    // the game's state
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
				case 2:
					//individual winners
					if(player1Score > player2Score) return this.playerNames[0]+" is the winner!";
					if(player1Score < player2Score) return this.playerNames[1]+" is the winner!";
					//case where everyone tied
					return this.playerNames[0]+" and "+this.playerNames[1]+" have tied...";

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

				case 4:
					//individual winners
					if(player1Score > player2Score && player1Score > player3Score && player1Score > player4Score) return this.playerNames[0]+" is the winner!";
					if(player2Score > player1Score && player2Score > player3Score && player2Score > player4Score) return this.playerNames[1]+" is the winner!";
					if(player3Score > player1Score && player3Score > player2Score && player3Score > player4Score) return this.playerNames[2]+" is the winner!";
					if(player4Score > player1Score && player4Score > player2Score && player4Score > player3Score) return this.playerNames[3]+" is the winner!";
					//case where two people tied
					if(player1Score == player2Score && player1Score > player3Score && player1Score > player4Score)
						return this.playerNames[0]+" and "+this.playerNames[1]+" have tied...";
					if(player1Score == player3Score && player1Score > player2Score && player1Score > player4Score)
						return this.playerNames[0]+" and "+this.playerNames[2]+" have tied...";
					if(player1Score == player4Score && player1Score > player2Score && player1Score > player3Score)
						return this.playerNames[0]+" and "+this.playerNames[3]+" have tied...";

					if(player2Score == player1Score && player2Score > player3Score && player2Score > player4Score)
						return this.playerNames[1]+" and "+this.playerNames[0]+" have tied...";
					if(player2Score == player3Score && player2Score > player1Score && player2Score > player4Score)
						return this.playerNames[1]+" and "+this.playerNames[2]+" have tied...";
					if(player2Score == player4Score && player2Score > player1Score && player2Score > player3Score)
						return this.playerNames[1]+" and "+this.playerNames[3]+" have tied...";

					if(player3Score == player1Score && player3Score > player2Score && player3Score > player4Score)
						return this.playerNames[2]+" and "+this.playerNames[0]+" have tied...";
					if(player3Score == player2Score && player3Score > player1Score && player3Score > player4Score)
						return this.playerNames[2]+" and "+this.playerNames[1]+" have tied...";
					if(player3Score == player4Score && player3Score > player1Score && player3Score > player2Score)
						return this.playerNames[2]+" and "+this.playerNames[3]+" have tied...";

					//case where three people tied
					if(player1Score == player2Score && player1Score == player3Score && player1Score > player4Score)
						return this.playerNames[0]+", "+this.playerNames[1]+", and "+this.playerNames[2]+" have all tied...";
					if(player1Score == player2Score && player1Score == player4Score && player1Score > player3Score)
						return this.playerNames[0]+", "+this.playerNames[1]+", and "+this.playerNames[3]+" have all tied...";
					if(player1Score == player3Score && player1Score == player4Score && player1Score > player2Score)
						return this.playerNames[0]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";

					if(player2Score == player1Score && player2Score == player3Score && player2Score > player4Score)
						return this.playerNames[1]+", "+this.playerNames[0]+", and "+this.playerNames[2]+" have all tied...";
					if(player2Score == player1Score && player2Score == player4Score && player2Score > player3Score)
						return this.playerNames[1]+", "+this.playerNames[0]+", and "+this.playerNames[3]+" have all tied...";
					if(player2Score == player3Score && player2Score == player4Score && player2Score > player1Score)
						return this.playerNames[1]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";

					if(player3Score == player2Score && player3Score == player1Score && player3Score > player4Score)
						return this.playerNames[0]+", "+this.playerNames[1]+", and "+this.playerNames[2]+" have all tied...";
					if(player3Score == player2Score && player3Score == player4Score && player3Score > player1Score)
						return this.playerNames[1]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";
					if(player3Score == player1Score && player3Score == player4Score && player3Score > player2Score)
						return this.playerNames[0]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";
					//case where everyone tied
					return this.playerNames[0]+", "+this.playerNames[1]+", "+this.playerNames[2]+", and "+this.playerNames[3]+" have all tied...";
			}

		}

    	return null;
    }

    /**
     * sends the updated state to the given player. In our case, we need to
     * make a copy of the Deck, and null out all the cards except the top card
     * in the middle deck, since that's the only one they can "see"
     * 
     * @param p
     * 		the player to which the state is to be sent
     */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// if there is no state to send, ignore
		if (state == null) {
			return;
		}

		// make a copy of the state; null out all cards except for the
		// top card in the middle deck
		GFState stateForPlayer = new GFState(state); // copy of state
		
		// send the modified copy of the state to the player
		p.sendInfo(stateForPlayer);
	}
	
	/**
	 * whether a player is allowed to move
	 * 
	 * @param playerIdx
	 * 		the player-number of the player in question
	 */
	protected boolean canMove(int playerIdx) {
		if (playerIdx < 0 || playerIdx > 1) {
			// if our player-number is out of range, return false
			return false;
		}
		else {
			// player can move if it's their turn, or if the middle deck is non-empty
			// so they can slap
			return state.getHand(2).size() > 0 || state.whoseTurn() == playerIdx;
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
		
		// check that we have slap-jack action; if so cast it
		if (!(action instanceof GFMoveAction)) {
			return false;
		} 
		GFMoveAction GFma = (GFMoveAction) action;
		
		// get the index of the player making the move; return false
		int thisPlayerIdx = getPlayerIdx(GFma.getPlayer());
		
		if (thisPlayerIdx < 0) { // illegal player
			return false;
		}

		if (GFma.isBrook()) {
			// if we have a slap 
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
	}
	
	/**
	 * helper method that gives all the cards in the middle deck to
	 * a given player; also shuffles the target deck
	 * 
	 * @param idx
	 * 		the index of the player to whom the cards should be given
	 */
	private void giveMiddleCardsToPlayer(int idx) {
		// illegal player: ignore
		if (idx < 0 || idx > 1) return;
		
		// move all cards from the middle deck to the target deck
		state.getHand(2).moveAllCardsTo(state.getHand(idx));
		
		// shuffle the target deck
		state.getHand(idx).shuffle();
	}
}
