import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Object extends GameObject{

    private BufferedImage sprite;
    private ObjectID effect;
    
    //Constructor
    public Object(int x, int y, int w, int h, int xdispl, int ydispl, BufferedImage img, ObjectID effect, ID id, Handler handler) {
        super(x, y, w, h, xdispl, ydispl, id, handler);
        this.effect = effect;
        sprite = img;
    }
    
    public ObjectID getEffect() {
        return effect;
    }

    public void tick() {
        if (Game.gameState == State.Game || Game.gameState == State.Tutorial) {
            x += 4;
        }
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x + xdispl, y + ydispl, null);
//        g.setColor(Color.white);
//        g.fillRect(x, y, w, h);
    }
    
}
