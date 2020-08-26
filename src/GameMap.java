import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GameMap {

    private Tiles tiles;
    private int fillTileId = -1;
    private List<MappedTile> tileList = new ArrayList<>();
    private Map<Integer, String> comments = new HashMap<>();
    private File mapFile;

    public GameMap(File mapFile, Tiles tiles) {
        this.mapFile = mapFile;
        this.tiles = tiles;
        try {
            Scanner scanner = new Scanner(mapFile);
            int currentLine = 0;
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
                    String[] splitLine = line.split(",");
                    if (splitLine.length >= 3) {
                        int tileId = Integer.parseInt(splitLine[0]);
                        int xPosition = Integer.parseInt(splitLine[1]);
                        int yPosition = Integer.parseInt(splitLine[2]);
                        MappedTile mappedTile = new MappedTile(tileId, xPosition, yPosition);
                        tileList.add(mappedTile);
                    }
                } else {
                    comments.put(currentLine, line);
                }
                currentLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        int tileWidth = Game.TILE_SIZE * xZoom;
        int tileHeight = Game.TILE_SIZE * yZoom;

        if (fillTileId >= 0) {

            Rectangle camera = renderer.getCamera();

            for (int i = camera.getY() - tileHeight - (camera.getY() % tileHeight); i < camera.getY() + camera.getHeight(); i += tileHeight) {
                for (int j = camera.getX() - tileWidth - (camera.getX() % tileWidth); j < camera.getX() + camera.getWidth(); j += tileWidth) {
                    tiles.renderTile(fillTileId, renderer, j, i, xZoom, yZoom);
                }
            }
        }

        for (MappedTile mappedTile : tileList) {
            tiles.renderTile(mappedTile.getId(), renderer, mappedTile.getX() * tileWidth, mappedTile.getY() * tileHeight, xZoom, yZoom);
        }
    }

    public void setTile(int tileX, int tileY, int tileId) {
        boolean foundTile = false;
        for (MappedTile tile : tileList) {
            if (tile.getX() == tileX && tile.getY() == tileY) {
                tile.setId(tileId);
                foundTile = true;
                break;
            }
        }
        if (!foundTile) {
            tileList.add(new MappedTile(tileId, tileX, tileY));
        }
    }

    public void removeTile(int tileX, int tileY) {

    }

    public void saveMap() {
        try {
            int currentLine = 0;

            if (mapFile.exists()) {
                mapFile.delete();
            }

            mapFile.createNewFile();
            PrintWriter printWriter = new PrintWriter(mapFile);

            if (fillTileId >= 0) {
                if (comments.containsKey(currentLine)) {
                    printWriter.println(comments.get(currentLine));
                    currentLine++;
                }
                printWriter.println("Fill:" + fillTileId);
            }

            for (MappedTile tile : tileList) {
                if (comments.containsKey(currentLine)) {
                    printWriter.println(comments.get(currentLine));
                }
                printWriter.println(tile.getId() + "," + tile.getX() + "," + tile.getY());
                currentLine++;
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
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

        public void setId(int id) {
            this.id = id;
        }
    }
}
