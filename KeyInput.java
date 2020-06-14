import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//Handles key inputs
public class KeyInput extends KeyAdapter{
    
    private Handler handler;
    private Minigame1 mg1;
    private Minigame2 mg2;
    
    //Constructor
    public KeyInput(Handler handler) {
        this.handler = handler;
    }
    
    public KeyInput(Handler handler, Minigame1 mg1, Minigame2 mg2) {
        this.handler = handler;
        this.mg1 = mg1;
        this.mg2 = mg2;
    }

    //Listens for key input
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject temp = handler.object.get(i);
            
            //Sets variables inside the player object so that calculations can be handled locally
            if (temp.getId() == ID.Player) {
                Player player = (Player) temp;
                if (key == KeyEvent.VK_W) player.setKeyDown(0, true);
                if (key == KeyEvent.VK_S) player.setKeyDown(1, true);
                if (key == KeyEvent.VK_A) player.setKeyDown(2, true);
                if (key == KeyEvent.VK_D) player.setKeyDown(3, true);
                if (key == KeyEvent.VK_SPACE) player.setSpaceDown(true);
            }
        }
        
        if ((Game.gameState == State.Minigame1 && Game.startOne == false) || (Game.gameState == State.Minigame2 && Game.startTwo == false) || Game.gameState == State.Info || Game.gameState == State.Credits) {
            if (key == KeyEvent.VK_B) {
                Game.gameState = State.Lobby;
            }
        }
        
        if (Game.gameState == State.Minigame1){
            if (key == KeyEvent.VK_SPACE) Game.startOne = true;
            
            if (key == KeyEvent.VK_A) mg1.addTilt(1.1);
            if (key == KeyEvent.VK_D) mg1.addTilt(-1.1);
        }
        
        if(Game.gameState == State.Minigame2){
            if (key == KeyEvent.VK_SPACE) Game.startTwo = true;
            
            if(key == KeyEvent.VK_Q) mg2.clean(0, 0);
            if(key == KeyEvent.VK_W) mg2.clean(0, 1);
            if(key == KeyEvent.VK_E) mg2.clean(0, 2);
            
            if(key == KeyEvent.VK_A) mg2.clean(1, 0);
            if(key == KeyEvent.VK_S) mg2.clean(1, 1);
            if(key == KeyEvent.VK_D) mg2.clean(1, 2);
            
            if(key == KeyEvent.VK_Z) mg2.clean(2, 0);
            if(key == KeyEvent.VK_X) mg2.clean(2, 1);
            if(key == KeyEvent.VK_C) mg2.clean(2, 2);
        }
    }
    
    //Listens for key release
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject temp = handler.object.get(i);
            
          //Sets variables inside the player object so that calculations can be handled locally
            if (temp.getId() == ID.Player) {
                Player player = (Player) temp;
                if (key == KeyEvent.VK_W) player.setKeyDown(0, false);
                if (key == KeyEvent.VK_S) player.setKeyDown(1, false);
                if (key == KeyEvent.VK_A) player.setKeyDown(2, false);
                if (key == KeyEvent.VK_D) player.setKeyDown(3, false);
                if (key == KeyEvent.VK_SPACE) player.setSpaceDown(false);
            }
        }
    }
    
}
