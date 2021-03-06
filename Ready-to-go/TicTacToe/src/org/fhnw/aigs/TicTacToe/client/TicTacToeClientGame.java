package org.fhnw.aigs.TicTacToe.client;

import javax.swing.JOptionPane;
import org.fhnw.aigs.client.gameHandling.ClientGame;
import org.fhnw.aigs.commons.GameMode;
import org.fhnw.aigs.commons.communication.FieldChangedMessage;
import org.fhnw.aigs.commons.communication.FieldClickFeedbackMessage;
import org.fhnw.aigs.commons.communication.GameEndsMessage;
import org.fhnw.aigs.commons.communication.GameStartMessage;
import org.fhnw.aigs.commons.communication.Message;
import org.fhnw.aigs.commons.communication.PlayerChangedMessage;

/**
 * The TicTacToe game on the client side.<br>
 * v1.0 Initial release<br>
 * v1.0.1 Minor changes
 * @author Matthias Stöckli (v1.0)
 * @version v1.0.1
 */
public class TicTacToeClientGame extends ClientGame {

    /**
     * The grid, i.e. the actual game frame.
     */
    private TicTacToeBoard ticTacToeBoard;

    /**
     * Create a new instance of TiCTacToeClientGame using the standard constructor
     * of {@link ClientGame}.
     * @param gameName The name of the game.
     * @param gameMode The game mode.
     */
    public TicTacToeClientGame(String gameName, GameMode gameMode) {
        super(gameName, gameMode);
    }
    
    /**
     * Create a new instance of TiCTacToeClientGame using the extended constructor
     * of {@link ClientGame}.
     * @param gameName The name of the game.
     * @param gameMode The game mode.
     * @param version The program version
     * @since v1.0.1
     */
    public TicTacToeClientGame(String gameName, String version, GameMode gameMode) {
        super(gameName, version, gameMode);
    }    

    /**
     * Processing of the TicTacToeClient logic. The following actions are defined:
     * <ul>
     * <li>Fade out loading screen.</li>
     * <li>Show feedback for invalid inputs.</li>
     * <li>Show new TicTacToe symbols in case of valid input.</li>
     * <li>Change the status label</li>
     * <li>Inform client about ending game</li>
     * </ul>
     * @param parsedMessage The handled messages
     */
    @Override
    public void processGameLogic(Message parsedMessage) {

        // Fade out overlay.
        if (parsedMessage instanceof GameStartMessage) {
            gameWindow.fadeOutOverlay();
        
        // Add new TicTacToe symbol
        } else if (parsedMessage instanceof FieldChangedMessage) {
            ticTacToeBoard.manipulateGUI(parsedMessage);
            
        // Show feedback in case of (invalid) input
        } else if (parsedMessage instanceof FieldClickFeedbackMessage) {
            ticTacToeBoard.manipulateGUI(parsedMessage);

        // Change the status label in the header.
        // your turn = "Your turn!", opponent's turn = "Wait for opponent...".
        } else if (parsedMessage instanceof PlayerChangedMessage) {
            PlayerChangedMessage endTurnMessage = (PlayerChangedMessage) parsedMessage;
            if (endTurnMessage.getNewPlayer().equals(player)) {
                getGameWindow().getHeader().setStatusLabelText("Your turn!");
            } else {
                getGameWindow().getHeader().setStatusLabelText("Wait for opponent...");
            }

        // Show a prompt when the game ends.
        } else if (parsedMessage instanceof GameEndsMessage) {
            setNoInteractionAllowed(true);
            GameEndsMessage gameEndMessage = (GameEndsMessage) parsedMessage;
            JOptionPane.showMessageDialog(null, gameEndMessage.getReason(), "Game ends", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * This method is called when the connection has been established.
     * In this case no further action is required and the game is started 
     * immediately.
     */
    @Override
    public void onGameReady() {
        startGame();
    }

    /** See {@link TicTacToeClientGame#ticTacToeBoard}. */
    public TicTacToeBoard getTicTacToeBoard() {
        return ticTacToeBoard;
    }

    /** See {@link TicTacToeClientGame#ticTacToeBoard}. */
    public void setTicTacToeBoard(TicTacToeBoard ticTacToeGrid) {
        this.ticTacToeBoard = ticTacToeGrid;
    }

}
