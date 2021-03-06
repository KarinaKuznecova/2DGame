public class Rectangle {

    private int x;
    private int y;
    private int width;
    private int height;
    private int[] pixels;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getPixels() {
        if (pixels != null) {
            return pixels;
        }
        System.out.println("Attempt of getting pixels without generating graphics first");
        return null;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    @Deprecated
    public void generateGraphics(int color) {
        pixels = new int[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i + j * width] = color;
            }
        }
    }

    public void generateGraphics(int borderWidth, int color) {
        pixels = new int[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i < borderWidth || i >= height - borderWidth || j < borderWidth || j >= width - borderWidth) {
                    pixels[i + j * width] = color;
                } else {
                    pixels[i + j * width] = Game.ALPHA;
                }
            }
        }
    }

    @Deprecated
    public void generateGraphicsAsInVideo(int borderWidth, int color) {
        pixels = new int[width * height];
        for (int i = 0; i < borderWidth; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i + j * width] = color;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < borderWidth; j++) {
                pixels[i + j * width] = color;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = width - borderWidth; j < width; j++) {
                pixels[i + j * width] = color;
            }
        }

        for (int i = height - borderWidth; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i + j * width] = color;
            }
        }
    }

    public boolean intersects(Rectangle otherRectangle) {
        return (intersectsByX(otherRectangle) && intersectsByY(otherRectangle));
    }

    private boolean intersectsByY(Rectangle otherRectangle) {
        return !(y > otherRectangle.getY() + otherRectangle.getHeight() || otherRectangle.getY() > y + height);
    }

    private boolean intersectsByX(Rectangle otherRectangle) {
        return !(x > otherRectangle.getX() + otherRectangle.getWidth() || otherRectangle.getX() > x + width);
    }
}
