package pk.calendar.controllers;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pk.calendar.controllers.storage.DBDateEventDao;
import pk.calendar.controllers.storage.DateEventDaoFactory;
import pk.calendar.models.EventManager;
import pk.calendar.views.DatePickerExt;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created on 5/21/2017.
 */
public class CallendarController {

    @FXML
    private BorderPane root;

    @FXML
    public void initialize() {
        DatePickerExt dp = new DatePickerExt(LocalDate.now(), this);
        DatePickerSkin dps = new DatePickerSkin(dp);
        Node popupContent = dps.getPopupContent();
        root.setCenter(popupContent);
    }

    public void updateDateCell(DateCell dc, LocalDate item) {
        dc.setId(null);
        handleEventPresent(dc, item);
        handleToday(dc, item);
        handleEventsAdded(dc, item);
    }

    public void initStageActions(Stage stage) {
        stage.setOnCloseRequest(e -> handleSaveEventsToDB());
    }

    private void handleToday(DateCell dc, LocalDate item) {
        if (item.isEqual(LocalDate.now())) {
            dc.setId("date-cell-today");
            dc.setOnMouseEntered(event -> dc.setId("date-cell-entered"));
            dc.setOnMouseExited(event -> dc.setId("date-cell-today"));
        }
    }

    private void handleEventPresent(DateCell dc, LocalDate item) {
        if (!EventManager.getInstance().getEventsByDate(item).isEmpty()) {
            dc.setId("date-cell-event");
            dc.setVisible(false);
            dc.setVisible(true);
        }
    }

    private void handleEventsAdded(DateCell dc, LocalDate item) {
        dc.addEventHandler(EventsChangedEvent.ADDED,
                e -> handleEventPresent(dc, item));
    }

    public void handleCellEvent(DateCell dc, MouseEvent e) {
        if (e.getClickCount() == 2) {
            createEventMenu(dc);
        }
    }

    public void handleCellEvent(DateCell dc, KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            createEventMenu(dc);
        }
    }

    private void handleSaveEventsToDB() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Save added events to database?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Exit");
        Label img = new Label();
        img.getStyleClass().addAll("alert", "error", "dialog-pane");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/assets/calendar-icon.png").toString()));
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
                db.write(EventManager.getInstance().getEventsAdded());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createEventMenu(DateCell dc) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EventView.fxml"));
            fxmlLoader.setControllerFactory(c -> new EventController(dc));
            GridPane root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            Stage stage = new Stage();
            stage.setTitle("Events of " + dc.getItem());
            stage.setScene(scene);
            stage.getIcons().add(new Image("/assets/calendar-icon.png"));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
