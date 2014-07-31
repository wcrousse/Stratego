

package stratego2.model.Player.AI;

import stratego2.model.Color;
import stratego2.model.Piece;
import stratego2.model.Rank;

/**
 *
 * @author roussew
 */
public class EnemyPiece extends Piece{
    
    private ProbabilityDistribution distribution;
    
    public EnemyPiece(int row, int column, Color color) {
        super(null, color, row, column);
    }

    EnemyPiece(ProbabilityDistribution distribution, 
            Color color, int row, int column) {
            
        super(null, color, row, column);
        this.distribution = distribution;
    }
    
    protected void pieceMobileUpdate() {
        double total = 0;
        for (Rank rank: Rank.values()) {
            if (rank != Rank.BOMB && rank != Rank.FLAG) {
                total += distribution.getProb(rank);
            }
            else {
                distribution.setProb(rank, 0);
            }
        }
        
        for (Rank rank: Rank.values()) {
            double p = distribution.getProb(rank) / total;
            distribution.setProb(rank, p);
        }
        distribution.setBombProb(0);
        distribution.setFlagProb(0);
    }
    
    protected void pieceRevealed(Rank rank) {
        double value = rank.getValue();
        for (Rank r: Rank.values()) {
            if (r == rank) {
                distribution.setProb(r, 1);
            }else {
                distribution.setProb(r, 0);
            }
        }
    }
    
}
