import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//Handles key inputs
public class KeyInput extends KeyAdapter{
    
    private Handler handler;
    
    //Constructor
    public KeyInput(Handler handler) {
        this.handler = handler;
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
            }
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
            }
        }
    }
    
}
