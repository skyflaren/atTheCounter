import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HUD {
    
    private int risk;
    private float score;
    
    private BufferedImageLoader loader = new BufferedImageLoader();
    private BufferedImage riskBar, pointer;
    
    //Constructor
    public HUD() {
        riskBar = loader.loadImage("/risk_bar.png");
        pointer = loader.loadImage("/pointer.png");
        risk = 500;
        score = 0;
    }
    
    //Helper methods
    public void setRisk(int risk) {
        this.risk = risk;
    }
    
    public int getRisk() {
        return risk;
    }
    
    public void incrementRisk(int inc) {
        if (Game.gameState == State.Lobby) risk = Game.clamp(risk+inc, 0, 999);
        else if (Game.gameState != State.Tutorial) risk += inc;
    }
    
    public void incrementScore(float inc) {
        if (Game.gameState == State.Game) score += inc;
    }
    
    public float getScore() {
        return score;
    }

    public void tick() {
        risk = Game.clamp(risk, 0, 1000);
        Game.objectSpeed = 4 + (int)(score/50);

        
//        score = 1001;
    }
    
    //Display for the character's risk level
    public void render(Graphics g) {
        if (Game.gameState == State.Lobby) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0,0,0,150));
            g2d.fillRoundRect(5, 5, 265, 60, 20, 20);
            g.drawImage(riskBar, 20, 20, null);
            g.drawImage(pointer, 25 + risk/5, 29, null);
        }
        else {
            g.drawImage(riskBar, 15, 15, null);
            g.drawImage(pointer, 20 + risk/5, 24, null);
        }
        if (Game.gameState == State.Game) {
            g.setColor(Color.white);
            g.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
            g.drawString(String.format("%05d", (int) score), 630, 45);
        }
    }
}
