package pk.calendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pk.calendar.controllers.CallendarController;

public class CalendarApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        BorderPane root = loader.load();

        Scene scene = new Scene(root, 1000, 600);

        stage.setScene(scene);
        stage.setTitle("Calendar");
        stage.getIcons().add(new Image("/assets/calendar-icon.png"));

        CallendarController cc = loader.getController();
        cc.initStageActions(stage);

        stage.show();
    }
}
