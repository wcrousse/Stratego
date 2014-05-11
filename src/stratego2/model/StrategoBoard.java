

package stratego2.model;

/**
 *
 * @author roussew
 */
public class StrategoBoard {
    private Square[][] board;
    
    public Square[][] getBoard(Color color) {
        Square[][] copy = new Square[board.length][board[0].length];
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                copy[i][j] = board[i][j].copy();
            }
        }
        return copy;
    }
}
