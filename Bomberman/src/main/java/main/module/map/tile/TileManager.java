package main.module.map.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.module.map.GameCanvas;
import main.module.map.Utils.ImageScaler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    GameCanvas gcTile;
    public Tile[] tile;
    public int[][] mapTileNum;
    public List<int[]> spawnPoints = new ArrayList<int[]>();


    public TileManager(GameCanvas gcTile) {
        this.gcTile = gcTile;
        tile = new Tile[10];
        mapTileNum = new int[gcTile.maxWorldCol][gcTile.maxWorldRow];

        getTileImage();
    }



    public void getTileImage() {
//        tile[3] = new Tile();
//        tile[3].image = new Image(getClass().getResourceAsStream("/Images/Tile/destructiblewall.png"));
//        tile[3].collision = true;
//
//        tile[1] = new Tile();
//        tile[1].image = new Image(getClass().getResourceAsStream("/Images/Tile/wall.png"));
//        tile[1].collision = true;
//
//        tile[2] = new Tile();
//        tile[2].image = new Image(getClass().getResourceAsStream("/Images/Tile/grass.png"));

        setup(1, "wall", true);
        setup(2, "grass", false);
        setup(3, "destructiblewall", true);
    }

    public void setup(int index, String imagePath, boolean collision) {
        ImageScaler imageScaler = new ImageScaler();
        tile[index] = new Tile();

        Image originalImage = new Image(getClass().getResourceAsStream("/Images/Tile/" + imagePath + ".png"));
        tile[index].image = imageScaler.scale(originalImage, gcTile.tileSize, gcTile.tileSize);

        tile[index].collision = collision;
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.out.println("Không tìm thấy file: " + filePath);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            spawnPoints.clear();

            int row = 0;
            while (row < gcTile.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.trim().split("\\s+");

                if (numbers.length != gcTile.maxWorldCol) {
                    System.out.println("❌ Lỗi tại dòng " + row + ": có " + numbers.length + " phần tử, cần " + gcTile.maxWorldCol);
                    continue; // hoặc throw lỗi tùy bạn
                }

                for (int col = 0; col < gcTile.maxWorldCol; col++) {
                    int tileNum = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                    if (tileNum == 2) {
                        spawnPoints.add(new int[]{col, row});
                    }
                }

                row++;
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi load bản đồ từ: " + filePath);
        }
    }


    public void draw(GraphicsContext g2) {
//        g2.drawImage(tile[0].image, 0, 0, gcTile.tileSize, gcTile.tileSize);
//        g2.drawImage(tile[1].image, 48, 0, gcTile.tileSize, gcTile.tileSize);
//        g2.drawImage(tile[2].image, 96, 0, gcTile.tileSize, gcTile.tileSize);

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gcTile.maxWorldCol && worldRow < gcTile.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gcTile.tileSize;
            int worldY = worldRow * gcTile.tileSize;
            int screenX = worldX - gcTile.player.worldX + gcTile.player.screenX;
            int screenY = worldY - gcTile.player.worldY + gcTile.player.screenY;

            if (worldX + gcTile.tileSize > gcTile.player.worldX - gcTile.player.screenX &&
                    worldX - gcTile.tileSize < gcTile.player.worldX + gcTile.player.screenX &&
                    worldY + gcTile.tileSize > gcTile.player.worldY - gcTile.player.screenY &&
                    worldY - gcTile.tileSize < gcTile.player.worldY + gcTile.player.screenY) {

                if (tileNum != 0 && tile[tileNum] != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY);
                }
            }
            worldCol++;

            if (worldCol == gcTile.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
