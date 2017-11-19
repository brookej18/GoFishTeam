package edu.up.cs301.gofish;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A game-move object that a go fish player sends to the game to make a move.
 * 
 * @author Alex Costa, Jackson Brooke, Logan Crawford
 * @version November 2017
 */
public abstract class GFMoveAction extends GameAction {
	
	private static final long serialVersionUID = -3107100271012188849L;
    private int targetPlayer;
    private Card targetCard;

    /**
     * Constructor for GFMoveAction
     *
     * @param player the player making the move
     */
    public GFMoveAction(GamePlayer player) {
        //invoke superclass constructor to set source
        super(player);
    }

    public GFMoveAction(GamePlayer player, int target, Card card) {
        //invoke superclass constructor to set source
        super(player, target, card);
    }
    
    /**
     * @return
     * 		whether the move is a brook or not
     */
    public boolean isBrook() {
    	return false;
    }
    
    /**
     * @return
     * 		whether the move is a request from another player or not
     */
    public boolean isRequest(){
        return false;
    }

    public Card getTargetCard() {
        return targetCard;
    }

    public int getTargetPlayer() {
        return targetPlayer;
    }

}
