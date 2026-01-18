module org.example.memorie {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.memorie to javafx.fxml, javafx.base;
    exports org.example.memorie;
}
