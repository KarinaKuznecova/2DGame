public class Player implements GameObject {

    private Rectangle playerRectangle;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    private int speed = 10;
    //TODO: make it as enum
    //0 - down, 1 - left, 2 - up, 3 - right
    private int direction;

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
            renderer.renderSprite(animatedSprite, playerRectangle.getX(), playerRectangle.getY(), xZoom, yZoom);
        } else if (sprite != null) {
            renderer.renderSprite(sprite, playerRectangle.getX(), playerRectangle.getY(), xZoom, yZoom);
        } else {
            renderer.renderRectangle(playerRectangle, xZoom, yZoom);
        }
    }

    @Override
    public void update(Game game) {
        KeyboardListener keyboardListener = game.getKeyboardListener();

        boolean isMoving = false;
        int newDirection = direction;

        // TODO: refactor
        if (keyboardListener.left()) {
            playerRectangle.setX(playerRectangle.getX() - speed);
            newDirection = 1;
            isMoving = true;
        }

        if (keyboardListener.right()) {
            playerRectangle.setX(playerRectangle.getX() + speed);
            newDirection = 3;
            isMoving = true;
        }

        if (keyboardListener.up()) {
            playerRectangle.setY(playerRectangle.getY() - speed);
            newDirection = 2;
            isMoving = true;
        }

        if (keyboardListener.down()) {
            playerRectangle.setY(playerRectangle.getY() + speed);
            newDirection = 0;
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

    public void updateCamera(Rectangle camera) {
        camera.setX(playerRectangle.getX() - (camera.getWidth() / 2));
        camera.setY(playerRectangle.getY() - (camera.getHeight() / 2));
    }

    private void updateDirection() {
        if (animatedSprite != null) {
            animatedSprite.setAnimationRange(direction, direction + 12);
//            animatedSprite.setAnimationRange((direction * 4), (direction * 4 + 4)); //if horizontal increase
        }
    }
}
