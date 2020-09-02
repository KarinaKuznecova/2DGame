public class GUI implements GameObject {

    private Sprite backgroundSprite;
    private GUIButton[] buttons;
    private Rectangle rectangle = new Rectangle();
    private boolean fixedOnScreen;

    public GUI(Sprite backgroundSprite, GUIButton[] buttons, int xPosition, int yPosition, boolean fixedOnScreen) {
        this.backgroundSprite = backgroundSprite;
        this.buttons = buttons;
        this.fixedOnScreen = fixedOnScreen;

        rectangle.setX(xPosition);
        rectangle.setY(yPosition);
        if (backgroundSprite != null) {
            rectangle.setHeight(backgroundSprite.getHeight());
            rectangle.setWidth(backgroundSprite.getWidth());
        } else {
            rectangle.setWidth(Game.TILE_SIZE);
            rectangle.setHeight(Game.TILE_SIZE);
        }
    }

    public GUI(GUIButton[] buttons, int xPosition, int yPosition, boolean fixedOnScreen) {
        this(null, buttons, xPosition, yPosition, fixedOnScreen);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (backgroundSprite != null) {
            renderer.renderSprite(backgroundSprite, rectangle.getX(), rectangle.getY(), xZoom, yZoom, fixedOnScreen);
        }
        for (GUIButton button : buttons) {
            button.render(renderer, xZoom, yZoom);
        }

    }

    @Override
    public void update(Game game) {
        for (GUIButton button : buttons) {
            button.update(game);
        }
    }

    @Override
    public void handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        if (!fixedOnScreen) {
            mouseRectangle = new Rectangle(mouseRectangle.getX() + camera.getX(), mouseRectangle.getY() + camera.getY(), 1, 1);
        }

        if (rectangle.getWidth() == 0 || rectangle.getHeight() == 0 || mouseRectangle.intersects(rectangle)) {
            for (GUIButton button : buttons) {
                mouseRectangle.setX(mouseRectangle.getX() + rectangle.getX());
                mouseRectangle.setY(mouseRectangle.getY() + rectangle.getY());
                button.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
            }
        }

    }
}
