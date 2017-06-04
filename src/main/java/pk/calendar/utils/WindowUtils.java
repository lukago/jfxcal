package pk.calendar.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created on 6/2/2017.
 */
public final class WindowUtils {

    private WindowUtils() {
    }

    public static File createPathPicker() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/data/"));

        return fileChooser.showOpenDialog(new Stage());
    }

    public static Alert createAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                msg, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Exit");
        Label img = new Label();
        img.getStyleClass().addAll("alert", "error", "dialog-pane");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/assets/calendar-icon.png"));
        alert.showAndWait();
        return alert;
    }

}
