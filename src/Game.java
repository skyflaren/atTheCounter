import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable{
    
    static final int WIDTH = 800, HEIGHT = 500;
    
    private Thread thread;
    private boolean running = false;
    private Handler handler;
    
    private Menu menu;
    
    //Spawns objects during the scrolling phase
    private Spawner obstacleSpawner, powerupSpawner, decorSpawner; 
    private GameObjectSpawner gameObjectSpawner; 
    
    //Loads Images
    private BufferedImage bg;
    private BufferedImageLoader loader;
    
    public static State gameState = State.Menu;
    
    //Constructor, initializes all the objects and assets
    public Game() {
        
        loader = new BufferedImageLoader();
        bg = loader.loadImage("/background.png");
        
        handler = new Handler();
        menu = new Menu(this, handler);
        
        this.addKeyListener(new KeyInput(handler));
        this.addMouseListener(menu);
        
        new Window(WIDTH, HEIGHT, "AT THE COUNTER", this);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Game engine, all of the processing is called from inside this method
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; //ticks per second
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >=1){ //Calls tick() based on elapsed time, this accounts for lag
                tick();
                delta--;
            }
            if (running)
                render();
            frames++;
                    
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        stop();
    }
    
    //Performs processing for each tick
    private void tick() {
        if (gameState == State.Menu) {
            menu.tick();
        }
        if (gameState == State.Tutorial) {
            handler.tick();
        }
        if (gameState == State.Game) {
            handler.tick();
            gameObjectSpawner.tick();
            decorSpawner.tick();
        }
    }
    
    //Renders images
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        
        if (gameState == State.Menu) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            menu.render(g);
        }
        if (gameState == State.Tutorial) {
            g.drawImage(bg,0,0,null);
            handler.render(g);
        }
        if (gameState == State.Game) {
            g.drawImage(bg,0,0,null);
            handler.render(g);
        }
        
        g.dispose();
        bs.show();
    }
    
    public void startLevel() {
        new Player(400,350,128,12,0,-104, loader.loadImage("/character_sprite_sheet.png"), new HUD(), ID.Player, handler, this);
        
        obstacleSpawner = new Spawner(2, 80, 4, 200, 5, 60, 60, new ObjectID[] {ObjectID.Crowd, ObjectID.Garbage}, 
                loader.loadImage("/obstacle_sprite_sheet.png"), handler);
        
        powerupSpawner = new Spawner(3, 1800, 1, 200, 5, 60, 60, new ObjectID[] {ObjectID.Mask, ObjectID.Sanitizer, ObjectID.Gloves},
                loader.loadImage("/powerup_sprite_sheet.png"), handler);
        
        gameObjectSpawner = new GameObjectSpawner(new Spawner[] {obstacleSpawner, powerupSpawner}, 200, 5, 60, 60, handler);
        
        decorSpawner = new Spawner(4, 240, 1, 0, 1, 200, 200, new ObjectID[] {ObjectID.Decor, ObjectID.Decor, ObjectID.Decor, ObjectID.Decor}, 
                loader.loadImage("/wall_decor_sprite_sheet.png"), handler);
    }
    
    //Helper method that squishes values into a specified range
    public static int clamp(int val, int min, int max) {
        return Math.min(max, Math.max(val, min));
    }
    
    public static void main(String[] args) {
        new Game();
    }
}
