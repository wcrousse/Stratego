

package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Color;
import stratego2.model.FriendlyPiece;
import stratego2.model.GameState;
import stratego2.model.HumanEnemyPiece;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Square;
import stratego2.model.StrategoRules;

/**
 * implements the default policy. Works as a fully functioning simple reflex AI 
 * currently plays randomly.
 * @author roussew
 */
public class DefaultPlayer extends AIPlayer { 
    protected int result;
    protected GameState startState;
    
    public DefaultPlayer(Color color, StrategoRules rules) {
        super(color, rules);
    }
    
    public DefaultPlayer(Color color, StrategoRules rules, GameState startState) {
        this(color, rules);
        this.startState = startState;
    }

    @Override
    public Move getMove() {
        ArrayList<MCSTNode> possibleActions = AIPlayer.generateSucessors(state);
        int moveIndex = (int)(Math.random()*possibleActions.size());
        MCSTNode action = possibleActions.get(moveIndex);
        return action.getMove();
    }
    
    @Override
    public void reportResult(Color color) {
        result = (color == this.color)? 1: -1;
        startState.setUtility(result);
    }

    @Override
    public List<FriendlyPiece> getSetup() {
        List<FriendlyPiece> friendlyArmy = super.getSetup();
        ArrayList<HumanEnemyPiece>enemyArmy = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            for(int j=0; j<10; j++) {
                
                HumanEnemyPiece p = 
                        (color == Color.BLUE)? 
                            new HumanEnemyPiece(Color.RED, 9-i, 9-j):
                            new HumanEnemyPiece(Color.BLUE, i, j);
                enemyArmy.add(p);
            }
        }
        List<? extends Piece>redArmy;
        List<? extends Piece>blueArmy;
        if(color == Color.BLUE) {
            redArmy = enemyArmy;
            blueArmy = friendlyArmy;
        } else {
            redArmy = friendlyArmy;
            blueArmy = enemyArmy;
        }
        state = new GameState(Color.BLUE, blueArmy, redArmy);
        return friendlyArmy;
    }
    /**
     * reports the result of following the default policy from the starting position
     * @return the utility of the starting state.
     */
    public double getStartStateUtility() {
        return startState.getUtility();
    }

    @Override
    public void reportMove(Move move) {
        Square start = state.getSquare(move.getStartRow(), move.getStartColumn());
        Piece piece = start.getOccupier();
        state = state.makeMove(move);
    }
}
