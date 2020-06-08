import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends MouseAdapter{
    
    private boolean playTutorial = true;
    
    private Button bPlay = new Button(250,200,300,60);
    
    private Handler handler;
    private Game game;
    
    int mx = 0, my = 0;
    
    public Menu(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
    }
    
    public void mousePressed(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        
//        System.out.println(mx+" "+my);
    }
    
    public void mouseReleased(MouseEvent e) {
        if (Game.gameState == State.Menu) {
            if (bPlay.mouseOver(mx, my)) {
                game.startLevel();
                if (playTutorial) {
                    playTutorial = false;
                    Game.gameState = State.Tutorial;
                }
                else Game.gameState = State.Game;
            }
        }
    }
    
    public void tick() {
        
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(250, 200, 300, 60);
        g.drawRect(250, 280, 300, 60);
        g.drawRect(250, 360, 300, 60);
    }
}
