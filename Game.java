import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends Canvas implements Runnable{
    
    static final int WIDTH = 800, HEIGHT = 500;
    
    private Thread thread;
    private boolean running = false;
    private Handler handler;
    private Window window;
    private Menu menu;
    
    //Spawns objects during the scrolling phase
    private Spawner obstacleSpawner, powerupSpawner, decorSpawner; 
    private GameObjectSpawner gameObjectSpawner; 
    private Player player;
    
    //Loads Images
    private BufferedImage bg, powerups, obstacles, decor, charSprites, managerSprites, mopBucket, counterLeft, counterRight, menuImage, gameOverImage, lobbyBg, creditsImage, infoImage,
        exitMat, startMat, soupImage, washroomMat, soupTurn, soupBg, mopTiles, infoPage, soupIntro,
        mopIntro, creditsPage, brownBG;
    private BufferedImageLoader loader;
    
    private Minigame1 mg1;
    private Minigame2 mg2;
    
    public static State gameState = State.Lobby;
    public static int objectSpeed = 4;
    public static boolean startOne = false, startTwo = false;
    
    //Constructor, initializes all the objects and assets
    public Game() {
        
        loader = new BufferedImageLoader();
        bg = loader.loadImage("/background.png");
        powerups = loader.loadImage("/powerup_sprite_sheet.png");
        obstacles = loader.loadImage("/obstacle_sprite_sheet.png");
        decor = loader.loadImage("/wall_decor_sprite_sheet.png");
        charSprites = loader.loadImage("/character_sprite_sheet.png");
        managerSprites = loader.loadImage("/manager_sprite_sheet.png");
        mopBucket = loader.loadImage("/mop_bucket.png");
        counterLeft = loader.loadImage("/counter_left.png");
        counterRight = loader.loadImage("/counter_right.png");
        menuImage = loader.loadImage("/menu.png");
        gameOverImage = loader.loadImage("/game_over.png");
        lobbyBg = loader.loadImage("/lobby_background.png");
        creditsImage = loader.loadImage("/credits.png");
        infoImage = loader.loadImage("/info.png");
        soupImage = loader.loadImage("/soup.png");
        exitMat = loader.loadImage("/exit_mat.png");
        startMat = loader.loadImage("/start_mat.png");
        washroomMat = loader.loadImage("/washroom_mat.png");
        soupTurn = loader.loadImage("/soup_turning.png");
        soupBg = loader.loadImage("/soup_bg.png");
        mopTiles = loader.loadImage("/mop_tiles.png");
        infoPage = loader.loadImage("/info_page.png");
        soupIntro = loader.loadImage("/soup_intro.png");
        mopIntro = loader.loadImage("/mop_intro.png");
        creditsPage = loader.loadImage("/credits_page.png");
        brownBG = loader.loadImage("/brown_bg.png");
        
        
        mg1 = new Minigame1(soupTurn);
        mg2 = new Minigame2(mopTiles);
        
        try {
            GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font wonder = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("wonder.ttf"));
            ge.registerFont(wonder);
       } catch (IOException|FontFormatException e) {
           e.printStackTrace();
       }
        
        handler = new Handler();
        menu = new Menu(this, handler);
        
        this.addKeyListener(new KeyInput(handler, mg1, mg2));
        this.addMouseListener(menu);
        
        window = new Window(WIDTH, HEIGHT, "AT THE COUNTER", this);
        
        startLevel();
        loadLobby();
    }
    
    public void close() {
        window.frame.dispatchEvent(new WindowEvent(window.frame, WindowEvent.WINDOW_CLOSING));
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
        else if (gameState == State.Tutorial) {
            handler.tick();
        }
        else if (gameState == State.Lobby) {
            handler.tick();
        }
        else if (gameState == State.Minigame1) { // Soup
            if(startOne == true){
                mg1.tick();
                if(Math.floor(mg1.getTime()) <= 0){
                    gameState = State.Lobby;
                    loadLobby();
                    player.incrementRisk((mg1.getScore()-1400)/7);
                    mg1.reset();
                    startOne = false;
                }
            }
        }
        else if (gameState == State.Minigame2) { // Mop
            if(startTwo == true){
                mg2.tick();
                if(Math.floor(mg2.getTime()) <= 0){
                    gameState = State.Lobby;
                    loadLobby();
                    player.incrementRisk((mg2.getScore()-1400)/7);
                    mg2.reset();
                    startTwo = false;
                }
            }
        }
        else if (gameState == State.Game) {
            handler.tick();
            gameObjectSpawner.tick();
            decorSpawner.tick();
        }
        else if (gameState == State.GameOver) {
            menu.setScore(player.getScore());
            menu.tick();
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
            g.drawImage(menuImage, 0, 0, null);
            menu.render(g);
        }
        else if (gameState == State.Tutorial) {
            g.drawImage(bg,0,0,null);
            handler.render(g);
        }
        else if (gameState == State.Lobby) {
            g.drawImage(lobbyBg,0,0,null);
            handler.render(g);
        }
        else if (gameState == State.Minigame1) {
            if(startOne == true){
                g.drawImage(soupBg, 0, 0, null);
                mg1.render(g);
            }
            else{
                g.drawImage(soupIntro, 0, 0, null);
            }
        }
        else if (gameState == State.Minigame2) {
            if(startTwo == true){
                g.drawImage(brownBG, 0, 0, null);
                mg2.render(g);
            }
            else{
                g.drawImage(mopIntro, 0, 0, null);
            }
        }
        else if (gameState == State.Info) {
            g.drawImage(infoPage, 0, 0, null);
            //info.render()
        }
        else if (gameState == State.Credits) {
            g.drawImage(creditsPage, 0, 0, null);
            //credits.render()
        }
        else if (gameState == State.Game) {
            g.drawImage(bg,0,0,null);
            handler.render(g);
        }
        else if (gameState == State.GameOver) {
            g.drawImage(gameOverImage,0,0,null);
            menu.render(g);
        }
        
        g.dispose();
        bs.show();
    }
    
    public void startLevel() {
        player = new Player(400,350,64,12,-40,-104, charSprites, managerSprites, new HUD(), ID.Player, handler, this);
        
        obstacleSpawner = new Spawner(2, 80, 4, 200, 5, 60, 60, new ObjectID[] {ObjectID.Crowd, ObjectID.Garbage}, 
                obstacles, handler);
        
        powerupSpawner = new Spawner(3, 1800, 1, 200, 5, 60, 60, new ObjectID[] {ObjectID.Mask, ObjectID.Sanitizer, ObjectID.Gloves},
                powerups, handler);
        
        gameObjectSpawner = new GameObjectSpawner(new Spawner[] {obstacleSpawner, powerupSpawner}, 200, 5, 60, 60, handler);
        
        decorSpawner = new Spawner(5, 240, 1, 0, 1, 200, 200, new ObjectID[] {ObjectID.Decor, ObjectID.Decor, ObjectID.Decor, ObjectID.Decor, ObjectID.Decor}, 
                decor, handler);
    }
    
    public void loadLobby() {
        new Object(140, 235, 50, 27, 0, 0,
                soupImage,
                ObjectID.Mini1, ID.CollidableObject, handler);
        new Object(400, 390, 82, 94, 0, 0,
                mopBucket,
                ObjectID.Mini2, ID.CollidableObject, handler);
        new Object(0, 200, 46, 90, 0, 0,
                counterLeft,
                ObjectID.Misc, ID.CollidableObject, handler);
        new Object(46, 234, 244, 56, -46, -34,
                counterRight,
                ObjectID.Misc, ID.CollidableObject, handler);
        new Object(300, 106, 123, 94, 0, -64,
                infoImage,
                ObjectID.Info, ID.CollidableObject, handler);
        new Object(472, 83, 73, 117, 0, -52,
                creditsImage,
                ObjectID.Credits, ID.CollidableObject, handler);
        new Object(6, 320, 44, 109, 0, 0,
                startMat,
                ObjectID.StartMat, ID.NonCollidableObject, handler);
        new Object(753, 218, 41, 113, 0, 0,
                exitMat,
                ObjectID.ExitMat, ID.NonCollidableObject, handler);
        new Object(753, 360, 41, 113, 0, 0,
                washroomMat,
                ObjectID.WashroomMat, ID.NonCollidableObject, handler);
    }
    
    //Helper method that squishes values into a specified range
    public static int clamp(int val, int min, int max) {
        return Math.min(max, Math.max(val, min));
    }
    
    public static double clamp(double val, double min, double max) {
        return Math.min(max, Math.max(val, min));
    }
    
    public static void main(String[] args) {
        new Game();
    }
}
