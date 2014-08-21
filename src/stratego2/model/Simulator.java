

package stratego2.model;

import java.util.ArrayList;
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
    
    private final GameState startState;
    private Color winColor;
    
    public Simulator(GameState startState, Player bluePlayer, Player redPlayer) {
        super(startState, bluePlayer, redPlayer);
        this.startState = startState;
    } 

    @Override
    public void run() {
        extractArmies();
        System.out.println(this);
        try {
            winColor = super.play();
        } catch (Exception ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void extractArmies() {
        blueArmy = new ArrayList<FriendlyPiece>();
        redArmy = new ArrayList<FriendlyPiece>();
        
        for(Square s: startState) {
            if(s.isOccupied()) {
                FriendlyPiece p = (FriendlyPiece)s.getOccupier();
                if(p.getColor() == Color.BLUE) blueArmy.add(p);
                else redArmy.add(p);
            }
        }
    }
    @Override
    protected Color declareWinner() {
        winColor = super.declareWinner();
        return winColor;
    }

    public Color getResult() {
        return winColor;
    } 
    

    
}
