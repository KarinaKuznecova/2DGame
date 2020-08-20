public interface GameObject {

    //should be called as often as possible
    public void render(RenderHandler renderer, int xZoom, int yZoom);

    //called 60fps
    public void update(Game game);

}
