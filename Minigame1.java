import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.*;

public class Minigame1 {
    private int moves, dir, score;
    private double tilt, timeLeft;
    private SpriteSheet ss;
                                             
    public Minigame1(BufferedImage soupTurn) {
        ss = new SpriteSheet(soupTurn, 128, 128);
        reset();
    }
    public void addTilt(double amt){
        tilt += amt;
        tilt = (int) Game.clamp(tilt, 0, 8);
    }
    public double getTime(){ return timeLeft; }
    public int getScore(){ return score; }
    
    public void reset(){
        tilt = 4.0;
        timeLeft = 702;
        score = 0;
        moves = 0;
        dir = 0;
    }
    
    public void tick() {
        if(moves == 0){
            moves = (int)(Math.round(Math.random()*6));
            dir = (int)(Math.round(Math.random()*2)-1);
        }
        else{
            tilt += dir/5.0;
            moves--;
        }
        timeLeft--;
        score += Math.abs(tilt-4);
        if(Math.floor(timeLeft) <= 0) System.out.println(score);
    }
    
    public void render(Graphics g) {
        BufferedImage temp = ss.grabImage((int) Game.clamp(tilt, 0, 8), 0, 128, 128);
        g.drawImage(temp, 336, 186, null);
        
        g.setColor(Color.RED);
        g.fillRect(49, 50, (int)timeLeft, 20);
    }
}
