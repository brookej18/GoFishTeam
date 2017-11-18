package edu.up.cs301.gofish;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jackson on 11/18/17.
 */
public class DeckTest {
    @Test
    public void add52() throws Exception {
    }

    @Test
    public void shuffle() throws Exception {
    }

    @Test
    public void moveTopCardTo() throws Exception {
    }

    @Test
    public void moveAllCardsTo() throws Exception {
    }

    @Test
    public void add() throws Exception {
    }

    @Test
    public void size() throws Exception {
    }

    @Test
    public void nullifyDeck() throws Exception {
    }

    @Test
    public void removeTopCard() throws Exception {
    }

    @Test
    public void peekAtTopCard() throws Exception {
    }

    @Test
    public void testSort() throws Exception {
        Deck hand = new Deck();
        Deck dealingDeck = new Deck();

        dealingDeck.add52();
        dealingDeck.shuffle();

        //add the last cards of the deck in reverse order
        int i = dealingDeck.size();
        assertEquals(52, i);

        for(i = dealingDeck.size(); i > 42; i--) hand.cards.add(dealingDeck.cards.get(i-1));

        //remove the first 42 cards from dealing deck
        for(i = 0; i < 42; i++) dealingDeck.cards.remove(0);

        hand.sort();

        assertEquals(hand.cards, dealingDeck.cards);

    }

}