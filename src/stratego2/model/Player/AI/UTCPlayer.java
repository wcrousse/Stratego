

package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Player.Player;
import stratego2.model.Square;
import stratego2.view.Display;

/**
 *
 * @author roussew
 */
public class UTCPlayer extends AIPlayer {
    private final Color color;
    
    public UTCPlayer(Color color) {
        this.color = color;
    }
    
    public UTCPlayer(Color color, StrategoRules, rules) {
        this.rules = rules;
        this.color = color;
    }
    
    @Override
    public Move getMove(Board board) {
        state = new GameState(board, color);
        ArrayList<Action>possibleActions = getSuccessors(state);
        return null;
    }


    @Override
    public void displayBoard(Board board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Piece> getSetup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void revealSquare(Square square) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reportResult(Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
