

package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Game;
import stratego2.model.Piece;

/**
 * data-structure represents the game state at any given moment. Game state
 * includes the arrangement of pieces on the board, who is to move. Game state
 * is nigh immutable only the utility can be modified. successors are not 
 * modifications of the current state. They are new states.
 * @author roussew
 */
public class GameState extends Board {
    private final List<Piece>blueArmy;
    private final List<Piece>redArmy;
    /**
     * 0 represents red 1 represents blue. should use constants Game.RED and
     * Game.BLUE
     */
    private final int toMove;
    private double utility;

    public GameState(int toMove, List<Piece> redArmy, List<Piece> blueArmy) {
        super(redArmy, blueArmy);
        this.toMove = toMove;
        this.redArmy = redArmy;
        this.blueArmy = blueArmy;
    }

    
    public GameState() {
        blueArmy = new ArrayList<>();
        redArmy = new ArrayList<>();
        toMove = Game.RED;
    }
    
    public double getUtility() {
        return utility;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }
   
    
}
