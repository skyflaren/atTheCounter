import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    
    protected int x, y, w, h, xdispl, ydispl;
    protected int velX, velY;
    protected Handler handler;
    protected ID id;
    
    //Constructor
    public GameObject(int x, int y, int w, int h, int xdispl, int ydispl, ID id, Handler handler) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.xdispl = xdispl; //Displacement of the sprite from its collision box
        this.ydispl = ydispl; 
        this.id = id;
        this.handler = handler;
        handler.addObject(this);
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    
    //Intersect that accounts for direction, prevents clipping into objects
    public boolean[] intersect(GameObject other, int velX, int velY) {
        // returns a boolean checking movement in both axis, just velY and just velX
        return new boolean[]{(other.getY() + velY < y + h && other.getY() + other.getH() + velY > y &&
                              other.getX() + velX < x + w && other.getX() + other.getW() + velX > x),
                              (other.getY() + velY < y + h && other.getY() + other.getH() + velY > y &&
                              other.getX() < x + w && other.getX() + other.getW() > x),
                              (other.getY() < y + h && other.getY() + other.getH() > y &&
                              other.getX() + velX < x + w && other.getX() + other.getW() + velX > x)};
    }
    
    //Intersect without accounting for direction, used to detect overlap in non-solid objects
    public boolean intersect(GameObject other) {
        return other.x + other.w > x && other.x < x + w
            && other.y + other.h > y && other.y < y + h;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
        }
    public int getY() {
        return y;
    }
    public void setW(int w) {
        this.w = w;
    }
    public void setH(int h) {
        this.h = h;
    }
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }
    public void setVelX(int velX) {
        this.velX = velX;
    }
    public void setVelY(int velY) {
        this.velY = velY;
    }
    public int getVelX() {
        return velX;
    }
    public int getVelY() {
        return velY;
    }
    public void setId(ID id) {
        this.id = id;
    }
    public ID getId() {
        return id;
    }
    
}
