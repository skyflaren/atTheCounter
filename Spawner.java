import java.awt.image.BufferedImage;
import java.util.*;

public class Spawner {
    
    private Handler handler;
    private SpriteSheet ss;
    private int objectCnt, counter, freq, w, h, maxSpawn, spawnHeight, spawnRange;
    private ObjectID[] effects;
    
    //Constructor
    public Spawner(int cnt, int freq, int maxSpawn, int spawnHeight, int spawnRange, int w, int h, ObjectID[] effects, BufferedImage objects, Handler handler) {
        this.handler = handler;
        this.freq = freq;
        this.w = w;
        this.h = h;
        this.maxSpawn = maxSpawn;
        this.spawnHeight = spawnHeight;
        this.spawnRange = spawnRange;
        this.effects = effects;
        objectCnt = cnt;
        counter = 0;
        
        ss = new SpriteSheet(objects, w, h);
    }
    
    public void tick() {
        counter += (Game.objectSpeed/3);
        if (counter >= freq) {
            int[] lanes = new int[spawnRange]; //Allows only one object to be spawned in each lane
            for (int i = 0; i < maxSpawn; i++) { //Attempts to spawn objects
                int lane = (int)(Math.random()*spawnRange); //Picks the lane
                lanes[lane] = (int)(Math.random()*objectCnt)+1; //Defines what type of object it is
            }
            for (int row = 0; row < spawnRange; row++) {
                if(lanes[row] != 0) { //If there is a valid object in that lane, create the object
                    new Object(-w, spawnHeight+row*h, w, h, 0, 0, ss.grabImage(lanes[row]-1, 0, w, h), effects[lanes[row]-1], ID.NonCollidableObject, handler);
                }
            }
        }
        counter %= freq;
    }

    //Helper methods
    public int getObjectCnt() {
        return objectCnt;
    }
    
    public int getCounter() {
        return counter;
    }
    
    public void setCounter(int cnt) {
        counter = cnt;
    }
    
    public int getFreq() {
        return freq;
    }
    
    public int getW() {
        return w;
    }
    
    public int getH() {
        return h;
    }
    
    public int getMaxSpawn() {
        return maxSpawn;
    }
    
    public int getSpawnHeight() {
        return spawnHeight;
    }
    
    public int getSpawnRange() {
        return spawnRange;
    }
    
    public ObjectID[] getEffects() {
        return effects;
    }
    
    public SpriteSheet getSpriteSheet() {
        return ss;
    }
    
    public Handler getHandler() {
        return handler;
    }
}
