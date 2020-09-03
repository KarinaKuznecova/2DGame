public interface GameObject {

    //should be called as often as possible
    void render(RenderHandler renderer, int xZoom, int yZoom);

    //called 60fps
    void update(Game game);

    //Return true to stop checking other clicks.
    boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);

}
