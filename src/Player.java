public class Player implements GameObject {

    private Rectangle playerRectangle;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    private int speed = 10;
    private Direction direction;

    public Player(Sprite playerSprite) {
        this.sprite = playerSprite;
        if (playerSprite instanceof AnimatedSprite) {
            animatedSprite = (AnimatedSprite) playerSprite;
        }
        updateDirection();
        playerRectangle = new Rectangle(32, 16, Game.TILE_SIZE, Game.TILE_SIZE);
        playerRectangle.generateGraphics(1, 123);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (animatedSprite != null) {
            renderer.renderSprite(animatedSprite, playerRectangle.getX(), playerRectangle.getY(), xZoom, yZoom, false);
        } else if (sprite != null) {
            renderer.renderSprite(sprite, playerRectangle.getX(), playerRectangle.getY(), xZoom, yZoom, false);
        } else {
            renderer.renderRectangle(playerRectangle, xZoom, yZoom, false);
        }
    }

    @Override
    public void update(Game game) {
        KeyboardListener keyboardListener = game.getKeyboardListener();

        boolean isMoving = false;
        Direction newDirection = direction;

        if (keyboardListener.left()) {
            playerRectangle.setX(playerRectangle.getX() - speed);
            newDirection = Direction.LEFT;
            isMoving = true;
        }

        if (keyboardListener.right()) {
            playerRectangle.setX(playerRectangle.getX() + speed);
            newDirection = Direction.RIGHT;
            isMoving = true;
        }

        if (keyboardListener.up()) {
            playerRectangle.setY(playerRectangle.getY() - speed);
            newDirection = Direction.UP;
            isMoving = true;
        }

        if (keyboardListener.down()) {
            playerRectangle.setY(playerRectangle.getY() + speed);
            newDirection = Direction.DOWN;
            isMoving = true;
        }

        if (newDirection != direction) {
            direction = newDirection;
            updateDirection();
        }

        if (animatedSprite != null && !isMoving) {
            animatedSprite.reset();
        }

        updateCamera(game.getRenderer().getCamera());
        if (animatedSprite != null && isMoving) {
            animatedSprite.update(game);
        }
    }

    @Override
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        return false;
    }

    public void updateCamera(Rectangle camera) {
        camera.setX(playerRectangle.getX() - (camera.getWidth() / 2));
        camera.setY(playerRectangle.getY() - (camera.getHeight() / 2));
    }

    private void updateDirection() {
        if (animatedSprite != null && direction != null) {
            animatedSprite.setAnimationRange(direction.directionNumber, direction.directionNumber + 12);
//            animatedSprite.setAnimationRange((direction * 4), (direction * 4 + 4)); //if horizontal increase
        }
    }
}
