import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tiles {

    private SpriteSheet spriteSheet;
    private List<Tile> tileList = new ArrayList<>();

    //spriteSheet should be already loaded
    public Tiles(File tilesFile, SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
        try {
            Scanner scanner = new Scanner(tilesFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    String[] splitLine = line.split("-");
                    String tileName = splitLine[0];
                    int spriteXPosition = Integer.parseInt(splitLine[1]);
                    int spriteYPosition = Integer.parseInt(splitLine[2]);
                    Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteXPosition, spriteYPosition));
                    tileList.add(tile);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void renderTile(int tileIndex, RenderHandler renderer, int xPosition, int yPosition, int xZoom, int yZoom) {
        if (tileIndex >= 0 && tileList.size() > tileIndex) {
            renderer.renderSprite(tileList.get(tileIndex).getSprite(), xPosition, yPosition, xZoom, yZoom, false);
        } else {
            System.out.println("tileIndex: " + tileIndex + " is out of bounds");
        }
    }

    public int size() {
        return tileList.size();
    }

    public Sprite[] getSprites() {
        Sprite[] sprites = new Sprite[size()];
        for (int i=0; i< sprites.length; i++) {
            sprites[i] = tileList.get(i).getSprite();
        }
        return sprites;
    }

    class Tile {
        public String tileName;
        public Sprite sprite;

        public Tile(String tileName, Sprite sprite) {
            this.tileName = tileName;
            this.sprite = sprite;
        }

        public Sprite getSprite() {
            return sprite;
        }
    }
}
