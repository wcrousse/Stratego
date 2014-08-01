

package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.FileParser;
import stratego2.model.FriendlyPiece;
import stratego2.model.GameState;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Rank;
import stratego2.model.Square;
import stratego2.model.StartPossitions;
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
        ArrayList<MCSTNode> possibleActions = super.generateSucessors(state);
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
        int[][] simpleLayout = startupFromFile(1);
        for (int i = 0; i < simpleLayout.length; i++) {
            for (int j = 0; j < simpleLayout[i].length; j++) {
                int value = simpleLayout[i][j];
                Rank rank = Rank.getRank(value);
                FriendlyPiece p;
                if (this.color == Color.BLUE) {
                    p = new FriendlyPiece(color, i, j, rank);
                } else {
                    p = new FriendlyPiece(color, 9-i, 9-j, rank);
                }

                army.add(p);
            }
        }
        return army;
    }
    
    private int[][] startupFromFile(int setUpNum) {
        
        FileParser parser = new FileParser();
            String fileName;
            switch (setUpNum) {
                case 0:
                    fileName = StartPossitions.FLAG_LEFT1.fileName();
                    break;
                case 1:
                    fileName = StartPossitions.FLAG_RIGHT1.fileName();
                    break;
                case 2:
                    fileName = StartPossitions.FLAG_MIDDLE.fileName();
                    break;
                case 3:
                    fileName = StartPossitions.FLAG_LEFT2.fileName();
                    break;
                case 4:
                    fileName = StartPossitions.FLAG_RIGHT2.fileName();
                    break;
                default:
                    //appease the compiler
                    fileName = null;
            }

            return parser.readPositionFile(fileName);
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
        state.makeMove(move);
    }
}
