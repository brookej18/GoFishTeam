package edu.up.cs301.gofish;

import java.util.ArrayList;

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

	//arrayList of GFHistory objects for the purpose of keeping track of all actions in the game.
	//All new objects are added to the back of the ArrayList
	public ArrayList<GFHistory> history;


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
    	hand = new Deck[6];
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
				hand[0].sort();
				hand[1].sort();
				break;

			//case where there are three players (deal out 7 cards to each)
			case 3:
				for(i=0; i<7; i++){
					hand[4].moveTopCardTo(hand[0]);
					hand[4].moveTopCardTo(hand[1]);
					hand[4].moveTopCardTo(hand[2]);
				}
				hand[0].sort();
				hand[1].sort();
				hand[2].sort();
				break;

			//case where there are four players (deal out 5 cards to each)
			case 4:
				for(i=0; i<5; i++){
					hand[4].moveTopCardTo(hand[0]);
					hand[4].moveTopCardTo(hand[1]);
					hand[4].moveTopCardTo(hand[2]);
					hand[4].moveTopCardTo(hand[3]);
				}
				hand[0].sort();
				hand[1].sort();
				hand[2].sort();
				hand[3].sort();
				break;
		}

		//initialize the score array so that everyone's score starts at zero
		score = new int[numPlayers];
		for(i=0; i<numPlayers; i++) score[i] = 0;

		//initialize the history arraylist object
		history = new ArrayList<>();
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
		hand = new Deck[6];
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

    	//set the history object
    	history = orig.history;
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
	 * NOT IMPLEMENTED YET
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

	/**
	 * This method should null out all the cards that the passed player should not
	 * be able to see. The hand that a player should never see is what is in the deck,
	 * as well as other players hands.
	 *
	 * @param playerNum
	 */
	public void nullCards(int playerNum){

		//for every hand, if the playerNum does match, those cards will become null.
		//The hand itself will still contain 'x' amount of cards, but they will just
		//be null
		if(playerNum != 0) hand[0].nullifyDeck();
		if(playerNum != 1) hand[1].nullifyDeck();
		if(playerNum != 2) hand[2].nullifyDeck();
		if(playerNum != 3) hand[3].nullifyDeck();
		if(playerNum != 4) hand[4].nullifyDeck();
		if(playerNum != 5) hand[5].nullifyDeck();
	}

	/**
	 * This method will look into the calling players deck and check it for brooks.
	 * If any are present, the method will remove all four cards from the players hand
	 * and place them in the discard pile. The score of this player will also be updated
	 * to reflect the new addition
	 *
	 * @param playerIdx
	 * 		Player calling the method
	 */
	public void findBrook(int playerIdx){

		synchronized (getHand(playerIdx)) {

			//immediate check, if the hand size is 0, return
			if (getHand(playerIdx).size() <= 0) return;

			//placeholder values
			int startIndex = 0;
			int currValue = getHand(playerIdx).cards.get(0).getRank().value(14);

			//iterate through all cards in this players hand
			int i;
			for (i = 0; i < getHand(playerIdx).size(); i++) {
				//if we come across a card that is a different rank than our place holder value
				//we will reset the placeholder values to account for the next set of cards
				if (currValue != getHand(playerIdx).cards.get(i).getRank().value(14)) {
					currValue = getHand(playerIdx).cards.get(i).getRank().value(14);
					startIndex = i;
					continue;
				}

				//else, if the difference of our current position (i) and our placeholder index (startIndex)
				//is three, we have found four cards in a row that all have the same rank.
				if (3 == i - startIndex) {

					//add all cards to the discard pile, and remove them from the current players hand
					getHand(5).cards.add(getHand(playerIdx).cards.get(i));
					getHand(playerIdx).cards.remove(i);
					getHand(5).cards.add(getHand(playerIdx).cards.get(i - 1));
					getHand(playerIdx).cards.remove(i - 1);
					getHand(5).cards.add(getHand(playerIdx).cards.get(i - 2));
					getHand(playerIdx).cards.remove(i - 2);
					getHand(5).cards.add(getHand(playerIdx).cards.get(i - 3));
					getHand(playerIdx).cards.remove(i - 3);

					//set our current players score to include the cards added, and set our iterator
					//value (i) back three places (since we just took out set of four cards)
					setScore(playerIdx, currValue);
					postHistory(playerIdx, -1, -1, currValue, false);
					i -= 3;
				}
			}

			//if the player has just emptied his/her/its hand of all cards by calling this method,
			//and there are cards left in the draw pile, give that player another card.
			if(getHand(playerIdx).size() == 0 && getHand(4).size() > 0){
				getHand(4).moveTopCardTo(getHand(playerIdx));
			}
		}

		return;
	}

	/**
	 * Setter method for the ArrayList history instance variable of this class. For integer null
	 * values, the value should be set to -1. For null boolean value, the value is false.
	 *
	 * @param player
	 * @param targetPlayer
	 * @param targetRank
	 * @param scoreAdded
	 * @param successful
	 */
	public void postHistory(int player, int targetPlayer, int targetRank, int scoreAdded, boolean successful){
		GFHistory temp = new GFHistory();
		temp.setPlayer(player);
		temp.setTargetPlayer(targetPlayer);
		temp.setTargetRank(targetRank);
		temp.setScoreAdded(scoreAdded);
		temp.setSuccess(successful);
		history.add(temp);
	}

}
