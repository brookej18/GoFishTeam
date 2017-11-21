package edu.up.cs301.gofish;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * Deck class - represents a deck of cards (not necessarily a full one)
 * 
 * @author Alex Costa
 * @author Jackson Brooke
 * @author Logan Crawford
 * @version July 2013
 *
 */
public class Deck implements Serializable {

	// to satisfy Serializable interface
	private static final long serialVersionUID = 3216223171210121485L;
	
	// the cards in our deck; the last card in the ArrayList is the top card
	// in the deck
	public ArrayList<Card> cards;
	
	/**
	 * constructor, creating an empty deck
	 */
	public Deck() {
		cards = new ArrayList<Card>();
	}
	
	/** copy constructor, making an exact copy of a deck
	 * 
	 * @param orig
	 * 		the deck from which the copy should be made
	 */
	public Deck(Deck orig) {
		// synchronize to ensure that original is not being modified as we
		// iterate over it
		synchronized(orig.cards) {
			// create a new arrayList for our new deck; add each card in it
			cards = new ArrayList<Card>();
			for (Card c: orig.cards) {
				cards.add(c);
			}
		}
	}
	
	/**
	 * adds one of each card, increasing the size of the deck by 52. Cards are added
	 * spades first (King to Ace), then similarly with hearts, diamonds and clubs.
	 * 
	 * @return
	 * 		the deck
	 */
	public Deck add52() {
		// add the cards
		for (char s : "SHDC".toCharArray()) {
			for (char r : "KQJT98765432A".toCharArray()) {
				this.add(Card.fromString(""+r+s));
			}
		}
		
		// return the deck
		return this;
	}
	
	/**
	 * 
	 */
	public Deck shuffle() {
		// synchronize so that we don't have someone trying to modify the
		// deck as we're modifying it
		synchronized (this.cards) {
			// go through a loop that randomly rearranges the cards
			for (int i = cards.size(); i > 1; i--) {
				int spot = (int)(i* Math.random());
				Card temp = cards.get(spot);
				cards.set(spot, cards.get(i-1));
				cards.set(i-1, temp);
			}
		}
		
		// return the deck
		return this;
	}

	/**
	 * Moves the top card the current deck to the top of another; does nothing if
	 * the first deck is empty
	 * 
	 * @param targetDeck
	 * 		the deck to which the card should be moved
	 */
	public void moveTopCardTo(Deck targetDeck) {
		
		// will hold the card
		Card c = null;
		
		// size of the first deck
		int size;
		
		// indivisibly check the deck for empty, and remove the card, to
		// avoid a race condition
		synchronized(this.cards) {
			size = this.size();
			if (size > 0) {
				c = cards.remove(cards.size()-1);
			}
		}
		
		// if the original size was non-zero, add the card to the top of the
		// target deck
		if (size > 0) {
			targetDeck.add(c);
		}
	}
	
	/**
	 * move all cards in the current deck to a another deck by repeated moving
	 * a single card from top to top
	 * 
	 * @param target
	 * 		the deck that will get the cards
	 */
	public void moveAllCardsTo(Deck target) {
		// if the source and target are the same, ignore
		if (this == target) {
			return;
		}
		
		// keep moving cards until the current deck is empty
		while (size() > 0) {
			moveTopCardTo(target);
		}
	}
	
	/**
	 * add a card to the top of a deck
	 * 
	 * @param c
	 * 		the card to add
	 */
	public void add(Card c) {
		// synchronize so that the underlying ArrayList is not accessed
		// inconsistently
		synchronized(this.cards) {
			cards.add(c);
		}
	}
	
	/**
	 * @return
	 * 		the number of cards in the deck
	 */
	public int size() {
		return cards.size();
	}
	
	/**
	 * replace each element in the deck with a null card; does not change
	 * the size of the deck, but rather causes the deck to yield null for
	 * when accessed with a valid index. A null card might represent that
	 * there is a card in that position, but that it is face-down.
	 */
	public void nullifyDeck() {
		// synchronize so that we don't get any race conditions (e.g., with
		// shuffle()
		synchronized (this.cards) {
			// null out each card
			for (int i = 0; i < cards.size(); i++) {
				cards.set(i, null);
			}
		}
	}

	/**
	 * remove the top card from the deck
	 * 
	 * @return
	 * 		the top card in the deck, which is also removed,
	 * 		or null if the deck was empty
	 */
	public Card removeTopCard() {
		synchronized (this.cards) {
			if (cards.isEmpty()) return null;
			return cards.remove(cards.size()-1);
		}
	}
	
	/**
	 * @return
	 * 		the top card in the deck, without removing it; null
	 * 		if the deck was empty
	 */
	public Card peekAtTopCard() {
		synchronized (this.cards) {
			if (cards.isEmpty()) return null;
			return cards.get(cards.size()-1);
		}
	}

	/**
	 * creates a printable version of the object, a list
	 * of two-character names for each card in the deck
	 * (starting at the bottom of the deck), surrounded by
	 * brackets
	 * 
	 * @return
	 * 		a printable version of the deck
	 */
	@Override
	public String toString() {
		// the eventual return value
		String rtnVal = "";
		
		// synchronize to avoid iterating while the
		// deck is being modified
		synchronized (this.cards) {
			// loop through, printing the short name of each
			// card (using '--' for null)
			for (Card c : cards) {
				if (c == null) {
					rtnVal += " --";
				}
				else {
					rtnVal += " "+c.shortName();
				}
			}
		}
		
		// surround by brackets and retuirn
		rtnVal = "[" + rtnVal + " ]";
		return rtnVal;
	}

	/**
	 * Helper method that will sort the deck passed to it. NOTE: this sort method
	 * will only sort the rank of the cards (i.e. in 2 - ACE order) and will not
	 * sort the cards by suit.
	 *
	 */
	public void sort(){
		//create a new copy of the deck for cards to be placed to
		Deck sortedCopy = new Deck();

		int i, index, smallest;
		while(cards.size() > 0){
			//always set the placeholders back to the first index
			index = 0;
			smallest = cards.get(index).getRank().value(14);

			//loop through the hand to try and find the smallest value of card
			for(i = 0; i < size(); i++){
				//if we found a card that has a smaller value than our current lowest
				if(cards.get(i).getRank().value(14) < smallest){
					//set the new index to that card and change the rank
					index = i;
					smallest = cards.get(i).getRank().value(14);
				}
			}

			//after the loop, add the smallest current card to the back of the sorted deck
			//and remove that card from the hand
			sortedCopy.cards.add(cards.get(index));
			cards.remove(index);
		}

		//lastly, move every card from the sortedCopy to the hand in ascending order
		for(i = 0; i < sortedCopy.cards.size(); i++) cards.add(sortedCopy.cards.get(i));
	}
}
