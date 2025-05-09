package main.module.map.SetPosition;

import main.module.map.GameCanvas;
import main.module.map.Objects.OBJ_Heart;
import main.module.map.Objects.RangeItem;
import main.module.map.Objects.SpeedItem;
import main.module.map.entities.Entity;
import main.module.map.monster.Balloon;
import main.module.map.monster.Fly;
import main.module.map.monster.Oneal;
import main.module.map.monster.Tanker;
import main.module.map.monster.Void;

import java.util.*;

public class AssetSetter {

    GameCanvas assetCanvas;

    public AssetSetter(GameCanvas assetCanvas) {
        this.assetCanvas = assetCanvas;
    }

    public void setObject() {
//        Random random = new Random();
//        List<int[]> spawnPoints = new ArrayList<>(assetCanvas.tileM.spawnPoints);
//        Collections.shuffle(spawnPoints, random);
////        assetCanvas.obj[0] = new OBJ_Heart(assetCanvas);
////        assetCanvas.obj[0].worldX = assetCanvas.tileSize * 15;
////        assetCanvas.obj[0].worldY = assetCanvas.tileSize * 5;
////
////        assetCanvas.obj[1] = new OBJ_Heart(assetCanvas);
////        assetCanvas.obj[1].worldX = assetCanvas.tileSize * 2;
////        assetCanvas.obj[1].worldY = assetCanvas.tileSize * 11;
//
//        int itemCount = Math.min(4, spawnPoints.size());
//        int speedItemIndex = 0;
//        int rangeItemIndex = 0;
//
//        for (int i = 0; i < itemCount && i < spawnPoints.size(); i++) {
//            int[] spawnPoint = spawnPoints.get(i);
//            int col = spawnPoint[0];
//            int row = spawnPoint[1];
//
//            if (i % 2 == 0 && speedItemIndex < 2) {
//                assetCanvas.obj[i + 2] = new SpeedItem(assetCanvas);
//                assetCanvas.obj[i + 2].worldX = col * assetCanvas.tileSize;
//                assetCanvas.obj[i + 2].worldY = row * assetCanvas.tileSize;
//                speedItemIndex++;
//            } else if (rangeItemIndex < 2) {
//                assetCanvas.obj[i + 2] = new RangeItem(assetCanvas);
//                assetCanvas.obj[i + 2].worldX = col * assetCanvas.tileSize;
//                assetCanvas.obj[i + 2].worldY = row * assetCanvas.tileSize;
//                rangeItemIndex++;
//            }
//        }

        Random random = new Random();
        List<int[]> spawnPoints = new ArrayList<>(assetCanvas.tileM.spawnPoints);

        int playerCol = assetCanvas.player.worldX / assetCanvas.tileSize;
        int playerRow = assetCanvas.player.worldY / assetCanvas.tileSize;

        spawnPoints.removeIf(point -> point[0] == playerCol && point[1] == playerRow);
        Collections.shuffle(spawnPoints, random);
        Set<String> usedPositions = new HashSet<>();
        usedPositions.add(playerCol + "," + playerRow);

//        int monsterCount = 0;

        List<Class<? extends Entity>> Items = new ArrayList<>();
        Collections.addAll(Items, RangeItem.class, SpeedItem.class);

        int numItems = Math.min(8, spawnPoints.size());
//        System.out.println("Số vị trí spawn khả dụng: " + spawnPoints.size());
//        System.out.println("Số quái  dự kiến spawn: " + numMonsters);
//
//        int flyCount = 0;
//        int maxFly = 10;
//        int balloonCount = 0;
//        int maxBalloon = 10;
        int objIndex = 0;
        for (int i = 0; i < numItems && i < spawnPoints.size() &&
                objIndex < assetCanvas.obj.length; i++) {
            int[] spawnPoint = spawnPoints.get(i);
            int col = spawnPoint[0];
            int row = spawnPoint[1];
            String positionKey = col + ", " + row;
            if (!usedPositions.contains(positionKey)) {
                try {
                    Class<? extends Entity> itemType = Items.get(random.nextInt(Items.size()));
                    assetCanvas.obj[objIndex] = itemType.getConstructor(GameCanvas.class).newInstance(assetCanvas);
                    assetCanvas.obj[objIndex].worldX = col * assetCanvas.tileSize;
                    assetCanvas.obj[objIndex].worldY = row * assetCanvas.tileSize;
                    System.out.println("Spawn item: " + itemType.getSimpleName() + " tại (" + col + ", " + row + ")");
                    usedPositions.add(positionKey);
                    objIndex++;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Lỗi spawn item tại: " + i);
                }
            }
        assetCanvas.usedPositions = usedPositions;
    }
//        assetCanvas.obj[0] = new OBJ_Key(assetCanvas);
//        assetCanvas.obj[0].worldX = 23 * assetCanvas.tileSize;
//        assetCanvas.obj[0].worldY = 7 * assetCanvas.tileSize;
//
//        assetCanvas.obj[1] = new OBJ_Key(assetCanvas);
//        assetCanvas.obj[1].worldX = 23 * assetCanvas.tileSize;
//        assetCanvas.obj[1].worldY = 40 * assetCanvas.tileSize;
//
//        assetCanvas.obj[2] = new OBJ_Key(assetCanvas);
//        assetCanvas.obj[2].worldX = 38 * assetCanvas.tileSize;
//        assetCanvas.obj[2].worldY = 8 * assetCanvas.tileSize;
//
//        assetCanvas.obj[3] = new OBJ_Door(assetCanvas);
//        assetCanvas.obj[3].worldX = 10 * assetCanvas.tileSize;
//        assetCanvas.obj[3].worldY = 11 * assetCanvas.tileSize;
//
//        assetCanvas.obj[4] = new OBJ_Door(assetCanvas);
//        assetCanvas.obj[4].worldX = 8 * assetCanvas.tileSize;
//        assetCanvas.obj[4].worldY = 28 * assetCanvas.tileSize;
//
//        assetCanvas.obj[5] = new OBJ_Door(assetCanvas);
//        assetCanvas.obj[5].worldX = 12 * assetCanvas.tileSize;
//        assetCanvas.obj[5].worldY = 22 * assetCanvas.tileSize;
//
//        assetCanvas.obj[6] = new OBJ_Chest(assetCanvas);
//        assetCanvas.obj[6].worldX = 10 * assetCanvas.tileSize;
//        assetCanvas.obj[6].worldY = 7 * assetCanvas.tileSize;
//
//        assetCanvas.obj[7] = new OBJ_Boots(assetCanvas);
//        assetCanvas.obj[7].worldX = 37 * assetCanvas.tileSize;
//        assetCanvas.obj[7].worldY = 42 * assetCanvas.tileSize;    }
    }

    public void setNPC() {
//        assetCanvas.npc[0] = new NPC_OldMan(assetCanvas);
//        assetCanvas.npc[0].worldX = assetCanvas.tileSize*21;
//        assetCanvas.npc[0].worldY = assetCanvas.tileSize*21;
//        assetCanvas.monsters[0] = new Balloon(assetCanvas);
//        assetCanvas.monsters[0].worldX = assetCanvas.tileSize * 7;
//        assetCanvas.monsters[0].worldY = assetCanvas.tileSize * 6;
//
//
//        assetCanvas.monsters[1] = new Balloon(assetCanvas);
//        assetCanvas.monsters[1].worldX = assetCanvas.tileSize * 7;
//        assetCanvas.monsters[1].worldY = assetCanvas.tileSize * 12;
        Random random = new Random();
        List<int[]> spawnPoints = new ArrayList<>(assetCanvas.tileM.spawnPoints);

        int playerCol = assetCanvas.player.worldX / assetCanvas.tileSize;
        int playerRow = assetCanvas.player.worldY / assetCanvas.tileSize;

        spawnPoints.removeIf(point -> point[0] == playerCol && point[1] == playerRow);
        Collections.shuffle(spawnPoints, random);
        Set<String> usedPositions = assetCanvas.usedPositions != null ?
                assetCanvas.usedPositions : new HashSet<>();
        usedPositions.add(playerCol + "," + playerRow);

//        int monsterCount = 0;

        List<Class<? extends Entity>> weightedMonsters = new ArrayList<>();
        Collections.addAll(weightedMonsters, Fly.class,
                Balloon.class, Void.class, Oneal.class, Tanker.class);

        int numMonsters = Math.min(15, spawnPoints.size());
//        System.out.println("Số vị trí spawn khả dụng: " + spawnPoints.size());
//        System.out.println("Số quái  dự kiến spawn: " + numMonsters);
//
//        int flyCount = 0;
//        int maxFly = 10;
//        int balloonCount = 0;
//        int maxBalloon = 10;
        int monsterIndex = 0;
        for (int i = 0; i < numMonsters && i < spawnPoints.size() &&
                monsterIndex < assetCanvas.monsters.length; i++) {
            int[] spawnPoint = spawnPoints.get(i);
            int col = spawnPoint[0];
            int row = spawnPoint[1];
            String positionKey = col + ", " + row;
            if (!usedPositions.contains(positionKey)) {
                try {
                    Class<? extends Entity> monsterType = weightedMonsters.get(random.nextInt(weightedMonsters.size()));
                    assetCanvas.monsters[monsterIndex] = monsterType.getConstructor(GameCanvas.class).newInstance(assetCanvas);
                    assetCanvas.monsters[monsterIndex].worldX = col * assetCanvas.tileSize;
                    assetCanvas.monsters[monsterIndex].worldY = row * assetCanvas.tileSize;
                    System.out.println("Spawn quái: " + monsterType.getSimpleName() + " tại (" + col + ", " + row + ")");
                    usedPositions.add(positionKey);
                    monsterIndex++;

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Loi spawn: " + i);
                }
            }
        }
        assetCanvas.usedPositions = usedPositions;
    }

}
