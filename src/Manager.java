import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Manager extends GameObject{
    
    private BufferedImage managerImage;
    private SpriteSheet ss;
    private SpriteSheet dialog;
    
    private BufferedImageLoader loader = new BufferedImageLoader();
    
    private boolean animFinished = true, kd = false;
    
    private int dir = 2, animCycle = 0, mTick = 0, bTick = 0, dialogNum = 0;
    private Player player;


    //Constructor
    public Manager(int x, int y, int w, int h, int xdispl, int ydispl, BufferedImage img, Player player, ID id, Handler handler) {
        super(x, y, w, h, xdispl, ydispl, id, handler);
        handler.removeObject(this);
        this.player = player;
        ss = new SpriteSheet(img, 128, 128);
        dialog = new SpriteSheet(loader.loadImage("dialog.png"),100,800);
        this.player = player;
        managerImage = ss.grabImage(dir, 0, 128, 128);
    }
    
    public void tick() {
        if (Game.gameState == State.Tutorial) {
            if (player.getTutStage() == 0) {
                if (x < 128) {
                    animFinished = false;
                    velX = 2;
                    kd = true;
                }
                else {
                    velX = 0;
                    kd = false;
                    animFinished = true;
                }
            }
            
            if (player.getTutStage() == 2) {
                if (x > -128) {
                    velX = -2;
                    kd = true;
                    animFinished = false;
                }
                else {
                    velX = 0;
                    kd = false;
                    animFinished = true;
                }
            }
            
            if (player.getTutStage() == 4) {
                if (x < 128) {
                    animFinished = false;
                    velX = 2;
                    kd = true;
                }
                else {
                    velX = 0;
                    kd = false;
                    animFinished = true;
                }
            }
            
            if (player.getTutStage() == 5) {
                if (x > -128) {
                    velX = -2;
                    kd = true;
                    animFinished = false;
                }
                else {
                    velX = 0;
                    kd = false;
                    animFinished = true;
                }
            }
            
            if (player.getTutStage() == 7) {
                if (x < 128) {
                    animFinished = false;
                    velX = 2;
                    kd = true;
                }
                else {
                    velX = 0;
                    kd = false;
                    animFinished = true;
                }
            }
            
            if (player.getTutStage() == 8) {
                if (x > -128) {
                    velX = -2;
                    kd = true;
                    animFinished = false;
                }
                else {
                    velX = 0;
                    kd = false;
                    animFinished = true;
                }
            }
            
            
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
            
            //Animation handling
            if (mTick == 5) {
                mTick = 0;
                bTick++;
                if (bTick == 2) {
                    bTick = 0;
                    managerImage = ss.grabImage(dir, animCycle, 128, 128);
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
    }

    public void render(Graphics g) {
        g.drawImage(managerImage, x+xdispl, y+ydispl, null);
        int tutStage = player.getTutStage();
        if (tutStage < 3) {
            dialogNum = tutStage;
        }
        else if (tutStage < 6) {
            dialogNum = tutStage-1;
        }
        else{
            dialogNum = tutStage-2;
        }
        if (tutStage != 3 && tutStage != 6 && dialogNum < 7) {
            g.drawImage(dialog.grabImage(dialogNum, 0, 800, 100), 0, 400, null);
        }
    }

    public boolean getAnimFinished() {
        return animFinished;
    }
    
    public void setAnimFinished(boolean status) {
        animFinished = status;
    }
}

