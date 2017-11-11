package edu.up.cs301.gofish;

import edu.up.cs301.game.GamePlayer;

/**
 * A GFPlayAction is an action that represents playing a card on the "up"
 * hand.
 * 
 * @author Steven R. Vegdahl
 * @version 31 July 2002
 */
public class GFPlayAction extends GFMoveAction
{
	private static final long serialVersionUID = 3250639793499599047L;

	/**
     * Constructor for the GFPlayMoveAction class.
     * 
     * @param player  the player making the move
     */
    public GFPlayAction(GamePlayer player)
    {
        // initialize the source with the superclass constructor
        super(player);
    }

    /**
     * @return
     * 		whether this action is a "play" move
     */
    public boolean isPlay() {
        return true;
    }
    
}
