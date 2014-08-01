

package stratego2.model.Player.AI;

import java.util.List;
import stratego2.model.Board;
import stratego2.model.Game;
import stratego2.model.GameState;
import stratego2.model.Move;

/**
 *
 * @author roussew
 */
public class MCSTNode {

    /**
     * the game state
     */
    private final GameState state;
    /**
     * representation of the move made.
     */
    private final Move move;
    /**
     * should not be accessible outside of the object. 
     */
    private double sum;
    /**
     * the utility score of this action.
     */
    private double utility;
    /**
     * keeps track of the number of times this move/state pair has been executed.
     */
    private int count;
    
    private List<MCSTNode>children;
    
    private MCSTNode parent;
    
    public MCSTNode(GameState state, Move move){
        this.state = state;
        this.move = move;
    }
    
    public MCSTNode(GameState root) {
        state = root;
        move = null;
    }
    
    public MCSTNode(GameState state, MCSTNode parent) {
        this.state = state;
        this.parent = parent;
        move = null;
    }
    
    
    /**
     * updates the utility score of this action.
     * @param value a new value indication the results of a game simulation,
     * following the default police, beginning at this action. 
     */
    public void updateUtility(double value) {
        sum += value;
        count++;
        utility = sum/count;
    }
    
    public GameState getState() {
        return state;
    }
    
    public Move getMove() {
        return move;
    }
    
    public void expand() {
        if (children != null) return;
        children = AIPlayer.generateSucessors(state);
    }
 
}
