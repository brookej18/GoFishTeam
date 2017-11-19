package edu.up.cs301.gofish;

import edu.up.cs301.game.GamePlayer;

/**
 * A CFCheckHandAction is an action that represents checking your own hand for a brook.
 *
 * @author Alex Costa, Jackson Brooke, Logan Crawford
 * @version November 2017
 */

public class GFCheckHandAction extends GFMoveAction {

    private static final long serialVersionUID = 2134321631283669359L;

    /**
     * Constructor for GFMoveAction
     *
     * @param player the player making the move
     */
    public GFCheckHandAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return
     * 		whether the move is a brook or not
     */
    public boolean isBrook() {
        return true;
    }
}
