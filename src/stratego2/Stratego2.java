/*
 */

package stratego2;

import stratego2.model.Color;
import stratego2.model.Game;
import stratego2.model.Player.HumanPlayer;
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
        
        HumanPlayer bluePlayer = new HumanPlayer(Color.BLUE, new GameFrame(Color.BLUE));
        HumanPlayer redPlayer = new HumanPlayer(Color.RED, new GameFrame(Color.RED));
        Game game = new Game();
        game.setPlayers(bluePlayer, redPlayer);
        game.play();
        
    }
    
}
