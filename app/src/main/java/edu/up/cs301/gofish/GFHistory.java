package edu.up.cs301.gofish;

/**
 * Created by Jackson on 11/20/17.
 */

public class GFHistory {

    private int currentPlayer;
    private int playerAsk;
    private int rankTake;
    private int scoreAdd;
    private boolean success;

    public void GFHistory(int player, int playerAsked, int rankTaken, int scoreAdded, boolean successful){
        currentPlayer = player;
        playerAsk = playerAsked;
        rankTake = rankTaken;
        scoreAdd = scoreAdded;
        success = successful;
    }


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
}
