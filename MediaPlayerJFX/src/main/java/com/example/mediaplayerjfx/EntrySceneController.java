package com.example.mediaplayerjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EntrySceneController implements Initializable {


    DataSingleton data_1 = DataSingleton.getInstance();
    @FXML
    private Button fileButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    
    @FXML
    public void handleButtonFileAction(ActionEvent actionEvent) throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            data_1.setFile(file);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            Stage stage = new Stage();
            stage.setTitle("MS Music Player");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(fileButton.getScene().getWindow());
            stage.show();
        }
    }
}
