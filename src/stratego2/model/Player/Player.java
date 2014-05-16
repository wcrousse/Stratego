
package stratego2.model.Player;

import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
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
    public Move getMove(Board board);

    /**
     * informs the player that the move he/she just selected was not legal, that
     * it is still the player's move
     */
    public void reportIllegalMove();

    /**
     * Displays the current game state to the player. In general this method
     * should simply call the View.displayBoard() method. 
     * @param board The current game state
     */
    public void displayBoard(Board board);

    /**
     * prompts the player to choose an initial setup of his/her pieces.
     * @return 
     */
    public List<Piece> getSetup();

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
    public void revealSquare(Square square);

    /**
     * reveals the winner of the game.
     * @param color the color of the pieces controlled by the winning player
     */
    public void reportResult(Color color);
    
}
