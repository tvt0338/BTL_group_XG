module main.map {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens Main_Modules to javafx.fxml;
    exports Main_Modules;
    exports Main_Modules.map01;
    opens Main_Modules.map01 to javafx.fxml;
}