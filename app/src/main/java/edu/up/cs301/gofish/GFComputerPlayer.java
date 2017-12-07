package edu.up.cs301.gofish;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * This is a computer player that plays at an average rate given
 * by the constructor parameter.
 *
 * @author Alex Costa
 * @author Jackson Brooke
 * @author Logan Crawford
 * @version November, 2017
 */
public class GFComputerPlayer extends GameComputerPlayer {

	// the most recent state of the game
	private GFState savedState;

	private boolean compDifficulty;

	/**
	 * Constructor for the GFComputerPlayer class; creates an "average"
	 * player.
	 *
	 * @param name
	 * 		the player's name
	 */
	public GFComputerPlayer(String name) {
		// invoke general constructor to create player whose average reaction
		// time is half a second.
		this(name, false);
	}

	/*
     * Constructor for the GFComputerPlayer class
     */
	public GFComputerPlayer(String name, boolean difficulty) {
		// invoke superclass constructor
		super(name);
		compDifficulty = difficulty;

	}

	/**
	 * Invoked whenever the player's timer has ticked. It is expected
	 * that this will be overridden in many players.
	 */
	@Override
	protected void timerTicked() {
		//The game itself will handle the transaction of cards. The player will not,
		//that way they don't have the opportunity to decline any requests.

		// stop the timer, since we don't want another timer-tick until it
		// again is explicitly started
		getTimer().stop();
	}

	/**
	 * callback method, called when we receive a message, typically from
	 * the game
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		// if we don't have a game-state, ignore
		if (!(info instanceof GFState)) return;

		// update our state variable
		savedState = (GFState)info;
		game.sendAction(new GFCheckHandAction(this));
		int playerNum = this.playerNum;


		if(savedState.whoseTurn() == playerNum) {
			// If it's my turn to play a card,
			// delay for up to two seconds; then play
			sleep(3000);
			// submit our move to the game object)

			savedState.findBrook(playerNum);

			if(!compDifficulty){

				//Simple computerPlayer implementation, so request random card to a random player

				//if the computers hand is larger than 0 cards
				if (savedState.getHand(playerNum).size() > 0) {


					//get a random rank from the computers hand to ask for
					Card requestCard = savedState.getHand(playerNum).cards.get(
							(int) (Math.random() * (savedState.getHand(playerNum).size()-1)));

					//get this players number, so to make sure not to ask itself for a card
					int requestPlayer = playerNum;
					while (requestPlayer == playerNum) {
						requestPlayer = (int)(Math.random()*savedState.getNumPlayers());
						//if the player we're asking doesn't have any cards, try again
						if(savedState.getHand(requestPlayer).size() == 0) requestPlayer = playerNum;
					}
					game.sendAction(new GFRequestAction(this, requestPlayer, requestCard));

				//else, just request a null card to end the turn.
				} else {
					game.sendAction(new GFRequestAction(this, 0, null));
				}
			}else{

				//smart AI implementation of the computer player

				//as long as the players hand is not empty
				if(savedState.getHand(playerNum).size() > 0){

					//index variables
					int i = savedState.history.size()-1, j;

					//while we start at the back of the history ArrayList and make our way to the front
					while(i > -1){

						//if a history object has yet to be used by an AI and this player didn't ask for it
						if(savedState.history.get(i).getUsedByAI() == false  &&
								savedState.history.get(i).getCurrentPlayer() != playerNum){

							//get the rank of the card requested during that move
							int rank = savedState.history.get(i).getRankTake();

							//for all cards in this players hand, check if we have that rank of card
							for(j = 0; j < savedState.getHand(playerNum).size(); j++){

								//if we found a card in our hand with that rank, request it from that player
								if(rank == savedState.getHand(playerNum).cards.get(j).getRank().value(14)){
									//update the history object to reflect that this was used by an AI
									savedState.history.get(i).setUsedByAI(true);

									game.sendAction(new GFRequestAction(this,
											savedState.history.get(i).getCurrentPlayer(),
											savedState.getHand(playerNum).cards.get(j)));
									return;
								}
							}
						}
						i--;
					}
					//if we got all the way through the history object, revert to random actions.

					//get a random rank from the computers hand to ask for

					Card requestCard = savedState.getHand(playerNum).cards.get(
							(int) (Math.random()*(savedState.getHand(playerNum).size() - 1)));

					//get this players number, so to make sure not to ask itself for a card
					int requestPlayer = playerNum;
					while (requestPlayer == playerNum) {
						requestPlayer = (int) (Math.random() * savedState.getNumPlayers());
						//if the player we're asking doesn't have any cards, try again
						if(savedState.getHand(requestPlayer).size() == 0) requestPlayer = playerNum;
					}
					game.sendAction(new GFRequestAction(this, requestPlayer, requestCard));

				//else, just request a null card to end the turn.
				}else{
					game.sendAction(new GFRequestAction(this, 0, null));
				}

			}
		}
	}
}
