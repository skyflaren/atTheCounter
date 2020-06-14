import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends MouseAdapter{
    
    private boolean playTutorial = true;
    private int score = 0;
    
    private Button bPlay = new Button(250,280,300,60),
            bQuit = new Button(250,360,300,60),
            bContinue = new Button(40,410,300,60),
            bLeave = new Button(460,410,300,60);
    
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
                else {
                    game.loadLobby();
                    Game.gameState = State.Lobby;
                }
            }
            if (bQuit.mouseOver(mx, my)) {
                game.close();
            }
        }
        
        if (Game.gameState == State.GameOver) {
            if (bContinue.mouseOver(mx, my)) {
                game.startLevel();
                game.loadLobby();
                Game.gameState = State.Lobby;
            }
            if (bLeave.mouseOver(mx, my)) {
                Game.gameState = State.Menu;
            }
        }
    }
    
    public void tick() {
        
    }

    public void render(Graphics g) {
        if (Game.gameState == State.GameOver) {
            g.setColor(Color.white);
            g.setFont(new Font("8BIT WONDER", Font.PLAIN, 40));
            g.drawString("SCORE",150,335);
            g.drawString(String.format("%05d", score), 450, 335);
        }
    }
    
    public void setScore(int score) {
        this.score = score;
    }
}
