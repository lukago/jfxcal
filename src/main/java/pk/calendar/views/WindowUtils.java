package pk.calendar.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created on 6/2/2017.
 * Utility static class.
 */
public final class WindowUtils {

    private WindowUtils() {
    }

    /**
     * Ceates path picker window.
     * @return picked path bu user
     */
    public static File createPathPicker() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(new Stage());
    }

    /**
     * Creates Alert window.
     * @param msg message of alert
     * @return created Alert
     */
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

    public static void createErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText("Exception occured");
        alert.setTitle("Error");
        Label img = new Label();
        img.getStyleClass().addAll("alert", "error", "dialog-pane");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/assets/calendar-icon.png"));
        alert.show();
    }

    public static void createAboutWindow() {
        String msg =
                "Features: \n-date events \n- event filters" +
                "\n- ICS/XML format save/load \n- notifications" +
                "\n- configurable settings \n- database for events" +
                "\n\n\nThis software is provided under MIT license" +
                "\n\nAuthors: " + "\nLukasz Golebiewski 203882" +
                "\nJakub Mielczarek 203943";

        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText("Callendar application v1.0 2017");
        alert.setTitle("About");
        Label img = new Label();
        img.getStyleClass().addAll("alert", "error", "dialog-pane");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/assets/calendar-icon.png"));
        alert.show();
    }

}
