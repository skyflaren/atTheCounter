import java.awt.image.BufferedImage;
import java.util.*;

public class GameObjectSpawner {
    
    private Handler handler;
    private BufferedImage objects;
    private SpriteSheet ss;
    private Spawner[] spawners;
    private int spawnRange, spawnHeight, h, w;
    private ArrayList<ArrayList<ObjectID>> effects;
    
    //Constructor
    public GameObjectSpawner(Spawner[] spawners, int spawnHeight, int spawnRange, int h, int w, Handler handler) {
        this.spawners = spawners;
        this.spawnRange = spawnRange;
        this.spawnHeight = spawnHeight;
        this.h = h;
        this.w = w;
        this.handler = handler;
        
        //Preprocesses the necessary components so that objects can be spawned with the right effects and images
        effects = new ArrayList<ArrayList<ObjectID>>();
        for (int i = 0; i < spawners.length; i++) {
            effects.add(new ArrayList<ObjectID>());
            ObjectID[] arr = spawners[i].getEffects();
            for (ObjectID id:arr) {
                effects.get(i).add(id);
            }
        }
    }
    
    public void tick() {
        int[][] lanes = new int[5][2]; //Each lane is assigned an object group and its effect
        
        for (int i = 0; i < spawners.length; i++) { //Processes each spawner
            Spawner spawner = spawners[i];
            spawner.setCounter(spawner.getCounter()+(Game.objectSpeed/3));
            int counter = spawner.getCounter(), freq = spawner.getFreq(), maxSpawn = spawner.getMaxSpawn(), objectCnt = spawner.getObjectCnt();
            
            if (counter >= freq) { //Attempts a spawn
                for (int j = 0; j < maxSpawn; j++) {
                    int lane = (int)(Math.random()*spawnRange);
                    lanes[lane][0] = i; //Assigns the object group
                    lanes[lane][1] = (int)(Math.random()*objectCnt)+1; //Assigns the object type
                }
            }
            spawner.setCounter(counter % freq);
        }
        
        for (int row = 0; row < spawnRange; row++) { //For each row, spawn the appropriate object
            if(lanes[row][1] != 0) {
                ss = spawners[lanes[row][0]].getSpriteSheet(); //Gets the appropriate spritesheet to pass
                ObjectID effect = effects.get(lanes[row][0]).get(lanes[row][1]-1); //Gets the appropriate effect to pass
                //Creates a new object
                new Object(-w, spawnHeight+row*h, w, h, 0, 0, ss.grabImage(lanes[row][1]-1, 0, w, h), effect, ID.NonCollidableObject, handler);
            }
        }
    }
}
