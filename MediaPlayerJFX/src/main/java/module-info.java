module com.example.mediaplayerjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.mediaplayerjfx to javafx.fxml;
    exports com.example.mediaplayerjfx;
}