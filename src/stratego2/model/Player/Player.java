
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

    public Move getMove(Board board);

    public void reportIllegalMove();

    public void displayBoard(Board board);

    public List<Piece> getSetup();

    public Color getColor();

    public void revealSquare(Square square);
    
}
