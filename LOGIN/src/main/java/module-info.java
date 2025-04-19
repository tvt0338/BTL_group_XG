module org.example.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.login to javafx.fxml;
    opens org.example.setting to javafx.fxml;
    opens org.example.authors to javafx.fxml;
    opens org.example.Help to javafx.fxml;

    exports org.example.login;
    exports org.example.setting;
    exports org.example.authors;
    exports org.example.Help;
}