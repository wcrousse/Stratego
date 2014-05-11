

package stratego2.model;

/**
 * 
 * @author roussew
 */


public class Move {
    private final int startRow;
    private final int startColumn;
    private final int destinationRow;
    private final int destinationColumn;

    /**
     * creates Move data structure
     * @param startRow the row index of the piece to be moved
     * @param startColumn the column index of the piece to be moved
     * @param destinationRow the row index of the destination square
     * @param destinationColumn the column index of the destination square
     */
    public Move(int startRow, int startColumn, int destinationRow, int destinationColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

  

    public int getDestinationRow() {
        return destinationRow;
    }

    public int getDestinationColumn() {
        return destinationColumn;
    }
    
    
}
