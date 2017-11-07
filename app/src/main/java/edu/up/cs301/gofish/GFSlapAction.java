package edu.up.cs301.gofish;

import edu.up.cs301.game.GamePlayer;

/**
 * A GFSlapAction is an action that represents slapping the card that is
 * on the "up" pile.
 * 
 * @author Steven R. Vegdahl
 * @version 31 July 2002
 */
public class GFSlapAction extends GFMoveAction
{
	private static final long serialVersionUID = 2134321631283669359L;

	/**
     * Constructor for the GFSlapMoveAction class.
     * 
     * @param player  the player making the move
     */
    public GFSlapAction(GamePlayer player)
    {
        // initialize the source with the superclass constructor
        super(player);
    }

    /**
	 * @return whether this action is a "slap" move
     */
    public boolean isSlap() {
        return true;
    }
    
}
