
package stratego2.view;

import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.Move;
import stratego2.model.Piece;

/**
 *
 * @author roussew
 */
public interface Display {

    /**
     * intended as part of the interface with the player class. Provides the 
     * user with an implementation dependent way of choosing a starting arrangement
     * of his/her pieces.
     * @return an integer array representation of the players pieces. Where the
     * integers correspond to the values of the pieces and the indices represent
     * the location of the pieces, with the convention that [0, 0] represents the
     * back row, leftmost column. 
     */
    public int[][] getSetup();
    /**
     * Intended as part of the interface with the player class. Provides the
     * user with an implementation dependent way of declaring a move. 
     * @param board A representation of the current game state.
     * @return a simple data structure, containing the coordinates the starting
     * square and the destination square of the piece to be moved.
     */
    public Move getMove(Board board);
    /**
     * provides an implementation dependent representation of the game state
     * to the user
     * @param board a representation of the current game state. 
     */
    public void displayBoard(Board board);
    /**
     * Intended to be called immediately when a move proposed by the user has
     * been found to be illegal. Notifies the user that his/her move was rejected
     * and that he/she must chose a move again.
     */
    public void reportIllegalMove();
    /**
     * Intended to be called subsequent to a battle between pieces, reveals the
     * identities of the pieces involved.
     * @param piece 
     */
    public void revealSquare(Piece piece);
    
    /**
     * Intended to be called after one side has won the game. Informs the player
     * of the result
     * @param color the color of the pieces controlled by the winning player
     */
    public void displayResults(Color color);
    
}
