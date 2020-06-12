import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
    
    LinkedList<GameObject> object = new LinkedList<GameObject>();
    
    //Handles processing of objects
    public void tick() {
        //If the object is off the screen, remove it from the list to prevent lag
        for (int i = 0; i < object.size(); i++) {
            GameObject temp = object.get(i);
            if (temp.x > 800) {
                object.remove(i);
            }
        }
        //Update each object
        for (int i = 0; i < object.size(); i++) {
            GameObject temp = object.get(i);
            temp.tick();
        }
    }
    
    //Calls each objects corresponding render method
    public void render(Graphics g) {
        for (int i = 0; i < object.size(); i++) {
            GameObject temp = object.get(i);
            temp.render(g);
        }
    }
    
    //Allows objects to be added
    public void addObject(GameObject object) {
        this.object.add(0,object);
    }
    
    //Allows objects to be removed
    public void removeObject(GameObject object) {
        this.object.remove(object);
    }
}
