package edu.up.cs301.gofish;

import android.graphics.Color;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * This is the primary activity for the Go Fish game. This sets up the objects
 * where the user may select the different players they wish to play against.
 * 
 * @author Alex Costa, Jackson Brooke, Logan Crawford
 * @version November 2017
 */
public class GFMainActivity extends GameMainActivity {
	
	public static final int PORT_NUMBER = 4752;

	/** A Go Fish game for two to four players. The default is local human vs. dumb computer */

	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<>();
		
		playerTypes.add(new GamePlayerType("human player (green)") {
			public GamePlayer createPlayer(String name) {
				return new GFHumanPlayer(name, Color.GREEN);
			}});

		playerTypes.add(new GamePlayerType("human player (yellow)") {
			public GamePlayer createPlayer(String name) {
				return new GFHumanPlayer(name, Color.YELLOW);
			}
		});

		playerTypes.add(new GamePlayerType("Computer Player (simple)") {
			public GamePlayer createPlayer(String name) {
				return new GFComputerPlayer(name, false);
			}
		});

		playerTypes.add(new GamePlayerType("Computer Player (smart)") {
			public GamePlayer createPlayer(String name) {
				return new GFComputerPlayer(name, true);
			}
		});

		// Create a game configuration class for Go Fish
		GameConfig defaultConfig = new GameConfig(playerTypes, 2, 4, "Go Fish", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Computer", 2);
		
		// Set the initial information for the remote player
		defaultConfig.setRemoteData("Guest", "", 1);
		
		//done!
		return defaultConfig;
	}//createDefaultConfig

	@Override
	public LocalGame createLocalGame(int numberOfPlayers) {
		return new GFLocalGame(numberOfPlayers);
	}
}
