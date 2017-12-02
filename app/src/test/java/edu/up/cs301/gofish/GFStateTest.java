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
        GFState state = new GFState(2);
        state.getHand(0).add52();

        Deck hand = new Deck();
        hand.add52();

        assertEquals(state.getHand(0).peekAtTopCard(), hand.peekAtTopCard());

        state.getHand(0).moveTopCardTo(state.getHand(1));
        hand.moveTopCardTo(state.getHand(1));
        assertEquals(state.getHand(0).peekAtTopCard(), hand.peekAtTopCard());

        state.getHand(0).moveTopCardTo(state.getHand(1));
        hand.moveTopCardTo(state.getHand(1));
        assertEquals(state.getHand(0).peekAtTopCard(), hand.peekAtTopCard());
    }

    @Test
    public void testWhoseTurn() throws Exception {
        GFState state = new GFState(2);

        //assuming the game starts with player index 1
        assertEquals(state.whoseTurn(), 0);

        state.setWhoseTurn(2);

        assertEquals(state.whoseTurn(), 2);
    }

    @Test
    public void testGetScore() throws Exception {
        GFState state = new GFState(2);

        assertEquals(state.getScore(0), 0);
        assertEquals(state.getScore(1), 0);
    }

    @Test
    public void testSetScore() throws Exception {
        GFState state = new GFState(2);

        //test for functionality first, to make sure the correct player gets the correct score
        state.setScore(1, 32);
        assertEquals(state.getScore(1), 32);

        //score should never be a negative number
        state.setScore(1, -100);
        assertNotEquals(state.getScore(1), -100);
    }

    @Test
    public void testTurnHistory() throws Exception {
        GFState state = new GFState(2);
    }

    @Test
    public void testGFStateCopy() throws Exception {
        GFState state = new GFState(2);

        state.getHand(0).add52();
        state.setWhoseTurn(1);

        state.getHand(0).moveTopCardTo(state.getHand(1));
        state.getHand(0).moveTopCardTo(state.getHand(1));
        state.getHand(0).moveTopCardTo(state.getHand(1));
        state.getHand(0).moveTopCardTo(state.getHand(1));
        state.getHand(0).moveTopCardTo(state.getHand(2));
        state.getHand(0).moveTopCardTo(state.getHand(2));
        state.getHand(0).moveTopCardTo(state.getHand(2));
        state.getHand(0).moveTopCardTo(state.getHand(2));


        GFState copy = new GFState(state);

        assertEquals(state.getHand(0), copy.getHand(0));
        assertEquals(state.getHand(1), copy.getHand(1));
        assertEquals(state.getHand(2), copy.getHand(2));
        assertEquals(state.whoseTurn(), copy.whoseTurn());

    }
}