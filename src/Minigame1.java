import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.*;

public class Minigame1 {
    private int moves, dir;
    private double tilt;
    private SpriteSheet ss;
                                             
    public Minigame1(BufferedImage soupTurn) {
        ss = new SpriteSheet(soupTurn, 128, 128);
        tilt = 4.0;
    }
    public void addTilt(double amt){
        tilt += amt;
        tilt = Math.max(Math.min(8, tilt), 0);
    }
    
    public void tick() {
        if(moves == 0){
            moves = (int)(Math.round(Math.random()*3));
            dir = (int)(Math.round(Math.random()*2)-1);
        }
        else{
            tilt += dir/3.0;
            moves--;
        }
        
    }
    public void render(Graphics g) {
        BufferedImage temp = ss.grabImage((int)Math.max(Math.min(8, tilt), 0), 0, 128, 128);
        g.drawImage(temp, 0, 0, null);
    }
}
