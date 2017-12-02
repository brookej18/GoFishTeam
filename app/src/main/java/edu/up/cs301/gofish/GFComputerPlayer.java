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
		// If it's not our turn to play, play a card.
		if (savedState.whoseTurn() != this.playerNum) {

		}else if(savedState.whoseTurn() == this.playerNum) {
			// If it's my turn to play a card,
			// delay for up to two seconds; then play
			sleep(2000);
			// submit our move to the game object)


			savedState.findBrook(this.playerNum);

			if(!compDifficulty){

				//Simple computerPlayer implementation, so request random card to a random player

				//if the computers hand is larger than 0 cards
				if (savedState.getHand(this.playerNum).size() > 0) {
					int handSize = savedState.getHand(this.playerNum).size();

					//get a random rank from the computers hand to ask for
					Card requestCard = savedState.getHand(this.playerNum).cards.get(
							(int) (Math.random() * handSize));

					//get this players number, so to make sure not to ask itself for a card
					int requestPlayer = this.playerNum;
					while (requestPlayer == this.playerNum) {
						requestPlayer = (int)(Math.random()*savedState.getNumPlayers());
					}
					game.sendAction(new GFRequestAction(this, requestPlayer, requestCard));

				//else, just request a null card to end the turn.
				} else {
					game.sendAction(new GFRequestAction(this, 0, null));
				}
			}else{

				//smart AI implementation of the computer player
			}
		}
	}
}
