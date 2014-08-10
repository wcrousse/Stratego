

package stratego2.model.Player.AI;

import java.util.ArrayList;
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

    private MCSTNode parent;
    
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
     * the average return of simulations going through this node;
     */
    private double qvalue;
    /**
     * keeps track of the number of times this move/state pair has been executed.
     */
    private int count;
    
    private boolean isLeaf;
    
    private ArrayList<MCSTNode>children;
    
    public MCSTNode(GameState state, Move move){
        children = new ArrayList<>();
        this.state = state;
        this.move = move;
    }
    
    public MCSTNode(GameState root) {
        children = new ArrayList<>();
        state = root;
        move = null;
    }
    
    public MCSTNode(GameState state, MCSTNode parent) {
        children = new ArrayList<>();
        this.state = state;
        this.parent = parent;
        move = null;
    }
    
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    public ArrayList<MCSTNode> getChildren() {
        return children;
    }
    
    public MCSTNode getParent() {
        return parent;
    }

    public double getQvalue() {
        return qvalue;
    }

    public void updateQValue(double result) {
        sum += result;
        qvalue = sum / count;
    }

    public int getCount() {
        return count;
    }

   public void incrementCount() {
       count++;
   }
   
   public boolean isRoot() {
       return (parent != null);
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
        for (MCSTNode child: children) {
            child.parent = this;
        }
    }
 
}
