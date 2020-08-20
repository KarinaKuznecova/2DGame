public class Player implements GameObject {

    Rectangle playerRectangle;
    int speed = 10;

    public Player() {
        playerRectangle = new Rectangle(32, 16, 16, 16);
        playerRectangle.generateGraphics(2, 12345);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderRectangle(playerRectangle, xZoom, yZoom);
    }

    @Override
    public void update(Game game) {

    }
}
