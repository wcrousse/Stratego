

package stratego2.model.Player.AI.Simulator;

import stratego2.model.Color;
import stratego2.model.Game;
import stratego2.model.Rank;

/**
 *
 * @author roussew
 */
public class SimPiece implements Comparable<SimPiece>{
        
    private final Color color;
    private final Rank rank;
    private int row, column;
    private boolean isKnown;
    private boolean isAttacker;


    public SimPiece(Color color, Rank rank, boolean isKnown, int row, int column) {
        this.color = color;
        this.rank = rank;
        this.isKnown = isKnown;
        this.row = row;
        this.column = column;
    }

    public Color getColor() {
        return color;
    }

    public Rank getRank() {
        return rank;
    }
    
    public boolean isKnown() {
        return isKnown;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isIsKnown() {
        return isKnown;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setColumn(int column) {
        this.column = column;
    }
    
    public boolean isMobile() {
        return (rank != Rank.BOMB && rank != Rank.FLAG);
    }


    @Override
    public int compareTo(SimPiece o) {
        if(rank == Rank.SPY && o.rank == Rank.MARSHAL ) return 1;
        if(rank == Rank.MINER && o.rank == Rank.BOMB) return 1;
        if(o.rank == Rank.FLAG) return 1;
        else return rank.getValue() - o.rank.getValue();
    }
    
    public String toString() {
        String val = (rank.getValue() > 9)? rank.getValue() +"": rank.getValue() + " ";
        return val + ", " + ((color == Color.BLUE)? "B": "R"); 
    }
    
}
