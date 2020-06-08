import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends GameObject{
    
    private boolean[] keyDown = new boolean[4]; //manages key input internally so that movement is attempted each tick
    private boolean spaceDown = false;
    private BufferedImage characterSprites;
    private BufferedImage playerImage;
    private SpriteSheet ss;
    private String powerup = "";
    private HUD hud;
    private int dir = 1, animCycle = 0, mTick = 0, bTick = 0, resistance = 1, powerupTimer = 0;
    private int tutStage = 0, tickCount = 0;
    private boolean kd = false, canMove = true;
    
    private BufferedImageLoader loader = new BufferedImageLoader();
    private Manager manager;
    private Game game;

    //Constructor
    public Player(int x, int y, int w, int h, int xdispl, int ydispl, BufferedImage img, HUD hud, ID id, Handler handler, Game game) {
        super(x, y, w, h, xdispl, ydispl, id, handler);
        this.hud = hud;
        this.game = game;
        ss = new SpriteSheet(img, 128, 128);
        manager = new Manager(-128,350,128,12,0,-104, loader.loadImage("/manager_sprite_sheet.png"), this, ID.CollidableObject, handler);
        playerImage = ss.grabImage(dir, 0, 128, 128);
    }
    
    public void tick() {
        
        if (Game.gameState == State.Tutorial) {
            if (tutStage == 0) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    spaceDown = false;
                }
            }
            if (tutStage == 1) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    spaceDown = false;
                    manager.setAnimFinished(false);
                }
            }
            if (tutStage == 2) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    spaceDown = false;
                    canMove = true;
                    new Object(-60, 200, 60, 60, 0, 0, 
                            new SpriteSheet(loader.loadImage("obstacle_sprite_sheet.png"), 60, 60).grabImage(0, 0, 60, 60), 
                            ObjectID.Crowd, ID.NonCollidableObject, handler);
                    new Object(-60, 320, 60, 60, 0, 0, 
                            new SpriteSheet(loader.loadImage("obstacle_sprite_sheet.png"), 60, 60).grabImage(1, 0, 60, 60), 
                            ObjectID.Garbage, ID.NonCollidableObject, handler);
                    new Object(-56, 380, 60, 60, 0, 0, 
                            new SpriteSheet(loader.loadImage("obstacle_sprite_sheet.png"), 60, 60).grabImage(0, 0, 60, 60), 
                            ObjectID.Crowd, ID.NonCollidableObject, handler);
                }
            }
            if (tutStage == 3) {
                tickCount++;
                if (tickCount > 240) {
                    tickCount = 0;
                    tutStage++;
                    canMove = false;
                    velX = 0;
                    velY = 0;
                    dir = 1;
                    manager.setAnimFinished(false);
                }
            }
            if (tutStage == 4) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    spaceDown = false;
                }
            }
            if (tutStage == 5) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    spaceDown = false;
                    canMove = true;
                    new Object(-60, 200, 60, 60, 0, 0, 
                            new SpriteSheet(loader.loadImage("powerup_sprite_sheet.png"), 60, 60).grabImage(0, 0, 60, 60), 
                            ObjectID.Mask, ID.NonCollidableObject, handler);
                    new Object(-60, 320, 60, 60, 0, 0, 
                            new SpriteSheet(loader.loadImage("powerup_sprite_sheet.png"), 60, 60).grabImage(1, 0, 60, 60), 
                            ObjectID.Sanitizer, ID.NonCollidableObject, handler);
                    new Object(-60, 440, 60, 60, 0, 0, 
                            new SpriteSheet(loader.loadImage("powerup_sprite_sheet.png"), 60, 60).grabImage(2, 0, 60, 60), 
                            ObjectID.Gloves, ID.NonCollidableObject, handler);
                }
            }
            if (tutStage == 6) {
                tickCount++;
                if (tickCount > 240) {
                    tickCount = 0;
                    tutStage++;
                    canMove = false;
                    velX = 0;
                    velY = 0;
                    dir = 1;
                    manager.setAnimFinished(false);
                }
            }
            if (tutStage == 7) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    spaceDown = false;
                }
            }
            if (tutStage == 8) {
                canMove = false;
                if (spaceDown && manager.getAnimFinished()) {
                    tutStage++;
                    Game.gameState = State.Game;
                    canMove = true;
                    spaceDown = false;
                }
            }
            manager.tick();
        }
        
        //Processing key input
        if (canMove) {
            if (keyDown[0]) { this.setVelY(-5); kd = true;}
            if (keyDown[1]) { this.setVelY(5); kd = true;}
            if (keyDown[2]) { this.setVelX(-5); kd = true;}
            if (keyDown[3]) { this.setVelX(5); kd = true;}
        }
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
                    if (obj.getEffect() == ObjectID.Mask || obj.getEffect() == ObjectID.Gloves) {
                        powerup = "mask";
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
            if (powerup.equals("mask")) {
                resistance = 1;
                powerup = "";
            }
        }
        
        if (Game.gameState != State.Tutorial) hud.tick();
        
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
        
        if (hud.getRisk() == 1000) {
            Game.gameState = State.Menu;
            handler.object.clear();
        }
    }

    public void render(Graphics g) {
//        System.out.println(x+" "+y);
//        g.setColor(Color.white);
//        g.fillRect(x, y, 10, 10);
        g.drawImage(playerImage, x+xdispl, y+ydispl, null);
        if (Game.gameState == State.Tutorial) manager.render(g);
        if (Game.gameState != State.Tutorial) hud.render(g);
    }
    
    //Helper methods
    public void setKeyDown(int ind, boolean status) {
        keyDown[ind] = status;
    }
    
    public boolean getKeyDown(int ind) {
        return keyDown[ind];
    }
    
    public void setSpaceDown(boolean status) {
        spaceDown = status;
    }
    
    public boolean getSpaceDown() {
        return spaceDown;
    }
    
    public int getTutStage() {
        return tutStage;
    }

}
