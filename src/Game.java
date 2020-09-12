import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
    private GUIButton[] buttons;

    private AnimatedSprite playerAnimations;

    private int selectedTileId = 2;

    private KeyboardListener keyboardListener = new KeyboardListener(this);
    private MouseEventListener mouseEventListener = new MouseEventListener(this);

    public Game() {
        //Make our program shutdown when we exit out.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the position and size of our frame x/y/width/height.
        setBounds(0, 0, 1300, 700);

        //Put our frame in the center of the screen.
        setLocationRelativeTo(null);

        //Add our graphics component
        add(canvas);

        //Make our frame visible.
        setVisible(true);

        //Create our object for buffer strategy.
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        loadEverything();

        addListeners();

        canvas.requestFocus();
    }

    private void addListeners() {
        canvas.addKeyListener(keyboardListener);
        canvas.addFocusListener(keyboardListener);
        canvas.addMouseListener(mouseEventListener);
        canvas.addMouseMotionListener(mouseEventListener);

        canvas.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = canvas.getWidth();
                int newHeight = canvas.getHeight();

                if (newWidth > renderer.getMaxWidth())
                    newWidth = renderer.getMaxWidth();

                if (newHeight > renderer.getMaxHeight())
                    newHeight = renderer.getMaxHeight();

                renderer.getCamera().setWidth(newWidth);
                renderer.getCamera().setHeight(newHeight);
                canvas.setSize(newWidth, newHeight);
                pack();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    private void loadEverything() {
        loadSpriteSheet();
        loadPlayerAnimatedImages();

        tiles = new Tiles(new File("src/resources/Tile.txt"), sheet);

        gameMap = new GameMap(new File("src/resources/Map.txt"), tiles);

        loadSDKGUI();
        loadGameObjects();
    }

    private void loadSpriteSheet() {
        BufferedImage bufferedImage = loadImage("resources/img/terrain.png");
        sheet = new SpriteSheet(bufferedImage);
        sheet.loadSprites(TILE_SIZE, TILE_SIZE, 0);
    }

    private void loadPlayerAnimatedImages() {
        BufferedImage playerSheetImage = loadImage("resources/img/betty.png");
        playerSheet = new SpriteSheet(playerSheetImage);
        playerSheet.loadSprites(TILE_SIZE, TILE_SIZE, 0);
        playerAnimations = new AnimatedSprite(playerSheet, 5);
    }

    private void loadSDKGUI() {
        GUIButton[] buttons = new GUIButton[tiles.size()];
        Sprite[] tileSprites = tiles.getSprites();

        for (int i = 0; i < buttons.length; i++) {
//            Rectangle tileRectangle = new Rectangle(0, i * (TILE_SIZE * ZOOM + 2), TILE_SIZE * ZOOM, TILE_SIZE * ZOOM);       // vertical on top left side
            Rectangle tileRectangle = new Rectangle(i * (TILE_SIZE * ZOOM + 2), 0, TILE_SIZE * ZOOM, TILE_SIZE * ZOOM);  //horizontal on top left
            buttons[i] = new SDKButton(this, i, tileSprites[i], tileRectangle);
        }
        this.buttons = buttons;
    }

    private void loadGameObjects() {
        GUI gui = new GUI(buttons, 5, 5, true);
        player = new Player(playerAnimations);

        gameObjects = new GameObject[2];
        gameObjects[0] = player;
        gameObjects[1] = gui;
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

    public void changeTile(int tileId) {
        selectedTileId = tileId;
    }

    public void leftClick(int x, int y) {
        Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
        boolean stoppedChecking = false;

        for (GameObject gameObject : gameObjects) {
            if (!stoppedChecking) {
                stoppedChecking = gameObject.handleMouseClick(mouseRectangle, renderer.getCamera(), ZOOM, ZOOM);
            }
        }
        if (!stoppedChecking) {
            x = (int) Math.floor((x + renderer.getCamera().getX()) / (32.0 * ZOOM));
            y = (int) Math.floor((y + renderer.getCamera().getY()) / (32.0 * ZOOM));
            gameMap.setTile(x, y, selectedTileId);

        }
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

    public int getSelectedTileId() {
        return selectedTileId;
    }
}
