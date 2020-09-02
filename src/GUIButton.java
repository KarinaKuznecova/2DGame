public abstract class GUIButton implements GameObject {

    protected Sprite sprite;
    protected Rectangle region;
    protected boolean fixed;

    public GUIButton(Sprite sprite, Rectangle region, boolean fixed) {
        this.sprite = sprite;
        this.region = region;
        this.fixed = fixed;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(sprite, region.getX(), region.getY(), xZoom, yZoom, fixed);
    }

    @Override
    public void update(Game game) {

    }

    @Override
    public void handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        if (mouseRectangle.intersects(region)) {
            activate();
        }
    }

    public abstract void activate();
}
