public class Player implements GameObject {

    private Rectangle playerRectangle;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    private int speed = 10;

    public Player() {
        playerRectangle = new Rectangle(32, 16, Game.TILE_SIZE, Game.TILE_SIZE);
        playerRectangle.generateGraphics(2, 1234567);
    }

    public Player(Sprite playerSprite) {
        this.sprite = playerSprite;
        if (playerSprite instanceof AnimatedSprite) {
            animatedSprite = (AnimatedSprite) playerSprite;
        }
        playerRectangle = new Rectangle(32, 16, Game.TILE_SIZE, Game.TILE_SIZE);
        playerRectangle.generateGraphics(1, 1234567);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderRectangle(playerRectangle, xZoom, yZoom);
    }

    @Override
    public void update(Game game) {
        KeyboardListener keyboardListener = game.getKeyboardListener();

        if (keyboardListener.up()) {
            playerRectangle.setY(playerRectangle.getY() - speed);
        }

        if (keyboardListener.down()) {
            playerRectangle.setY(playerRectangle.getY() + speed);
        }

        if (keyboardListener.left()) {
            playerRectangle.setX(playerRectangle.getX() - speed);
        }

        if (keyboardListener.right()) {
            playerRectangle.setX(playerRectangle.getX() + speed);
        }
        updateCamera(game.getRenderer().getCamera());
    }

    public void updateCamera(Rectangle camera) {
        camera.setX(playerRectangle.getX() - (camera.getWidth() / 2));
        camera.setY(playerRectangle.getY() - (camera.getHeight() / 2));
    }
}
