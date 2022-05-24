module com.example.prog2graphical {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires transitive java.desktop;
    requires javafx.swing;
    exports com.example.prog2graphical;
    exports primary;
}