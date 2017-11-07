package edu.up.cs301.gofish;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A game-move object that a tic-tac-toe player sends to the game to make
 * a move.
 * 
 * @author Steven R. Vegdahl
 * @version 2 July 2001
 */
public abstract class GFMoveAction extends GameAction {
	
	private static final long serialVersionUID = -3107100271012188849L;

    /**
     * Constructor for GFMoveAction
     *
     * @param player the player making the move
     */
    public GFMoveAction(GamePlayer player)
    {
        // invoke superclass constructor to set source
        super(player);
    }
    
    /**
     * @return
     * 		whether the move was a slap
     */
    public boolean isSlap() {
    	return false;
    }
    
    /**
     * @return
     * 		whether the move was a "play"
     */
    public boolean isPlay() {
    	return false;
    }

}
