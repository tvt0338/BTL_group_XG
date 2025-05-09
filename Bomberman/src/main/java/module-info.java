module bomberman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens org.example.login to javafx.fxml;
    opens main.module.map to javafx.fxml;

    exports org.example.login;
    exports main.module.map;
    exports main.module.map.collision;
    opens main.module.map.collision to javafx.fxml;
    exports main.module.map.entities;
    opens main.module.map.entities to javafx.fxml;
    exports main.module.map.tile;
    opens main.module.map.tile to javafx.fxml;
    exports main.module.map.Utils;
    opens main.module.map.Utils to javafx.fxml;
}