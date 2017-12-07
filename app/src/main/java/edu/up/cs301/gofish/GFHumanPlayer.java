package edu.up.cs301.gofish;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * A GUI that allows a human to play Slapjack. Moves are made by clicking
 * regions on a surface. Presently, it is laid out for landscape orientation.
 * If the device is held in portrait mode, the cards will be very long and
 * skinny.
 *
 * @author Alex Costa
 * @author Jackson Brooke
 * @author Logan Crawford
 * @version November, 2017
 */
public class GFHumanPlayer extends GameHumanPlayer implements Animator {

	// sizes and locations of card decks and cards, expressed as percentages
	// of the screen height and width
	private final static float CARD_HEIGHT_PERCENT = 30; // height of a card
	private final static float CARD_WIDTH_PERCENT = 12; // width of a card
	private final static float LEFT_BORDER_PERCENT = 3; // width of left border
	private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
	private final static float VERTICAL_BORDER_PERCENT = 3; // width of top/bottom borders
	private float cardHighLight = 0;
	private Card card;

	// our game state
	protected GFState state;

	// our activity
	private Activity myActivity;

	// the animation surface
	private AnimationSurface surface;

	// the background color
	private int backgroundColor;

	/**
	 * constructor
	 *
	 * @param name
	 * 		the player's name
	 * @param bkColor
	 * 		the background color
	 */
	public GFHumanPlayer(String name, int bkColor) {
		super(name);
		backgroundColor = bkColor;
	}

	/**
	 * callback method: we have received a message from the game
	 *
	 * @param info
	 * 		the message we have received from the game
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		Log.i("GFComputerPlayer", "receiving updated state ("+info.getClass()+")");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if we had an out-of-turn or illegal move, flash the screen
			surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof GFState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
		else {
			// it's a game-state object: update the state. Since we have an animation
			// going, there is no need to explicitly display anything. That will happen
			// at the next animation-tick, which should occur within 1/20 of a second
			this.state = (GFState)info;
			Log.i("human player", "receiving");
		}
	}

	/**
	 * call-back method: called whenever the GUI has changed (e.g., at the beginning
	 * of the game, or when the screen orientation changes).
	 *
	 * @param activity
	 * 		the current activity
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		myActivity = activity;

		// Load the layout resource for the new configuration
		activity.setContentView(R.layout.gf_human_player);

		// link the animator (this object) to the animation surface
		surface = (AnimationSurface) myActivity
				.findViewById(R.id.animation_surface);
		surface.setAnimator(this);

		// read in the card images
		Card.initImages(activity);

		// if the state is not null, simulate having just received the state so that
		// any state-related processing is done
		if (state != null) {
			receiveInfo(state);
		}
	}

	/**
	 * @return the top GUI view
	 */
	@Override
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	/**
	 * @return
	 * 		the animation interval, in milliseconds
	 */
	public int interval() {
		// 1/20 of a second
		return 50;
	}

	/**
	 * @return
	 * 		the background color
	 */
	public int backgroundColor() {
		return backgroundColor;
	}

	/**
	 * @return
	 * 		whether the animation should be paused
	 */
	public boolean doPause() {
		return false;
	}

	/**
	 * @return
	 * 		whether the animation should be terminated
	 */
	public boolean doQuit() {
		return false;
	}

	/**
	 * callback-method: we have gotten an animation "tick"; redraw the screen image:
	 * - the middle deck, with the top card face-up, others face-down
	 * - the two players' decks, with all cards face-down
	 * - a red bar to indicate whose turn it is
	 *
	 * @param g
	 * 		the canvas on which we are to draw
	 */
	public void tick(Canvas g) {

		// ignore if we have not yet received the game state
		if (state == null) return;

		// get the height and width of the animation surface
		int height = surface.getHeight();
		int width = surface.getWidth();

		// draw the Local DrawPile
		Deck drawPile = state.getHand(4);
		if (drawPile != null) {
			// if drawPile is not empty, draw a set of N card-backs,
			// so that the user can see the size of
			// the pile
			RectF drawPileLocation = drawPileTopCardLocation();
			drawCardBacks(g, drawPileLocation,
					0, 0, state.getHand(4).size());
		}

		//draw and update a string denoting whose turn it is to play
		Paint paintString = new Paint();
		paintString.setColor(Color.BLACK);
		paintString.setTextSize(50);
		int player = state.whoseTurn();
		String[] players = allPlayerNames;
		if(player == 0){
			g.drawText("Player's Turn: " + players[0], 0, 50, paintString);
		}else if(player == 1){
			g.drawText("Player's Turn: " + players[1], 0, 50, paintString);
		}else if(player == 2){
			g.drawText("Player's Turn: " + players[2], 0, 50, paintString);
		}else if(player == 3){
			g.drawText("Player's Turn: " + players[3], 0, 50, paintString);
		}else{
			Log.i( "playerTurnStringDisplay","There are no current players");
		}

		int playerSouth = this.playerNum;
		int playerNorth;
		int playerEast;
		int playerWest;

		if(state.getNumPlayers() == 2)
		{
			playerNorth = 1-this.playerNum;

			//draw and update the Strings denoting the score of each player
			paintString.setTextSize(40);
			g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
			g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);

			// draw opponent North's cards, face down
			Deck playerNorthHand  = state.getHand(playerNorth);
			RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
			if(playerNorthHand != null)
			{
				//if the player's Hand located int the upperCenter of the screen, North, draw a set of N card-backs,
				//so the user's can see the size of the pile.
				drawCardBacks(g, oppTopLocation,
						0.0065f*width, 0, state.getHand(playerNorth).size());
			}

			// draw my cards, player South, face up
			Deck playerSouthHand = state.getHand(this.playerNum);
			RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
			if(playerSouthHand != null)
			{
				//If our Hand is not empty, draw our cards so we can see them along side each other.
				drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
			}
		}
		else if(state.getNumPlayers() == 3)
		{
			if(playerSouth == 0)
			{
				playerWest = 1;
				playerNorth = 2;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}
			}
			else if(playerSouth == 1)
			{
				playerWest = 2;
				playerNorth = 0;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}
			}
			else if(playerSouth == 2)
			{
				playerWest = 0;
				playerNorth = 1;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}
			}
		}
		else if(state.getNumPlayers() == 4)
		{
			if(playerSouth == 0)
			{
				playerWest = 1;
				playerNorth = 2;
				playerEast = 3;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);
				g.save();
				g.rotate(270f, 1820, 730);
				g.drawText(players[playerEast]+"'s Score: "+state.getScore(playerEast), 1820, 730, paintString);
				g.restore();

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}

				//draw opponent East's cards, face down
				Deck playerEastHand = state.getHand(playerEast);
				RectF oppEastLocation = opponentEastTopCardLocation(); //drawing size/location
				if(playerEastHand != null)
				{
					//if the player's Hand located in the right of the screen, East, is not empty,
					//draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppEastLocation, 0, .01f*height, state.getHand(playerEast).size());
				}
			}
			else if(playerSouth == 1)
			{
				playerWest = 2;
				playerNorth = 3;
				playerEast = 0;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);
				g.save();
				g.rotate(270f, 1820, 730);
				g.drawText(players[playerEast]+"'s Score: "+state.getScore(playerEast), 1820, 730, paintString);
				g.restore();

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}

				//draw opponent East's cards, face down
				Deck playerEastHand = state.getHand(playerEast);
				RectF oppEastLocation = opponentEastTopCardLocation(); //drawing size/location
				if(playerEastHand != null)
				{
					//if the player's Hand located in the right of the screen, East, is not empty,
					//draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppEastLocation, 0, .01f*height, state.getHand(playerEast).size());
				}
			}
			else if(playerSouth == 2)
			{
				playerWest = 3;
				playerNorth = 0;
				playerEast = 1;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);
				g.save();
				g.rotate(270f, 1820, 730);
				g.drawText(players[playerEast]+"'s Score: "+state.getScore(playerEast), 1820, 730, paintString);
				g.restore();

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}

				//draw opponent East's cards, face down
				Deck playerEastHand = state.getHand(playerEast);
				RectF oppEastLocation = opponentEastTopCardLocation(); //drawing size/location
				if(playerEastHand != null)
				{
					//if the player's Hand located in the right of the screen, East, is not empty,
					//draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppEastLocation, 0, .01f*height, state.getHand(playerEast).size());
				}
			}
			else if(playerSouth == 3)
			{
				playerWest = 0;
				playerNorth = 1;
				playerEast = 2;

				//draw and update the Strings denoting the score of each player
				paintString.setTextSize(40);
				g.drawText(players[playerSouth]+"'s Score: "+state.getScore(playerSouth), 900, 810, paintString);
				g.save();
				g.rotate(90f, 220, 330);
				g.drawText(players[playerWest]+"'s Score: "+state.getScore(playerWest), 220, 330, paintString);
				g.restore();
				g.drawText(players[playerNorth]+"'s Score: "+state.getScore(playerNorth), 900, 290, paintString);
				g.save();
				g.rotate(270f, 1820, 730);
				g.drawText(players[playerEast]+"'s Score: "+state.getScore(playerEast), 1820, 730, paintString);
				g.restore();

				//draw my cards, player South, face up
				Deck playerSouthHand = state.getHand(this.playerNum);
				RectF thisCardLocation = thisPlayerFirstCardLocation(); // drawing size/location
				if(playerSouthHand != null)
				{
					//If our Hand is not empty, draw our cards so we can see them along side each other.
					drawOurHand(g, thisCardLocation, 0.06f*width, 0, state.getHand(this.playerNum).size());
				}

				//draw opponent West's cards, face down
				Deck playerWestHand = state.getHand(playerWest);
				RectF oppWestLocation = opponentWestTopCardLocation(); //drawing size/location
				if(playerWestHand != null)
				{
					//if the player's Hand located in the left of the screen, West, is not empty, draw
					//a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppWestLocation, 0, .01f*height, state.getHand(playerWest).size());
				}

				// draw opponent North's cards, face down
				Deck playerNorthHand  = state.getHand(playerNorth);
				RectF oppTopLocation = opponentNorthCardLocation(); // drawing size/location
				if(playerNorthHand != null)
				{
					//if the player's Hand located int the upperCenter of the screen, North, is not empty
					// draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppTopLocation,
							0.0065f*width, 0, state.getHand(playerNorth).size());
				}

				//draw opponent East's cards, face down
				Deck playerEastHand = state.getHand(playerEast);
				RectF oppEastLocation = opponentEastTopCardLocation(); //drawing size/location
				if(playerEastHand != null)
				{
					//if the player's Hand located in the right of the screen, East, is not empty,
					//draw a set of N card-backs, so the user's can see the size of the pile.
					drawCardBacks(g, oppEastLocation, 0, .01f*height, state.getHand(playerEast).size());
				}
			}
		}

		//draw the previous messages on the board
		paintString.setTextSize(25);
		ArrayList<GFHistory> hist = state.history;
		if(hist.size() >= 3){
			g.drawText(histToString(hist.get(hist.size()-1)), 10, 85, paintString);
			g.drawText(histToString(hist.get(hist.size()-2)), 10, 115, paintString);
			g.drawText(histToString(hist.get(hist.size()-3)), 10, 145, paintString);
		}else if(hist.size() >= 2){
			g.drawText(histToString(hist.get(hist.size()-1)), 10, 85, paintString);
			g.drawText(histToString(hist.get(hist.size()-2)), 10, 115, paintString);
		}else if(hist.size() >= 1){
			g.drawText(histToString(hist.get(hist.size()-1)), 10, 85, paintString);
		}

		//draw the text the denotes how many cards are left in the pile
		paintString.setTextSize(50);
		if(state.getHand(4).size() > 0) paintString.setColor(Color.WHITE);
		g.drawText("Deck", 1000, 500, paintString);
		g.drawText(state.getHand(4).size()+" cards", 1000, 575, paintString);

		//draw the rectangle for the User to touch when wanting to check Hand
		Paint paintButton = new Paint();
		paintButton.setColor(Color.DKGRAY);
		g.drawRect(checkHandRect(), paintButton);
		//draw Text on top of the Gray rect, identifying the Check Hand 'Button'
		Paint paintText = new Paint();
		paintText.setColor(Color.CYAN);
		paintText.setTextSize(30);
		g.drawText("Add to score/",1350, 530, paintText);
		g.drawText("end turn", 1385, 570, paintText);

		Paint pHighLight = new Paint();
		pHighLight.setColor(Color.RED);
		if(cardHighLight != 0){
			if(state.getHand(this.playerNum).size() < 15){
				g.drawRect(cardHighLight, height-15, cardHighLight + 125, height, pHighLight);
			}else{
				g.drawRect(cardHighLight, height-15, cardHighLight + 62, height, pHighLight);
			}
		}
	}


	/**
	 *
	 * @return
	 * 		The rect that is called to represent the 'Button' location
	 * 		for the user to select when Checking their Hand.
	 */
	private RectF checkHandRect()
	{
		float rectLeft = 1340;
		float rectRight = 1550;
		float rectTop = 480;
		float rectBottom = 590;
		return new RectF(rectLeft, rectTop,rectRight, rectBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the opponent's deck is to
	 * 		be drawn
	 */

	private RectF opponentNorthCardLocation() {
		// near the top-middle of the drawing surface, based on the height
		// and width, and the percentages defined above
		int height = surface.getHeight();
		int width = surface.getWidth();
		float rectLeft = (100-CARD_WIDTH_PERCENT-RIGHT_BORDER_PERCENT)*width/150;
		float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
		float rectTop = (20-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
		float rectBottom = (22-VERTICAL_BORDER_PERCENT)*height/100f;
		return new RectF(rectLeft, rectTop, rectRight, rectBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the
	 * 		drawing surface where the top card in the West
	 * 		player's deck is to be drawn
	 */
	private RectF opponentWestTopCardLocation()
	{
		//centered along the left edge of the drawing surface, based on the height
		//and width, and the percentages defined above.
		int height = surface.getHeight();
		int width = surface.getWidth();
		float rectLeft = (23-CARD_WIDTH_PERCENT-RIGHT_BORDER_PERCENT)*width/100;
		float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
		float rectTop = (60-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
		float rectBottom = (60-VERTICAL_BORDER_PERCENT)*height/100f;
		float rRight = rectLeft - (rectTop - rectBottom);
		float rBottom = rectTop - (rectLeft - rectRight);
		return new RectF(rectLeft, rectTop, rRight, rBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the
	 * 		drawing surface where the top card in the East
	 * 		player's deck is to be drawn
	 */
	private RectF opponentEastTopCardLocation()
	{
		//centered alon the right edge of hte drawing surface, based on the height
		//and width, and the percentages defined above.
		int height = surface.getHeight();
		int width = surface.getWidth();
		float rectLeft = (122-CARD_WIDTH_PERCENT-RIGHT_BORDER_PERCENT)*width/100;
		float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
		float rectTop = (65-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
		float rectBottom = (60-VERTICAL_BORDER_PERCENT)*height/100f;
		float rRight = rectLeft - (rectTop - rectBottom);
		float rBottom = rectTop - (rectLeft - rectRight);
		return new RectF(rectLeft, rectTop, rRight, rBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the current player's deck is to
	 * 		be drawn
	 */
	private RectF thisPlayerFirstCardLocation() {
		// near the right-bottom of the drawing surface, based on the height
		// and width, and the percentages defined above
		int width = surface.getWidth();
		int height = surface.getHeight();
		float rectLeft = (40-RIGHT_BORDER_PERCENT-CARD_WIDTH_PERCENT)*width/100;
		float rectRight = (rectLeft + width*CARD_WIDTH_PERCENT/100);
		float rectTop = (102-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
		float rectBottom = (102-VERTICAL_BORDER_PERCENT)*height/100f;
		return new RectF(rectLeft, rectTop, rectRight, rectBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the draw pile is to
	 * 		be drawn.
	 */
	private RectF drawPileTopCardLocation() {
		// near the middle of the drawing surface, based on the height
		// and width, and the percentages defined below
		int height = surface.getHeight();
		int width = surface.getWidth();
		float rectLeft = (125-CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
		float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
		float rectTop = (60-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
		float rectBottom = (60-VERTICAL_BORDER_PERCENT)*height/100f;
		return new RectF(rectLeft, rectTop, rectRight, rectBottom);
	}

	/**
	 * draws a sequence of card-backs, each offset a bit from the previous one, so that all can be
	 * seen to some extent
	 *
	 * @param g
	 * 		the canvas to draw on
	 * @param topRect
	 * 		the rectangle that defines the location of the top card (and the size of all
	 * 		the cards
	 * @param deltaX
	 * 		the horizontal change between the drawing position of two consecutive cards
	 * @param deltaY
	 * 		the vertical change between the drawing position of two consecutive cards
	 * @param numCards
	 * 		the number of card-backs to draw
	 */
	private void drawCardBacks(Canvas g, RectF topRect, float deltaX, float deltaY,
							   int numCards) {
		// loop through from back to front, drawing a card-back in each location
		for (int i = numCards-1; i >= 0; i--) {
			// determine the position of this card's top/left corner
			float left = topRect.left + i*deltaX;
			float top = topRect.top + i*deltaY;
			// draw a card-back (hence null) into the appropriate rectangle
			drawCard(g,
					new RectF(left, top, left + topRect.width(), top + topRect.height()),
					null);
		}
	}

	/**
	 * Draws our(the local player's) Hand, each card slightly spaced to the right of the other.
	 *
	 * @param g
	 * 		the canvas to draw on
	 * @param thisRect
	 * 		The rectangle that defines the location of the first card of the hand.
	 * 		(and the size of all the cards)
	 * @param deltaX
	 * 		The horizontal change between the drawing position of two consecutive cards
	 * @param deltaY
	 * 		The vertical change between the drawing position and the two consecutive cards
	 * @param numCards
	 * 		The number of cards in our hand
	 */
	private void drawOurHand(Canvas g, RectF thisRect, float deltaX, float deltaY, int numCards) {

		//loop through from front to back, drawing a card in each location
		for(int i = 0; i < numCards; i++){
			//determine the position of this card's topLeft corner
			float left, top;
			if(state.getHand(this.playerNum).size() < 15) {
				left = thisRect.left + i * deltaX;
			}else{
				left = thisRect.left + i * deltaX/2;
			}
			top = thisRect.top + i * deltaY;
			//draw the correct card, into the appropriate rectangle
			synchronized (state.getHand(this.playerNum)) {
				try{
					drawCard(g, new RectF(left, top, left + thisRect.width(),
							top + thisRect.height()), state.getHand(this.playerNum).cards.get(i));
				}catch(ArrayIndexOutOfBoundsException AE){

				} catch (IndexOutOfBoundsException IE) {

				}
			}
		}
	}


	/**
	 * callback method: we have received a touch on the animation surface
	 *
	 * @param event
	 * 		the motion-event
	 */
	public void onTouch(MotionEvent event) {
		if(state == null) return;
		// ignore everything except down-touch events
		if(event.getAction() != MotionEvent.ACTION_DOWN) return;

		// get the location of the touch on the surface
		int x = (int) event.getX();
		int y = (int) event.getY();

		//determine whether the touch occurred the player's deck or the check hand button
		RectF checkHandLoc = checkHandRect();

		float left = thisPlayerFirstCardLocation().left;
		float top = thisPlayerFirstCardLocation().top;
		float right;
		if(state.getHand(this.playerNum).size() < 15) {
			right = thisPlayerFirstCardLocation().width() / 2 * (state.getHand(this.playerNum).size() + 2);
		}else{
			right = thisPlayerFirstCardLocation().width() / 4 * (state.getHand(this.playerNum).size() + 6);
		}
		float bottom = thisPlayerFirstCardLocation().bottom;

		//draw a rect that encompasses the size of the entire human player's hand
		RectF myHandLoc = new RectF(left, top, right, bottom);

		if (myHandLoc.contains(x, y)) {
			//it's on the human players hand, get which card it is by sub-dividing the width by
			//the number of cards in the players hand
			if(state.getHand(this.playerNum).size() != 0) {

				//since we are printing every half-card, we can sub-divide the width of the
				//RectF object into hand.size()+1 halves, or for more than 15 cards, hand.size()+3 quarters
				float widthHalfCard;
				if(state.getHand(this.playerNum).size() < 15){
					widthHalfCard = (right - left)/(state.getHand(this.playerNum).size()+1);
				}else{
					widthHalfCard = (right - left)/(state.getHand(this.playerNum).size()+3);
				}
				//shift the x down to index at zero for simplicity
				float shiftedX = x - left;

				//for all subdivision of the width, check if shiftedX falls into one
				int i;
				for(i = 0; i<state.getHand(this.playerNum).size()+1; i++){
					//to prevent indexOutOfBounds exception, if the index i is getting past the
					//number of cards in the hand, set it to hand.size() - 1 and break the forLoop
					if(i == state.getHand(this.playerNum).size()){
						i--;
						break;
					}

					//if the card is within a sub-division, we have found our card and we can break
					if(widthHalfCard*i < shiftedX && shiftedX < widthHalfCard*(i+1)){
						if(state.getHand(this.playerNum).size() < 15){
							cardHighLight = (float)(left + widthHalfCard*i + 4.8*i);
						}else{
							cardHighLight = left + widthHalfCard*i;
						}
						break;
					//else, we probably touched the last card, so highlight the last card
					}else{
						if(state.getHand(this.playerNum).size() < 15){
							cardHighLight = (float)(left + widthHalfCard*(state.getHand(this.playerNum).size()-1) + 4.8*i);
						}else{
							cardHighLight = left + widthHalfCard*(state.getHand(this.playerNum).size()-1);
						}
					}
				}

				card = state.getHand(this.playerNum).cards.get(i);

			}
		//else the check hand button was pressed, so check our hand for brooks
		}else if(checkHandLoc.contains(x, y)) {
			if (state.getHand(this.playerNum).size() > 0) {
				//The touch is on the Check Hand 'Button', so we will check our hand
				card = null;
				cardHighLight = -1000;
				game.sendAction(new GFCheckHandAction(this));
			} else {
				//if we have no cards left, request a null card
				game.sendAction(new GFRequestAction(this, 0, null));
			}

			////////////////////////////////////////////////////////////////////////////////////////////
			//else, we are trying to ask for a card from someone in the playing field
			////////////////////////////////////////////////////////////////////////////////////////////
		}else if(opponentWestTopCardLocation().contains(x,y) || opponentEastTopCardLocation().contains(x,y) ||
				opponentNorthCardLocation().contains(x,y)){
			if(card != null) {
				int location = 0;
				if (opponentWestTopCardLocation().contains(x, y)){
					location = 1;
				}else if (opponentEastTopCardLocation().contains(x,y)){
					location = 3;
				}else if (state.getNumPlayers() == 2){
					if (opponentNorthCardLocation().contains(x,y)) location = 1;
				}else{
					if (opponentNorthCardLocation().contains(x,y)) location = 2;
				}

				int playerAsking = (this.playerNum + location)%state.getNumPlayers();

				game.sendAction(new GFRequestAction(this, playerAsking, card));

				card = null;
				cardHighLight = -1000;
			}else{
				surface.flash(Color.RED, 50);
			}
		}else{
			// illegal touch-location: flash for 1/20 second
			surface.flash(Color.RED, 50);
		}
	}

	/**
	 * draws a card on the canvas; if the card is null, draw a card-back
	 *
	 * @param g
	 * 		the canvas object
	 * @param rect
	 * 		a rectangle defining the location to draw the card
	 * @param c
	 * 		the card to draw; if null, a card-back is drawn
	 */
	private static void drawCard(Canvas g, RectF rect, Card c) {
		if (c == null) {
			// null: draw a card-back, consisting of a blue card
			// with a white line near the border. We implement this
			// by drawing 3 concentric rectangles:
			// - blue, full-size
			// - white, slightly smaller
			// - blue, even slightly smaller
			Paint white = new Paint();
			white.setColor(Color.WHITE);
			Paint blue = new Paint();
			blue.setColor(Color.BLUE);
			RectF inner1 = scaledBy(rect, 0.96f); // scaled by 96%
			RectF inner2 = scaledBy(rect, 0.98f); // scaled by 98%
			g.drawRect(rect, blue); // outer rectangle: blue
			g.drawRect(inner2, white); // middle rectangle: white
			g.drawRect(inner1, blue); // inner rectangle: blue
		}
		else {
			// just draw the card
			c.drawOn(g, rect);
		}
	}

	/**
	 * scales a rectangle, moving all edges with respect to its center
	 *
	 * @param rect
	 * 		the original rectangle
	 * @param factor
	 * 		the scaling factor
	 * @return
	 * 		the scaled rectangle
	 */
	private static RectF scaledBy(RectF rect, float factor) {
		// compute the edge locations of the original rectangle, but with
		// the middle of the rectangle moved to the origin
		float midX = (rect.left+rect.right)/2;
		float midY = (rect.top+rect.bottom)/2;
		float left = rect.left-midX;
		float right = rect.right-midX;
		float top = rect.top-midY;
		float bottom = rect.bottom-midY;

		// scale each side; move back so that center is in original location
		left = left*factor + midX;
		right = right*factor + midX;
		top = top*factor + midY;
		bottom = bottom*factor + midY;

		// create/return the new rectangle
		return new RectF(left, top, right, bottom);
	}

	/**
	 * Method that will take a GFHistory object and convert that object to a human readable string.
	 * The GFHistory object is coded as
	 *
	 *
	 * @param hist
	 * @return
	 */
	public String histToString(GFHistory hist){
		String playerNames[] = allPlayerNames;

		//if the player in the history object is not in the range of players, return empty string
		// This should never happen, but will show up if errors occur during debugging
		if(hist.getCurrentPlayer() < 0 || hist.getCurrentPlayer() > state.getNumPlayers()) return "";

		//set the rank as a string variable (mainly for Jack, Queen, King, Ace)
		String rank;
		switch (hist.getRankTake()){
			case 11:
				rank = "Jack";
				break;
			case 12:
				rank = "Queen";
				break;
			case 13:
				rank = "King";
				break;
			case 14:
				rank = "Ace";
				break;
			default:
				rank = ""+hist.getRankTake();
		}

		//if the player asked for a card from another player, and SUCCESSFULLY took that card
		if(hist.getPlayerAsk() != -1 && hist.getRankTake() != -1 && hist.getSuccess() == true) {
			return playerNames[hist.getCurrentPlayer()] + " TOOK the " + rank + " cards from " +
					playerNames[hist.getPlayerAsk()] + ".";
			//Printed in the form: "Player1 took the X cards from Player2."

			//if the player asked for a card, but DID NOT get that card
		}else if(hist.getPlayerAsk() != -1 && hist.getRankTake() != 1){
			return "GO FISH: "+playerNames[hist.getCurrentPlayer()]+" asked "+playerNames[hist.getPlayerAsk()]+" for the "+
					rank+"...";
			//Printed in the form: "Player1 asked Player2 for the X..."

			//if the player added to their score
		}else if(hist.getScoreAdd() != -1){
			return playerNames[hist.getCurrentPlayer()]+" just added "+hist.getScoreAdd()+" to their score!";
			//Printed in the form: "Player1 just added X to their score!"
		}

		return "";
	}
}
