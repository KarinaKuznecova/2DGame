import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameMap {

    private Tiles tiles;
    private int fillTileId = -1;
    private List<MappedTile> tileList = new ArrayList<>();

    public GameMap(File mapFile, Tiles tiles) {
        this.tiles = tiles;
        try {
            Scanner scanner = new Scanner(mapFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    if (line.contains(":")) {
                        String[] splitLine = line.split(":");
                        if (splitLine[0].equalsIgnoreCase("Fill")) {
                            fillTileId = Integer.parseInt(splitLine[1]);
                            continue;
                        }
                    }
                    String[] splitLine = line.split("-");
                    if (splitLine.length >= 3) {
                        int tileId = Integer.parseInt(splitLine[0]);
                        int xPosition = Integer.parseInt(splitLine[1]);
                        int yPosition = Integer.parseInt(splitLine[2]);
                        MappedTile mappedTile = new MappedTile(tileId, xPosition, yPosition);
                        tileList.add(mappedTile);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        int xIncrement = 16 * xZoom;
        int yIncrement = 16 * yZoom;

//        if (fillTileId >= 0) {
//            Rectangle camera = renderer.getCamera();
//            MappedTile mappedTile = tileList.get(fillTileId);
//            for (int i = 0; i < camera.getHeight(); ) {
//                for (int j = 0; j < camera.getWidth(); ) {
//
//                }
//            }
//        }

        for (int i = 0; i < tileList.size(); i++) {
            MappedTile mappedTile = tileList.get(i);
            tiles.renderTile(mappedTile.getId(), renderer, mappedTile.getX() * xIncrement, mappedTile.getY() * yIncrement, xZoom, yZoom);
        }
    }

    class MappedTile {
        private int id;
        private int x;
        private int y;

        public MappedTile(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int getId() {
            return id;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
