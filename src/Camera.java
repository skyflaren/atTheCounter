import java.awt.Graphics;

public class Camera {
    
    private int camX, camY;
    private GameObject following;

    public Camera(int x, int y, Player player) {
        camX = x;
        camY = y;
        following = player;
    }

    public void tick() {
        camX = following.getX() - Game.WIDTH / 2;
        camY = following.getY() - Game.HEIGHT / 2;
        
        camX = Game.clamp(camX,0,Game.WIDTH);
        camY = Game.clamp(camY,0,Game.HEIGHT);
    }
    
    public void render(Graphics g) {
        g.translate(-camX, -camY);
    }
    
    
}
