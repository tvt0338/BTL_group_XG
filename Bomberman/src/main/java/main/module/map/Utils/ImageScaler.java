package main.module.map.Utils;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageScaler {
    public Image scale(Image original, int width, int height) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,width,height);
        gc.drawImage(original, 0, 0, width, height);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        WritableImage scaledImage = new WritableImage(width, height);
        canvas.snapshot(params, scaledImage);
        return scaledImage;
    }
}
