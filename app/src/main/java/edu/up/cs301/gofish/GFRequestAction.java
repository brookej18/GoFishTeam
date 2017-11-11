package edu.up.cs301.gofish;

import edu.up.cs301.game.GamePlayer;

/**
 * A GFRequestAction is an action that represents requesting a card of similar rank from
 * another players hand.
 *
 * @author Alex Costa, Jackson Brooke, Logan Crawford
 * @version November 2017
 */

public class GFRequestAction extends GFMoveAction {

    private static final long serialVersionUID = 3250639793499599047L;

    /**
     * Constructor for GFMoveAction
     *
     * @param player the player making the move
     */
    public GFRequestAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return
     * 		whether the move is a request from another player or not
     */
    public boolean isRequest(){
        return false;
    }
}
