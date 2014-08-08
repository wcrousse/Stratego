
package stratego2;

import java.util.logging.Level;
import java.util.logging.Logger;
import stratego2.model.Color;
import stratego2.model.Game;
import stratego2.model.Player.AI.DefaultPlayer;
import stratego2.model.Player.HumanPlayer;
import stratego2.model.Player.Player;
import stratego2.model.StrategoRules;
import stratego2.view.TextDisplay;
import stratego2.view.gui.GameFrame;
/**
 *
 * @author roussew
 */
public class Stratego2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Player bluePlayer = new HumanPlayer(Color.BLUE, new GameFrame(Color.BLUE));
        Player redPlayer = new DefaultPlayer(Color.RED, new StrategoRules());
        Game game = new Game();
        game.setPlayers(bluePlayer, redPlayer);
        try {
            game.startGame();
        } catch (Exception ex) {
            Logger.getLogger(Stratego2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
