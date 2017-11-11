package edu.up.cs301.gofish;

import org.junit.Test;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;

import static org.junit.Assert.*;

/**
 * Created by Jackson on 11/9/17.
 */

public class GFStateTest {

    //Instance variables of the GFState class

    //Private Deck[] hand denotes the hand of each player
    //Private int whoseTurn denotes which players turn it currently is
    //Private int[] score is the score for each individual player

    @Test
    public void testGetHand() throws Exception {
        GFState state = new GFState();
        state.getDeck(0).add52();

        Deck hand = new Deck();
        hand.add52();

        assertEquals(state.getDeck(0).peekAtTopCard(), hand.peekAtTopCard());

        state.getDeck(0).moveTopCardTo(state.getDeck(1));
        hand.moveTopCardTo(state.getDeck(1));
        assertEquals(state.getDeck(0).peekAtTopCard(), hand.peekAtTopCard());

        state.getDeck(0).moveTopCardTo(state.getDeck(1));
        hand.moveTopCardTo(state.getDeck(1));
        assertEquals(state.getDeck(0).peekAtTopCard(), hand.peekAtTopCard());
    }

    @Test
    public void testWhoseTurn() throws Exception {
        GFState state = new GFState();

        //assuming the game starts with player index 1
        assertEquals(state.whoseTurn(), 1);

        state.setwhoseTurn(2);

        assertEquals(state.whoseTurn(), 2);
    }

    @Test
    public void testGetScore() throws Exception {
        GFState state = new GFState();

        assertEquals(state.getScore(1), 0);
        assertEquals(state.getScore(2), 0);
    }

    @Test
    public void testSetScore() throws Exception {
        GFState state = new GFState();

        //test for functionality first, to make sure the correct player gets the correct score
        state.setScore(1, 32);
        assertEquals(state.getScore(1), 32);

        //score should never be a negative number
        state.setScore(1, -100);
        assertNotEquals(state.getScore(1), -100);
    }

    @Test
    public void testTurnHistory() throws Exception {
        GFState state = new GFState();
    }

    @Test
    public void testGFStateCopy() throws Exception {
        GFState state = new GFState();

        state.getDeck(0).add52();
        state.setwhoseTurn(1);

        state.getDeck(0).moveTopCardTo(state.getDeck(1));
        state.getDeck(0).moveTopCardTo(state.getDeck(1));
        state.getDeck(0).moveTopCardTo(state.getDeck(1));
        state.getDeck(0).moveTopCardTo(state.getDeck(1));
        state.getDeck(0).moveTopCardTo(state.getDeck(2));
        state.getDeck(0).moveTopCardTo(state.getDeck(2));
        state.getDeck(0).moveTopCardTo(state.getDeck(2));
        state.getDeck(0).moveTopCardTo(state.getDeck(2));


        GFState copy = new GFState(state);

        assertEquals(state.getDeck(0), copy.getDeck(0));
        assertEquals(state.getDeck(1), copy.getDeck(1));
        assertEquals(state.getDeck(2), copy.getDeck(2));
        assertEquals(state.whoseTurn(), copy.whoseTurn());

    }
}