package pk.calendar;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pk.calendar.controllers.DatePickerExt;

import java.time.LocalDate;

public class CalendarApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

        DatePickerExt dp = new DatePickerExt(LocalDate.now());
        DatePickerSkin dps = new DatePickerSkin(dp);
        Node popupContent = dps.getPopupContent();

        root.setCenter(popupContent);

        Scene scene = new Scene(root, 1000, 600);

        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
    }
}
