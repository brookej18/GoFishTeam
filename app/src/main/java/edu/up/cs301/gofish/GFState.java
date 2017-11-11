package edu.up.cs301.gofish;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the state of a Go Fish game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Alex Costa, Jackson Brooke, Logan Crawford
 * @version November 2017
 *
 */
public class GFState extends GameState {
	private static final long serialVersionUID = -8269749892027578792L;

    /*Instance Variables*/

	//array of Deck to reflect the decks that are present on the table.
	//hand[0]: is the first players hand
	//hand[1]: is the second players hand
	//hand[2]: is the third players hand (provided they are playing)
	//hand[3]: is the fourth players hand (provided they are player)
	//hand[4]: will be the dealing deck, so that all drawn cards come from this hand
	//hand[5]: is the discard deck, so when cards leave the table due to being scored,
	// they will not return to the game
    private Deck[] hand;

    //array of int to reflect the score of each player
    private int[] score;
    
    //integer to reflect which players turn it is
	//0 reflects player 1;	1 reflects player 2;
	//2 reflects player 3;	3 reflects player 3;
    private int whoseTurn;

    //integer to reflect how many players are present in the game
	private int numPlayers;


    /**
     * Constructor for objects of class GFState. Initializes for the beginning of the
     * game, with player 1 as the first to play
     */
    public GFState() {
    	/*Currently, while we focus on just two players to start the game, numPlayers is going
    	* to be defaulted to 2 players. Later implementations will see the numPlayers parameter
    	* being passed to the constructor for more dynamic games*/
    	numPlayers = 2;

    	//initialize so that the local player always goes first
    	whoseTurn = 0;
    	
    	//initialize the decks as follows:
    	// if there are 2 or 3 players, deal out 7 cards to each person
		// else, there are 4 players and each should receive 5

		//create an empty deck for each player and the draw and discard hand
    	hand = new Deck[5];
		int i;
    	for(i=0; i<6; i++) hand[i] = new Deck();

		//give the dealing hand all 52 cards in the deck and shuffle
    	hand[4].add52();
    	hand[4].shuffle();

    	//deal out the cards depending on the number of players
		switch(numPlayers){
			//case where there are only two players (deal out 7 cards to each)
			case 2:
				for(i=0; i<7; i++){
					hand[4].moveTopCardTo(hand[0]);
					hand[4].moveTopCardTo(hand[1]);
				}
				break;

			//case where there are three players (deal out 7 cards to each)
			case 3:
				for(i=0; i<7; i++){
					hand[4].moveTopCardTo(hand[0]);
					hand[4].moveTopCardTo(hand[1]);
					hand[4].moveTopCardTo(hand[2]);
				}
				break;

			//case where there are four players (deal out 5 cards to each)
			case 4:
				for(i=0; i<5; i++){
					hand[4].moveTopCardTo(hand[0]);
					hand[4].moveTopCardTo(hand[1]);
					hand[4].moveTopCardTo(hand[2]);
					hand[4].moveTopCardTo(hand[3]);
				}
				break;
		}

		//initialize the score array so that everyone score starts at zero
		score = new int[numPlayers];
		for(i=0; i<numPlayers; i++) score[i] = 0;
    }
    
    /**
     * Copy constructor for objects of class GFState. Makes a copy of the given state
     *  
     * @param orig  the state to be copied
     */
    public GFState(GFState orig) {
    	//set the number of players in the game
		numPlayers = orig.numPlayers;

		//create a new array of Deck, and copy all hands (even if empty)
		hand = new Deck[5];
		hand[0] = new Deck(orig.hand[0]);	//for player one
		hand[1] = new Deck(orig.hand[1]);	//for player two
		hand[2] = new Deck(orig.hand[2]);	//for player three
		hand[3] = new Deck(orig.hand[3]);	//for player four
		hand[4] = new Deck(orig.hand[4]);	//for draw hand
		hand[5] = new Deck(orig.hand[5]);	//for discard hand

		//set all the current scores for each player
		score = new int[numPlayers];
		int i;
		for(i=0; i<numPlayers; i++) score[i] = orig.score[i];

    	//set the current players turn
    	whoseTurn = orig.whoseTurn;
    }
    
    /**
     * Returns the given deck.
     * 
     * @return  the deck for the given player, or the draw hand if the index is 4
	 *
	 * @param numHand
	 * 		the index of the hand that should be returned
     */
    public Deck getHand(int numHand) {
        if (numHand < 0 || numHand > 5) return null;
        return hand[numHand];
    }
    
    /**
     * Tells which player's turn it is.
     * 
     * @return the index of the player whose turn it is
	 * 	between 0 and 1 for two players
	 * 	between 0 and 2 for three players
	 * 	between 0 and 3 for four players
     */
    public int whoseTurn() {
        return whoseTurn;
    }
    
    /**
     * Changes whose move it currently is.
     * 
     * @param idx
     * 		the index of the player whose move it now is
     */
    public void setWhoseTurn(int idx) {
    	if(idx < 0 || idx > 4) return;
    	whoseTurn = idx;
    }

    /**
	 * Retrieves the score of the given index of player
	 *
	 * @return score
	 * 		returns the score
	 *
	 * @param idx
	 * 		is the index for the given player
	 */
	public int getScore(int idx){
		int numScore;
		if(idx < 0 || idx > numPlayers) return -1;
		numScore = score[idx];
		return numScore;
	}

	/**
	 * Adds a score to the specified players current score
	 *
	 * @param idx
	 * 		is the index for the given player
	 * @param numScore
	 * 		is the score that should be added to the index of player
	 */
	public void setScore(int idx, int numScore){
		if(idx < 0 || idx > numPlayers) return;
		score[idx] += numScore;
	}

	/**
	 *
	 */
	public String[] turnHistory(int whoseTurn, Card cardPlayed){
		return null;
	}

	/**
	 * Getter method to return the number of players from the state
	 *
	 * @return numPlayers
	 * 		is the number of players in the game
	 */
	public int getNumPlayers() {
		return numPlayers;
	}
}
