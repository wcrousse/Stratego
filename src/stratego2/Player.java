

package stratego2;

import stratego2.model.Piece;

/**
 *
 * @author roussew
 */
public class Player {
    private Piece[] army;
    
    public boolean hasMove() {
        boolean result = false;
        for(Piece piece: army) {
            if(piece.hasMove()){
                result = true;
                break;
            }
        }
        return result;
    }
    
    public boolean isFlagCaptured(){
        
    }
}
