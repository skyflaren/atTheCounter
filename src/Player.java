import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends GameObject{
    
    private boolean[] keyDown = new boolean[4]; //manages key input internally so that movement is attempted each tick
    private BufferedImage characterSprites;
    private BufferedImage playerImage;
    private SpriteSheet ss;
    private HUD hud;
    private int dir = 0, animCycle = 0, mTick = 0, bTick = 0, resistance = 1, powerupTimer = 0;
    private boolean kd = false;

    //Constructor
    public Player(int x, int y, int w, int h, int xdispl, int ydispl, BufferedImage img, HUD hud, ID id, Handler handler) {
        super(x, y, w, h, xdispl, ydispl, id, handler);
        this.hud = hud;
        ss = new SpriteSheet(img, 128, 128);
        
        playerImage = ss.grabImage(0, 0, 128, 128);
    }
    
    public void tick() {
        
//        if (Game.status.equals("static") && x < 200) {
//            Game.status = "scroll";
//        }
        
        //Processing key input
        if (keyDown[0]) { this.setVelY(-5); kd = true;}
        if (keyDown[1]) { this.setVelY(5); kd = true;}
        if (keyDown[2]) { this.setVelX(-5); kd = true;}
        if (keyDown[3]) { this.setVelX(5); kd = true;}
        
        //Prevents delay when going in opposite directions
        if (!keyDown[0] && !keyDown[1]) this.setVelY(0);
        if (!keyDown[2] && !keyDown[3]) this.setVelX(0);
        
        boolean mU = true, mD = true, mL = true, mR = true;
        
        //Handle object collision for solid objects
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject temp = handler.object.get(i);
            if (temp.getId() == ID.CollidableObject) {
                boolean[] scenarios = temp.intersect(this, velX, velY);
                if(scenarios[0]) {
                    if (!scenarios[1] && !scenarios[2]) {
                        if (velY > 0) mD = false;
                        if (velY < 0) mU = false;
                    }
                    else if (!scenarios[1]) {
                        if (velX > 0) mR = false;
                        if (velX < 0) mL = false;
                    }
                    else if (!scenarios[2]) {
                        if (velY > 0) mD = false;
                        if (velY < 0) mU = false;
                    }
                }
                if(scenarios[1]) {
                    if (velY > 0) mD = false;
                    if (velY < 0) mU = false;
                }
                if(scenarios[2]) {
                    if (velX > 0) mR = false;
                    if (velX < 0) mL = false;
                }
            }
        }
        
        //Handle object collisions for scrolling objects
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject temp = handler.object.get(i);
            if (temp.getId() == ID.NonCollidableObject) {
                if (this.intersect(temp)) {
                    Object obj = (Object) temp; // we can assume this won't raise errors because the object was marked with an ID
                    if (obj.getEffect() == ObjectID.Crowd) {
                        hud.incrementRisk(4/resistance);
                    }
                    if (obj.getEffect() == ObjectID.Garbage) {
                        hud.incrementRisk(2/resistance);
                    }
                    if (obj.getEffect() == ObjectID.Mask) {
                        resistance = 2;
                        powerupTimer = 1200;
                        handler.removeObject(temp);
                    }
                    if (obj.getEffect() == ObjectID.Sanitizer) {
                        hud.incrementRisk(-100);
                        handler.removeObject(temp);
                    }
                }
            }
        }
        
        if (powerupTimer > 0) {
            powerupTimer--;
        }
        
        if (powerupTimer == 0) {
            resistance = 1;
        }
        
        hud.tick();
        
        //Restricts movement if there is a solid object
        if (!mU) velY = Math.max(0, velY);
        if (!mD) velY = Math.min(0, velY);
        if (!mL) velX = Math.max(0, velX);
        if (!mR) velX = Math.min(0, velX);
        
        //Animation processing
        if (velX != 0) {
            x += velX;
            if (velX > 0) dir = 2;
            if (velX < 0) dir = 1;
        }
        else {
            y += velY;
            if (velY > 0) dir = 0;
            if (velY < 0) dir = 3;
        }
        
        //Bound the character in the screen
        x = Game.clamp(x, 0, 800-w);
        y = Game.clamp(y, 200, 500-h);
        
        //Animation handling
        if (mTick == 5) {
            mTick = 0;
            bTick++;
            if (bTick == 2) {
                bTick = 0;
                playerImage = ss.grabImage(dir, animCycle, 128, 128);
                if (kd) {
                    animCycle++;
                    animCycle %= 4;
                }
                else {
                    if (animCycle % 2 == 1)
                        animCycle++;
                    animCycle %= 4;
                }
            }
            kd = false;
        }
        mTick++;
    }

    public void render(Graphics g) {
        System.out.println(x+" "+y);
//        g.setColor(Color.white);
//        g.fillRect(x, y, 10, 10);
        g.drawImage(playerImage, x+xdispl, y+ydispl, null);
        hud.render(g);
    }
    
    //Helper methods
    public void setKeyDown(int ind, boolean status) {
        keyDown[ind] = status;
    }
    
    public boolean getKeyDown(int ind) {
        return keyDown[ind];
    }

}
