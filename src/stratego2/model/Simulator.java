

package stratego2.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import stratego2.model.Game;
import stratego2.model.Player.AI.DefaultPlayer;
import stratego2.model.Player.Player;
/**
 *
 * @author roussew
 */
public class Simulator extends Game implements Runnable{
    
    private GameState startState;
    private Color winColor;
    
    public Simulator(GameState startState, Player redPlayer, Player bluePlayer) {
        super();
        
    } 

    @Override
    public void run() {
        try {
            super.startGame();
        } catch (Exception ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected Color declareWinner() {
        winColor = super.declareWinner();
        return winColor;
    }

    public Color getWinColor() {
        return winColor;
    }
    
    
}
