module com.example.mediaplayerjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires mp3agic;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.mediaplayerjfx to javafx.fxml;
    exports com.example.mediaplayerjfx;
}
