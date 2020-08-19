import java.awt.image.BufferedImage;

public class SpriteSheet {
    private int[] pixels;
    private BufferedImage image;
    private Sprite[] loadedSprites = null;
    private int spriteSizeX;
    private boolean loaded = false;

    public final int SIZE_X;
    public final int SIZE_Y;

    public SpriteSheet(BufferedImage sheetImage) {
        image = sheetImage;
        SIZE_X = sheetImage.getWidth();
        SIZE_Y = sheetImage.getHeight();

        pixels = new int[SIZE_X * SIZE_Y];
        pixels = sheetImage.getRGB(0, 0, SIZE_X, SIZE_Y, pixels, 0, SIZE_X);
    }

    public void loadSprites(int spriteSizeX, int spriteSizeY, int paddingWidth) {
        this.spriteSizeX = spriteSizeX;
        loadedSprites = new Sprite[(SIZE_X / spriteSizeX) * (SIZE_Y / spriteSizeY)];
        int spriteId = 0;

        for (int i = 0; i < SIZE_Y; i += spriteSizeY + paddingWidth) {
            for (int j = 0; j < SIZE_X; j += spriteSizeX + paddingWidth) {
                loadedSprites[spriteId] = new Sprite(this, j, i, spriteSizeX, spriteSizeY);
                spriteId++;
            }
        }
        loaded = true;
    }

    public Sprite getSprite(int x, int y) {
        if (loaded) {
            int spriteId = x + y * (SIZE_X / spriteSizeX);
            if (spriteId < loadedSprites.length) {
                return loadedSprites[spriteId];
            } else {
                System.out.println("wanted sprite id is out of bounds");
            }
        } else {
            System.out.println("Sprites not loaded");
        }
        return null;
    }

    public int[] getPixels() {
        return pixels;
    }

    public BufferedImage getImage() {
        return image;
    }
}
