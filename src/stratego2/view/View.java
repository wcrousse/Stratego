

package stratego2.view;

import stratego2.model.Game;
import stratego2.model.Move;
import stratego2.model.Player;
import stratego2.model.Square;
import stratego2.model.StrategoBoard;

/**
 * perhaps unnecessary. consider combining with display interface. 
 * @author roussew
 */
public class View {
    private Display display;
    private Game control;
    private Player player;
    
    public void display(Square[][] board) {
        //display.displayBoard(board.);
    }
    
    public Move getMove() {
        //return display.getMove();
        return null;
    }  
}
