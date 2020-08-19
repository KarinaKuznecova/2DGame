import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Game extends JFrame implements Runnable {

    public static final int alpha = 0xFF80FF00;
    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    private BufferedImage bufferedImage;
    private Rectangle testRectangle = new Rectangle(50, 50, 100, 100);
    private Rectangle testRectangle2 = new Rectangle(100, 90, 100, 100);
    private Sprite testSprite;
    private SpriteSheet sheet;
    private Tiles tiles;

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
        bufferedImage = loadImage("resources/img/Tiles.png");
        sheet = new SpriteSheet(bufferedImage);
        sheet.loadSprites(16, 16, 0);

        File file = new File("src/resources/Tile.txt");
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());
        tiles = new Tiles(file, sheet);

//        testSprite = sheet.getSprite(1, 1);

        testRectangle2.generateGraphicsAsInVideo(5, 1234);
        testRectangle.generateGraphics(5, 123456);
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

//        renderer.renderSprite(testSprite, 0, 0, 5, 5);
        tiles.renderTile(0, renderer, 0, 0, 3, 3);
        renderer.renderRectangle(testRectangle2, 1, 1);
        renderer.renderRectangle(testRectangle, 1, 1);
        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
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
}
