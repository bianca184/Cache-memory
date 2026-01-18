package org.example.memorie;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class StartController {

    @FXML private ComboBox<String> modeBox;
    @FXML private TextField cacheSizeField;

    @FXML
    protected void onStart() throws IOException {
        String mode = modeBox.getValue();
        if (mode == null) {
            showError("Selectați modul de simulare.");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(cacheSizeField.getText());
            if (!isPowerOfTwo(size)) {
                showError("Dimensiunea cache-ului trebuie să fie o putere a lui 2 (4, 8, 16…).");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Introduceți o valoare numerică validă pentru dimensiune!");
            return;
        }

        openMainWindow(mode, size);


        Stage stage = (Stage) modeBox.getScene().getWindow();
        stage.close();
    }

    private boolean isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private void openMainWindow(String mode, int size) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cache-view.fxml"));
        Scene scene = new Scene(loader.load(), 900, 500);


        scene.getStylesheets().add(
                getClass().getResource("/styles/flat-ui.css").toExternalForm()
        );

        CacheController controller = loader.getController();
        controller.initializeSimulator(mode, size);

        Stage stage = new Stage();
        stage.setTitle("Simulare Cache - " + mode);
        stage.setScene(scene);
        stage.show();
    }

}
