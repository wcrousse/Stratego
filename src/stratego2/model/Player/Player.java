
package stratego2.model.Player;

import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.FriendlyPiece;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Square;

/**
 *
 * @author roussew
 */
public interface Player {

    /**
     * prompts the player to select a move.
     * @param board the current game state
     * @return a Move object 
     */
    public Move getMove();

    /**
     * informs the player that the move he/she just selected was not legal, that
     * it is still the player's move
     * @throws java.lang.Exception
     */
    public void reportIllegalMove() throws Exception;

    /**
     * This method should not be used. Players should never be passed a complete
     * board representation. 
     * Displays the current game state to the player. In general this method
     * should simply call the View.displayBoard() method. 
     * @param board The current game state
     */
    public void displayBoard();

    /**
     * prompts the player to choose an initial setup of his/her pieces.
     * @return 
     */
    public List<FriendlyPiece> getSetup();

    /**
     * gets the color of the pieces controlled by the player
     * @return 
     */
    public Color getColor();

    /**
     * reveals the identity of the piece occupying the given square. Should
     * be called after a battle has taken place. In general should just trigger
     * the corresponding View method.
     * @param square the square to be revealed
     */
    public void revealSquare(Square square, Move move);

    /**
     * reveals the winner of the game.
     * @param color the color of the pieces controlled by the winning player
     */
    public void reportResult(Color color);
    
    /**
     * reveals the move that was most recently successfully played. 
     * this and not display board should be called after each move.
     * @param move 
     */
    public void reportMove(Move move);
}
