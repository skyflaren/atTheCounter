import java.awt.Color;
import java.awt.Graphics;

public class HUD {
    
    
    private int risk;
    
    //Constructor
    public HUD() {
        risk = 500;
    }
    
    //Helper methods
    public void setRisk(int risk) {
        this.risk = risk;
    }
    
    public int getRisk() {
        return risk;
    }
    
    public void incrementRisk(int inc) {
        risk += inc;
    }

    public void tick() {
        risk = Game.clamp(risk, 0, 1000);
    }
    
    //Display for the character's risk level
    public void render(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(15, 15, 204, 32);
        g.setColor(Color.green);
        g.fillRect(17, 17, (risk*2)/10, 28);
    }
}
