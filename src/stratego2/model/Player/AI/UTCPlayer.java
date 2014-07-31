
package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.Game;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Square;
import stratego2.model.StrategoRules;

/**
 *
 * @author roussew
 */
public class UTCPlayer extends AIPlayer {
     
    private ArrayList<EnemyPiece> opposingArmy;

    public UTCPlayer(Color color) {
        this.color = color;
    }

    public UTCPlayer(Color color, StrategoRules rules) {
        super(color, rules);
    }

    @Override
    public Move getMove(Board board) {
        state = new GameState(board, color);
        ArrayList<Action> possibleActions = super.generateSucessors(state);
        return null;
    }

    public void processMove(Move move) {
        Piece piece = movedPiece(move);
        piece = piece.setLocation(move.getDestinationRow(), move.getDestinationColumn());
        state.makeMove(move);
    }

    private Piece movedPiece(Move move) {
        return state.getSquare(move.getStartRow(), move.getStartColumn()).getOccupier();
    }

    @Override
    public void displayBoard(Board board) {
        Move lastMove = extractMove(board);
        //updateBeliefState(move);
    }

    private Move extractMove(Board newState) {
        for (int i = 0; i < Game.NUM_ROWS; i++) {
            for (int j = 0; j < Game.NUM_COLUMNS; j++) {
                Piece oldPiece = null, newPiece = null;
                if (state.getSquare(i, j).isOccupied())  {
                    oldPiece = state.getSquare(i, j).getOccupier();
                if (newState.getSquare(i, j).isOccupied()) {
                    newPiece = newState.getSquare(i, j).getOccupier();
                }
                if (oldPiece != null || newPiece != null) {
                    if ((oldPiece != null && newPiece == null)
                            || (oldPiece == null && newPiece != null) 
                            || (!oldPiece.equals(newPiece))) {
                        
                    }
                    
                }
                if (!oldPiece.equals(newPiece) )
                    if (newState.getSquare(i, j).isOccupied()) {
                        Piece piece2 = newState.getSquare(i, j).getOccupier();
                        if (!oldPiece.equals(piece2)) {
                            // 
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public void reportMove(Move move) {
        Square start = state.getSquare(move.getStartRow(), move.getStartColumn());
            Piece piece = start.getOccupier();
            state.makeMove(move);
    }

    @Override
    public List<Piece> getSetup() {
        return null;

    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void revealSquare(Square square) {
        Piece winner = 
    }

    @Override
    public void reportResult(Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class Location {

        int row;
        int column;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + this.row;
            hash = 17 * hash + this.column;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Location other = (Location) obj;
            if (this.row != other.row) {
                return false;
            }
            if (this.column != other.column) {
                return false;
            }
            return true;
        }

    }

    class EnemyPiece extends Piece{

        ProbabilityDistribution distribution;
   
        public EnemyPiece(Piece original) {
            super(original);
        }
    }
}
