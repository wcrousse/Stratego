

package stratego2.model;

/**
 * 
 * @author roussew
 */


public class Move {
    private int startRow;
    private int startColumn;
    private int destinationRow;
    private int destinationColumn;

    /**
     * creates a Move object without assigned coordinates
     */
    public Move() {}
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

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public void setDestinationRow(int destinationRow) {
        this.destinationRow = destinationRow;
    }

    public void setDestinationColumn(int destinationColumn) {
        this.destinationColumn = destinationColumn;
    }
    
    
}
