package edu.up.cs301.gofish;

/**
 * A GFHistory object is designed to save the values passed by an action, and involve the current
 * player, (optional) target player being fished from, (optional) rank of the card being asked for,
 * (optional) score that was added to the current players score, and (optional) if the Go Fish
 * was successful or not
 *
 * @author: Jackson R. Brooke
 * @version : November 20, 2017.
 */

public class GFHistory {

    /*Instance variables:
    * In the situation where a variable is being used OPTIONALLY, because all instance variables
    * (excluding boolean success) are integers, the null value to be used with this class is -1.
    * For boolean success, the null value is false*/

    private int currentPlayer;      //the player whose turn it is
    private int playerAsk;          //(optional) player being fished from
    private int rankTake;           //(optional) rank being fished for
    private int scoreAdd;           //(optional) score added to the current players score
    private boolean success;        //(optional) was the Go Fish successful or not
    private boolean usedByAI;       //denotes whether the smart AI has used an instance of this
                                    //object for requesting cards. Initially set to false, set to
                                    //true if used by any AI

    /**
     * Constructor
     *
     * @param player
     * @param playerAsked
     * @param rankTaken
     * @param scoreAdded
     * @param successful
     * @param usedBySmartAI
     */
    public void GFHistory(int player, int playerAsked, int rankTaken, int scoreAdded, boolean successful, boolean usedBySmartAI){
        currentPlayer = player;
        playerAsk = playerAsked;
        rankTake = rankTaken;
        scoreAdd = scoreAdded;
        success = successful;
        usedByAI = usedBySmartAI;
    }

    /*Setter methods for all instance variables*/

    public void setScoreAdded(int scoreAdded) {
        this.scoreAdd = scoreAdded;
    }

    public void setTargetRank(int targetRank) {
        this.rankTake = targetRank;
    }

    public void setTargetPlayer(int targetPlayer) {
        this.playerAsk = targetPlayer;
    }

    public void setPlayer(int player) {
        this.currentPlayer = player;
    }

    public void setSuccess(boolean successful){
        success = successful;
    }

    public void setUsedByAI(boolean usedBySmartAI){
        usedByAI = usedBySmartAI;
    }


    /*Getter methods for all instance variables*/

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPlayerAsk() {
        return playerAsk;
    }

    public int getRankTake() {
        return rankTake;
    }

    public int getScoreAdd() {
        return scoreAdd;
    }

    public boolean getSuccess(){
        return success;
    }

    public boolean getUsedByAI(){
        return usedByAI;
    }
}
