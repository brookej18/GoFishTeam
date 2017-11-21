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

//Edit for the GUI as it's developing

		// draw the Local DrawPile
		Deck drawPile = state.getHand(4);
		if (drawPile != null) {
			// if drawPile is not empty, draw a set of N card-backs,
			// so that the user can see the size of
			// the pile
			RectF midTopLocation = middlePileTopCardLocation();
			drawCardBacks(g, midTopLocation,
					0, 0, state.getHand(4).size());
		}

		// draw the opponent's cards, face down
		Deck playerTwoHand  = state.getHand(1);
		RectF oppTopLocation = opponentTopCardLocation(); // drawing size/location
		if(playerTwoHand != null)
		{
			//if player Two's Hand is not empty, draw a set of N card-backs,
			//so the user's can see the size of the pile.
			drawCardBacks(g, oppTopLocation,
					0.01f*width, 0, state.getHand(1-this.playerNum).size());
		}

		// draw my cards, face up
		Deck playerOneHand = state.getHand(0);
		RectF thisTopLocation = thisPlayerTopCardLocation(); // drawing size/location
		if(playerOneHand != null)
		{
			//If our Hand is not empty, draw our cards so we can see them along side each other.
			drawOurHand(g, thisTopLocation, 0.05f*width, 0, state.getHand(0).size());
		}

		//draw and update a string denoting whose turn it is to play
		Paint paintString = new Paint();
		paintString.setColor(Color.BLACK);
		paintString.setTextSize(50);
		int player = state.whoseTurn();
		String[] players = allPlayerNames;
		if(player == 0)
		{
			g.drawText("Player's Turn: " + players[0], 0, 50, paintString);
		}
		else if(player == 1)
		{
			g.drawText("Player's Turn: " + players[1], 0, 50, paintString);
		}
		else if(player == 2)
		{
			g.drawText("Player's Turn: " + players[2], 0, 50, paintString);
		}
		else if(player == 3)
		{
			g.drawText("Player's Turn: " + players[3], 0, 50, paintString);
		}
		else
		{
			Log.i( "playerTurnStringDisplay","There are no current players");
		}

		//draw and update the Strings denoting the score of each player
		paintString.setTextSize(40);
		g.drawText(players[0]+"'s Score: "+state.getScore(0), 400, 810, paintString);
		g.drawText(players[1]+"'s Score: "+state.getScore(1), 1600, 50, paintString);

        //draw the previous messages on the board
        paintString.setTextSize(25);
        ArrayList<GFHistory> hist = state.history;
        if(hist.size() >= 3){
            g.drawText(histToString(hist.get(hist.size()-1)), 0, 85, paintString);
            g.drawText(histToString(hist.get(hist.size()-2)), 0, 115, paintString);
            g.drawText(histToString(hist.get(hist.size()-3)), 0, 145, paintString);
        }else if(hist.size() >= 2){
            g.drawText(histToString(hist.get(hist.size()-1)), 0, 85, paintString);
            g.drawText(histToString(hist.get(hist.size()-2)), 0, 115, paintString);
        }else if(hist.size() >= 1){
            g.drawText(histToString(hist.get(hist.size()-1)), 0, 85, paintString);
        }

		//draw the rectangle for the User to touch when wanting to check Hand
		Paint paintButton = new Paint();
		paintButton.setColor(Color.DKGRAY);
		g.drawRect(checkHandRect(), paintButton);
		//draw Text on top of the Gray rect, identifying the Check Hand 'Button'
		Paint paintText = new Paint();
		paintText.setColor(Color.CYAN);
		paintText.setTextSize(35);
		g.drawText("Check Hand",150, 750, paintText);
	}


	/**
	 *
	 * @return
	 * 		The rect that is called to represent the 'Button' location
	 * 		for the user to select when Checking their Hand.
	 */
	private RectF checkHandRect()
	{
		float rectLeft = 140;
		float rectRight = 350;
		float rectTop = 680;
		float rectBottom = 790;
		return new RectF(rectLeft, rectTop,rectRight, rectBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the opponent's deck is to
	 * 		be drawn
	 */

	private RectF opponentTopCardLocation() {
		// near the left-bottom of the drawing surface, based on the height
		// and width, and the percentages defined above
		int height = surface.getHeight();
		int width = surface.getWidth();
		float rectLeft = (100-CARD_WIDTH_PERCENT-RIGHT_BORDER_PERCENT)*width/150;
		float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
		float rectTop = (35-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
		float rectBottom = (35-VERTICAL_BORDER_PERCENT)*height/100f;
		return new RectF(rectLeft, rectTop, rectRight, rectBottom);
	}

	/**
	 * @return
	 * 		the rectangle that represents the location on the drawing
	 * 		surface where the top card in the current player's deck is to
	 * 		be drawn
	 */
	private RectF thisPlayerTopCardLocation() {
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
	private RectF middlePileTopCardLocation() {
		// near the middle-bottom of the drawing surface, based on the height
		// and width, and the percentages defined above
		int height = surface.getHeight();
		int width = surface.getWidth();
		float rectLeft = (75-CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
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
	private void drawOurHand(Canvas g, RectF thisRect, float deltaX, float deltaY, int numCards)
	{
		//loop through from front to back, drawing a card in each location
		for(int i = 0; i < numCards; i++)
		{
			//determine the position of this card's topLeft corner
			float left = thisRect.left + i * deltaX;
			float top = thisRect.top + i * deltaY;
			//draw the correct card, into the appropriate rectangle
			synchronized (state.getHand(0)) {
				try{
					drawCard(g, new RectF(left, top, left + thisRect.width(),
							top + thisRect.height()), state.getHand(0).cards.get(i));
				}catch(ArrayIndexOutOfBoundsException AE){

				}catch (IndexOutOfBoundsException IE){

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

		// ignore everything except down-touch events
		if (event.getAction() != MotionEvent.ACTION_DOWN) return;

		// get the location of the touch on the surface
		int x = (int) event.getX();
		int y = (int) event.getY();

		// determine whether the touch occurred on the top-card of either
		// the player's deck or the draw deck
		RectF myTopCardLoc = thisPlayerTopCardLocation();
		RectF drawTopCardLoc = checkHandRect();
		if (myTopCardLoc.contains(x, y)) {
			// it's on my pile: we're playing a card: send action to
			// the game
			if(state.getHand(0).size() != 0) {
				game.sendAction(new GFRequestAction(this, 1, state.getHand(0).
						cards.get((int) (Math.random() * state.getHand(0).size()))));
				Log.i("Contains xy", "sent action.");
			}else{
				game.sendAction(new GFRequestAction(this, 0, null));
			}
		}
		else if (drawTopCardLoc.contains(x, y)) {
			//The touch is on the Check Hand 'Button'
			//We are checking our Hand!
			game.sendAction(new GFCheckHandAction(this));
		}
		else {
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

		//if the player asked for a card from another player, and SUCCESSFULLY took that card
		if(hist.getPlayerAsk() != -1 && hist.getRankTake() != -1 && hist.getSuccess() == true) {
			return playerNames[hist.getCurrentPlayer()] + " took the " + hist.getRankTake() + " cards from " +
					playerNames[hist.getPlayerAsk()] + ".";
			//Printed in the form: "Player1 took the X cards from Player2."

			//if the player asked for a card, but DID NOT get that card
		}else if(hist.getPlayerAsk() != -1 && hist.getRankTake() != 1){
			return playerNames[hist.getCurrentPlayer()]+" asked "+playerNames[hist.getPlayerAsk()]+" for the "+
					hist.getRankTake()+"...";
			//Printed in the form: "Player1 asked Player2 for the X..."

			//if the player added to their score
		}else if(hist.getScoreAdd() != -1){
			return playerNames[hist.getCurrentPlayer()]+" just added "+hist.getScoreAdd()+" to their score!";
			//Printed in the form: "Player1 just added X to their score!"
		}

		return "";
	}
}
