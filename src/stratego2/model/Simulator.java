

package stratego2.model;

import stratego2.model.Game;
import stratego2.model.Player.AI.DefaultPlayer;
import stratego2.model.Player.Player;
import stratego2.model.Player.AI.GameState;
/**
 *
 * @author roussew
 */
public class Simulator extends Game implements Runnable{
    
    private GameState startState;
    
    public Simulator(GameState startState, Player redPlayer, Player bluePlayer) {
        super();
        
    } 

    @Override
    public void run() {
        super.startGame();
    }
    
    @Override
    protected Color declareWinner() {
        Color winningColor = super.declareWinner();
        return winningColor;
    }
}
