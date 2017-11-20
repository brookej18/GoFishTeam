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
public class GFComputerPlayer extends GameComputerPlayer
{
	// the minimum reaction time for this player, in milliseconds
	private double minReactionTimeInMillis;

	// the most recent state of the game
	private GFState savedState;

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



		// If it's not our turn to play, play a card.
		if (savedState.whoseTurn() != this.playerNum) {
			// If it's my turn to play a card,
			// delay for up to two seconds; then play
			//sleep((int)(2000* Math.random()));
			// submit our move to the game object
			//game.sendAction(new GFCheckHandAction(this));
		}
		else if(savedState.whoseTurn() == this.playerNum) {
			// If it's my turn to play a card,
			// delay for up to two seconds; then play
			sleep(1000);
			// submit our move to the game object)

			//game.sendAction(new GFCheckHandAction(this));

			//Random request move for computer player
			if(savedState.getHand(this.playerNum).size() > 0) {
				int handSize = savedState.getHand(this.playerNum).size();
				Card requestCard = savedState.getHand(this.playerNum).cards.get(
						(int) (Math.random() * handSize));
				int requestPlayer = this.playerNum;
				while (requestPlayer == this.playerNum) {
					requestPlayer = 0;
				}
				game.sendAction(new GFRequestAction(this, requestPlayer, requestCard));
			}else{
				game.sendAction(new GFRequestAction(this, 0, null));
			}
		}
	}
}
