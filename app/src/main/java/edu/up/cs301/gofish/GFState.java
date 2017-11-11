package edu.up.cs301.gofish;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the state of a Go Fish game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */
public class GFState extends GameState {
	private static final long serialVersionUID = -8269749892027578792L;

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

	// the three hand of cards:
    //  - 0: hand for player 0
    //  - 1: hand for player 1
    //  - 2: the "up" hand, where the top card
	// Note that when players receive the state, all but the top card in all hand
	// are passed as null.
    private Deck[] hand;
    
    // integer that sets the turn of the player
    private int whoseTurn;

    /**
     * Constructor for objects of class GFState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     *  
     */
    public GFState() {
    	// randomly pick the player who starts
    	whoseTurn = (int)(2* Math.random());
    	
    	// initialize the decks as follows:
    	// - each player deck (#0 and #1) gets half the cards, randomly
    	//   selected
    	// - the middle deck (#2) is empty
    	hand = new Deck[3];
    	hand[0] = new Deck(); // create empty deck
    	hand[1] = new Deck(); // create empty deck
    	hand[2] = new Deck(); // create empty deck
    	hand[whoseTurn].add52(); // give all cards to player whose turn it is, in order
    	hand[whoseTurn].shuffle(); // shuffle the cards
    	// move cards to opponent, until to hand have ~same size
    	while (hand[whoseTurn].size() >=
    			hand[1-whoseTurn].size()+1) {
    		hand[whoseTurn].moveTopCardTo(hand[1-whoseTurn]);
    	}
    }
    
    /**
     * Copy constructor for objects of class GFState. Makes a copy of the given state
     *  
     * @param orig  the state to be copied
     */
    public GFState(GFState orig) {
    	// set index of player whose turn it is
    	whoseTurn = orig.whoseTurn;
    	// create new deck array, making copy of each deck
    	hand = new Deck[3];
    	hand[0] = new Deck(orig.hand[0]);
    	hand[1] = new Deck(orig.hand[1]);
    	hand[2] = new Deck(orig.hand[2]);
    }
    
    /**
     * Gives the given deck.
     * 
     * @return  the deck for the given player, or the middle deck if the
     *   index is 2
     */
    public Deck getDeck(int num) {
        if (num < 0 || num > 2) return null;
        return hand[num];
    }
    
    /**
     * Tells which player's turn it is.
     * 
     * @return the index (0 or 1) of the player whose turn it is.
     */
    public int whoseTurn() {
        return whoseTurn;
    }
    
    /**
     * change whose move it is
     * 
     * @param idx
     * 		the index of the player whose move it now is
     */
    public void setwhoseTurn(int idx) {
    	whoseTurn = idx;
    }
 
    /**
     * Replaces all cards with null, except for the top card of deck 2
     */
    public void nullAllButTopOf2() {
    	// see if the middle deck is empty; remove top card from middle deck
    	boolean empty2 = hand[2].size() == 0;
    	Card c = hand[2].removeTopCard();
    	
    	// set all cards in deck to null
    	for (Deck d : hand) {
    		d.nullifyDeck();
    	}
    	
    	// if middle deck had not been empty, add back the top (non-null) card
    	if (!empty2) {
    		hand[2].add(c);
    	}
    }

    public int getScore(int idx){
    	return 0;
	}

	public void setScore(int idx, int score){

	}
}
