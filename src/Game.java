import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame implements Runnable {

    public static final int ALPHA = 0xFF80FF00;
    public static final int TILE_SIZE = 32;
    public static final int ZOOM = 2;

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;

    private SpriteSheet sheet;
    private SpriteSheet playerSheet;
    private Tiles tiles;
    private GameMap gameMap;
    private GameObject[] gameObjects;
    private Player player;

    private AnimatedSprite playerAnimations;

    private KeyboardListener keyboardListener = new KeyboardListener(this);
    private MouseEventListener mouseEventListener = new MouseEventListener(this);

    public Game() {
        //Make our program shutdown when we exit out.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the position and size of our frame x/y/width/height.
        setBounds(0, 0, 1000, 800);

        //Put our frame in the center of the screen.
        setLocationRelativeTo(null);

        //Add our graphics component
        add(canvas);

        //Make our frame visible.
        setVisible(true);

        //Create our object for buffer strategy.
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        //load assets
        BufferedImage bufferedImage = loadImage("resources/img/terrain.png");
        sheet = new SpriteSheet(bufferedImage);
        sheet.loadSprites(TILE_SIZE, TILE_SIZE, 0);

        // loading player animated image
        BufferedImage playerSheetImage = loadImage("resources/img/betty.png");
        playerSheet = new SpriteSheet(playerSheetImage);
        playerSheet.loadSprites(TILE_SIZE, TILE_SIZE, 0);
        playerAnimations = new AnimatedSprite(playerSheet, 5);

        //load tiles
        tiles = new Tiles(new File("src/resources/Tile.txt"), sheet);

        //load map
        gameMap = new GameMap(new File("src/resources/Map.txt"), tiles);

        //TODO: make it prettier
        //load objects
        gameObjects = new GameObject[1];
        player = new Player(playerAnimations);
        gameObjects[0] = player;

        //adding listeners
        canvas.addKeyListener(keyboardListener);
        canvas.addFocusListener(keyboardListener);
        canvas.addMouseListener(mouseEventListener);
        canvas.addMouseMotionListener(mouseEventListener);
    }

    public static void main(String[] args) {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        super.paint(graphics);

        gameMap.render(renderer, ZOOM, ZOOM);

        for (GameObject gameObject : gameObjects) {
            gameObject.render(renderer, ZOOM, ZOOM);
        }

        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
        renderer.clear();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime(); //long 2^63
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double changeInSeconds = 0;

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1) {
                update();
                changeInSeconds--;
            }

            render();
            lastTime = now;
        }

    }

    public void update() {
        for (GameObject object : gameObjects) {
            object.update(this);
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(image, 0, 0, null);
            return formattedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void leftClick(int x, int y) {
        x = (int) Math.floor((x + renderer.getCamera().getX()) / (32.0 * ZOOM));
        y = (int) Math.floor((y + renderer.getCamera().getY()) / (32.0 * ZOOM));
        gameMap.setTile(x, y, 2);
    }

    public void rightClick(int x, int y) {
        x = (int) Math.floor((x + renderer.getCamera().getX()) / (32.0 * ZOOM));
        y = (int) Math.floor((y + renderer.getCamera().getY()) / (32.0 * ZOOM));
        gameMap.removeTile(x, y);
    }

    public void handleCTRL(boolean[] keys) {
        if (keys[KeyEvent.VK_S]) {
            gameMap.saveMap();
        }
    }

    public KeyboardListener getKeyboardListener() {
        return keyboardListener;
    }

    public RenderHandler getRenderer() {
        return renderer;
    }

    public MouseEventListener getMouseEventListener() {
        return mouseEventListener;
    }
}
