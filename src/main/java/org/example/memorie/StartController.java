package org.example.memorie;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;
import javafx.event.ActionEvent;



public class StartController {

    @FXML
    protected void onDirectMapped(ActionEvent event) throws IOException {
        openMainWindow("Direct Mapped");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onFIFO(ActionEvent event) throws IOException {
        openMainWindow("Fully Associative (FIFO)");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void openMainWindow(String mode) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("cache-view.fxml"));
        Scene scene=new Scene(loader.load(), 600, 400);
        CacheController controller=loader.getController();
        controller.setMode(mode);
        Stage stage=new Stage();
        stage.setTitle("Simulare Cache " + mode);
        stage.setScene(scene);
        stage.show();


    }
}
