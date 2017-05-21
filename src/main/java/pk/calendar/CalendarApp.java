package pk.calendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CalendarApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

        Scene scene = new Scene(root, 1000, 600);

        stage.setScene(scene);
        stage.setTitle("Calendar");
        stage.getIcons().add(new Image("/assets/calendar-icon.png"));
        stage.show();
    }
}
