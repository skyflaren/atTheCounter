import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.*;

public class Minigame2 {
    private boolean arr[][];
    private int score, timeLeft;
    private SpriteSheet mopTiles;
                                             
    public Minigame2(BufferedImage mt) {
        mopTiles = new SpriteSheet(mt, 150, 150);
        arr = new boolean [3][3];
        reset();
    }
    
    public void reset(){
        for(int c = 0; c < 3; c++){
            for(int r = 0; r < 3; r++){
                arr[r][c] = false;
            }
        }
        timeLeft = 700;
        score = 0;
    }
    
    public int getScore(){
        return score;
    }
    
    public int getTime(){
        return timeLeft;
    }
    
    public void clean(int row, int col){
        arr[row][col] = false;
    }
    
    public void tick() {
        for(int c = 0; c < 3; c++){
            for(int r = 0; r < 3; r++){
                if(arr[r][c] == false){
                    if((int)(Math.random()*300) == 1) arr[r][c] = true;
                }
                else{
                    score++;
                }
            }
        }
        timeLeft--;
        if(Math.floor(timeLeft) <= 0) System.out.println(score);
    }
    
    public void render(Graphics g) {
        for(int c = 0; c < 3; c++){
            for(int r = 0; r < 3; r++){
                BufferedImage temp = mopTiles.grabImage(r*3+c, (arr[r][c] == true ? 0 : 1), 150, 150);
                g.drawImage(temp, 175 + 150*c, 40 + 150*r, null);
            }
        }
        g.setColor(Color.RED);
        g.fillRect(49, 10, (int)timeLeft, 20);
    }
}
