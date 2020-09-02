import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class RenderHandler {

    private BufferedImage view;
    private int[] pixels;
    private Rectangle camera;

    public RenderHandler(int width, int height) {
        //Create a BufferedImage that will represent our view.
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        camera = new Rectangle(0, 0, width, height);

        //Create an array for pixels
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    // renders our pixels to the screen
    public void render(Graphics graphics) {
        graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    // render our image to array of all pixels
    public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        renderPixelsArrays(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition, xZoom, yZoom, fixed);
    }

    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, boolean fixed) {
        int[] rectanglePixels = rectangle.getPixels();
        if (rectanglePixels != null) {
            renderPixelsArrays(rectanglePixels, rectangle.getWidth(), rectangle.getHeight(), rectangle.getX(), rectangle.getY(), xZoom, yZoom, fixed);
        }
    }

    public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
        renderPixelsArrays(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition, xZoom, yZoom, fixed);
    }

    public void renderPixelsArrays(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
        for (int y = 0; y < renderHeight; y++) {
            for (int x = 0; x < renderWidth; x++) {
                for (int yZ = 0; yZ < yZoom; yZ++) {
                    for (int xZ = 0; xZ < xZoom; xZ++) {
                        int pixel = renderPixels[renderWidth * y + x];
                        int xPos = (x * xZoom + xZ + xPosition);
                        int yPos = (y * yZoom + yZ + yPosition);
                        setPixel(pixel, xPos, yPos, fixed);
                    }
                }
            }
        }
    }

    public void setPixel(int pixel, int x, int y, boolean fixed) {
        int pixelIndex = 0;
        if (!fixed && isInRangeOfCamera(x, y)) {
            pixelIndex = (x - camera.getX()) + (y - camera.getY()) * view.getWidth();
        }
        if (fixed) {
            if (x >= 0 && y >=0 && x <= camera.getWidth() && y <= camera.getHeight()) {
                pixelIndex = x + y * view.getWidth();
            }
        }
        if (isInGlobalRange(pixelIndex) && !isAlphaColor(pixel)) {
            pixels[pixelIndex] = pixel;
        }
    }

    private boolean isAlphaColor(int pixel) {
        return pixel == Game.ALPHA || pixel == 0x7fdf06 || pixel == 0x80ff06;
    }

    private boolean isInGlobalRange(int pixelIndex) {
        return pixelIndex < pixels.length;
    }

    private boolean isInRangeOfCamera(int x, int y) {
        return x >= camera.getX() &&
                y >= camera.getY() &&
                x <= camera.getX() + camera.getWidth() &&
                y <= camera.getY() + camera.getHeight();
    }

    public Rectangle getCamera() {
        return camera;
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }
}
