import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends GameObject{
    
    private boolean[] keyDown = new boolean[4]; //manages key input internally so that movement is attempted each tick
    private boolean spaceDown = false;
    private BufferedImage playerImage, indicator;
    private SpriteSheet ss, ss2, prompts;
    private SpriteSheet[] tut = new SpriteSheet[2];
    private String powerup = "";
    private HUD hud;
    private int dir = 1, animCycle = 0, mTick = 0, bTick = 0, resistance = 1, powerupTimer = 0, gender = 0;
    private int tutStage = 0, tickCount = 0, displayIndicator = 0;
    private boolean kd = false, canMove = true, usedMat = false;
    
    private BufferedImageLoader loader = new BufferedImageLoader();
    private Manager manager;
    private Game game;

    //Constructor
    public Player(int x, int y, int w, int h, int xdispl, int ydispl, BufferedImage img, BufferedImage img2, HUD hud, ID id, Handler handler, Game game) {
        super(x, y, w, h, xdispl, ydispl, id, handler);
        this.hud = hud;
        this.game = game;
        
        
        
        ss = new SpriteSheet(img, 128, 128);
        ss2 = new SpriteSheet(img2, 128, 128);
        manager = new Manager(-128,350,128,12,0,-104, loader.loadImage("/manager_sprite_sheet.png"), this, ID.CollidableObject, handler);
        indicator = loader.loadImage("/indicator.png");
        prompts = new SpriteSheet(loader.loadImage("/prompts.png"), 15, 200);
        
        playerImage = ss.grabImage(dir, 0, 128, 128);
        tut[0] = new SpriteSheet(loader.loadImage("obstacle_sprite_sheet.png"),60,60);
        tut[1] = new SpriteSheet(loader.loadImage("powerup_sprite_sheet.png"),60,60);
    }
    
    public void tick() {
        
        if (Game.gameState == State.Tutorial) {
            boolean animFinished = manager.getAnimFinished();
//            boolean animFinished = true;
            if (tutStage == 0) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    spaceDown = false;
                }
            }
            else if (tutStage == 1) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    spaceDown = false;
                    manager.setAnimFinished(false);
                }
            }
            else if (tutStage == 2) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    spaceDown = false;
                    canMove = true;
                    new Object(-60, 200, 60, 60, 0, 0, 
                            tut[0].grabImage(0, 0, 60, 60), 
                            ObjectID.Crowd, ID.NonCollidableObject, handler);
                    new Object(-60, 320, 60, 60, 0, 0, 
                            tut[0].grabImage(1, 0, 60, 60), 
                            ObjectID.Garbage, ID.NonCollidableObject, handler);
                    new Object(-56, 380, 60, 60, 0, 0, 
                            tut[0].grabImage(0, 0, 60, 60), 
                            ObjectID.Crowd, ID.NonCollidableObject, handler);
                }
            }
            else if (tutStage == 3) {
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
            else if (tutStage == 4) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    spaceDown = false;
                }
            }
            else if (tutStage == 5) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    spaceDown = false;
                    canMove = true;
                    new Object(-60, 200, 60, 60, 0, 0, 
                            tut[1].grabImage(0, 0, 60, 60), 
                            ObjectID.Mask, ID.NonCollidableObject, handler);
                    new Object(-60, 320, 60, 60, 0, 0, 
                            tut[1].grabImage(1, 0, 60, 60), 
                            ObjectID.Sanitizer, ID.NonCollidableObject, handler);
                    new Object(-60, 380, 60, 60, 0, 0, 
                            tut[1].grabImage(2, 0, 60, 60), 
                            ObjectID.Gloves, ID.NonCollidableObject, handler);
                }
            }
            else if (tutStage == 6) {
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
            else if (tutStage == 7) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    spaceDown = false;
                }
            }
            else if (tutStage == 8) {
                canMove = false;
                if (spaceDown && animFinished) {
                    tutStage++;
                    x = 400; y = 350;
                    Game.gameState = State.Lobby;
                    game.loadLobby();
                    canMove = true;
                    spaceDown = false;
                }
            }
            manager.tick();
        }
        
        if (Game.gameState == State.Lobby) {
            if (x < 20) {
                
            }
        }
        
        //Processing key input
        if (canMove) {
            if (keyDown[0]) { this.setVelY(-5); dir = 3; kd = true;}
            if (keyDown[1]) { this.setVelY(5); dir = 0; kd = true;}
            if (keyDown[2]) { this.setVelX(-5); dir = 1;kd = true;}
            if (keyDown[3]) { this.setVelX(5); dir = 2; kd = true;}
        }
        //Prevents delay when going in opposite directions
        if (!keyDown[0] && !keyDown[1]) this.setVelY(0);
        if (!keyDown[2] && !keyDown[3]) this.setVelX(0);
        
        boolean mU = true, mD = true, mL = true, mR = true;
        
        //Handle object collision for solid objects
        displayIndicator = 0;
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
                
                boolean[] interact;
                if (dir == 0) {
                    interact = temp.intersect(this, 0, 5);
                }
                else if (dir == 1) {
                    interact = temp.intersect(this, -5, 0);
                }
                else if (dir == 2) {
                    interact = temp.intersect(this, 5, 0);
                }
                else {
                    interact = temp.intersect(this, 0,-5);
                }
                if ((interact[1] || interact[2]) && spaceDown) {
                    if (((Object) temp).getEffect() == ObjectID.Mini1){
                        displayIndicator = 0;
                        Game.gameState = State.Minigame1;
                    }
                    else if (((Object) temp).getEffect() == ObjectID.Mini2)
                        Game.gameState = State.Minigame2;
                    else if (((Object) temp).getEffect() == ObjectID.Info)
                        Game.gameState = State.Info;
                    else if (((Object) temp).getEffect() == ObjectID.Credits)
                        Game.gameState = State.Credits;
                }
                else if(interact[1] || interact[2]){
                    if (((Object) temp).getEffect() == ObjectID.Mini1)
                        displayIndicator = 1;
                    else if (((Object) temp).getEffect() == ObjectID.Mini2)
                        displayIndicator = 1;
                    else if (((Object) temp).getEffect() == ObjectID.Info)
                        displayIndicator = 3;
                    else if (((Object) temp).getEffect() == ObjectID.Credits)
                        displayIndicator = 4;
                }
            }
        }
        
        //Handle object collisions for scrolling objects
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject temp = handler.object.get(i);
            if (temp.getId() == ID.NonCollidableObject) {
                Object obj = (Object) temp; // we can assume this won't raise errors because the object was marked with an ID
                if (this.intersect(temp)) {
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
                        hud.incrementScore(20.0f);
                        handler.removeObject(temp);
                    }
                    if (obj.getEffect() == ObjectID.Sanitizer) {
                        hud.incrementRisk(-100);
                        handler.removeObject(temp);
                    }
                    if (obj.getEffect() == ObjectID.StartMat) {
                        for (int j = 0; j < handler.object.size(); j++) {
                            GameObject go = handler.object.get(j);
                            if (go.getId() != ID.Player) {
                                handler.object.remove(j);
                                j--;
                            }
                        }
                        x = 650; y = 350;
                        Game.gameState = State.Game;
                    }
                    if (obj.getEffect() == ObjectID.ExitMat) {
                        handler.object.clear();
                        Game.gameState = State.Menu;
                    }
                }
                if (obj.getEffect() == ObjectID.WashroomMat) {
                    if (this.intersect(temp)) {
                        if (spaceDown && !usedMat) {
                            gender ^= 1;
                            usedMat = true;
                        }
                        else if(!usedMat)
                            displayIndicator = 2;
                    }
                    else {
                        usedMat = false;
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
        y = Game.clamp(y, 200, 490-h);
        
        //Animation handling
        if (mTick == 5) {
            mTick = 0;
            bTick++;
            if (bTick == 2) {
                bTick = 0;
                hud.incrementScore(0.2f);
                if (gender == 0) playerImage = ss.grabImage(dir, animCycle, 128, 128);
                else playerImage = ss2.grabImage(dir, animCycle, 128, 128);
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
            Game.gameState = State.GameOver;
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
        if (displayIndicator > 0) g.drawImage(prompts.grabImage(displayIndicator-1, 0, 200, 15), 8 , 460, null);
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
    
    public int getScore() {
        return (int) hud.getScore();
    }
    
    public void incrementRisk(int val){
        hud.incrementRisk(val);
    }

}
