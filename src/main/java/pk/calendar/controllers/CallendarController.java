package pk.calendar.controllers;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pk.calendar.models.EventManager;
import pk.calendar.models.EventsChangedEvent;
import pk.calendar.models.storage.DBDateEventDao;
import pk.calendar.models.storage.DateEventDaoFactory;
import pk.calendar.views.DatePickerExt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 5/21/2017.
 */
public class CallendarController {

    @FXML
    private BorderPane root;

    private DatePicker dp;
    private DatePickerSkin dps;

    @FXML
    public void initialize() {
        dp = new DatePickerExt(LocalDate.now(), this);
        dps = new DatePickerSkin(dp);
        Node popupContent = dps.getPopupContent();
        root.setCenter(popupContent);
    }

    public void updateDateCell(DateCell dc, LocalDate item) {
        dc.setId(null);
        handleToday(dc, item);
        handleEventPresent(dc, item);
        handleEventNoPresent(dc, item);
        handleEventsAdded(dc, item);
    }

    public void initStageActions(Stage stage) {
        stage.setOnCloseRequest(e -> handleSaveEventsToDB());
    }

    private void handleToday(DateCell dc, LocalDate item) {
        if (item.isEqual(LocalDate.now())) {
            if (!EventManager.getInstance().getEventsByDate(item).isEmpty()) {
                dc.setId("date-cell-today-event");
            } else {
                dc.setId("date-cell-today");
            }
        }
    }

    private void handleEventPresent(DateCell dc, LocalDate item) {
        if (!EventManager.getInstance().getEventsByDate(item).isEmpty()) {
            dc.setId("date-cell-event");
        }
    }

    private void handleEventNoPresent(DateCell dc, LocalDate item) {
        if (EventManager.getInstance().getEventsByDate(item).isEmpty()) {
            if (item.isEqual(LocalDate.now())) {
                dc.setId("date-cell-today");
            } else {
                dc.setId("date-cell-default");
            }
        }
    }

    private void handleEventsAdded(DateCell dc, LocalDate item) {
        dc.addEventHandler(EventsChangedEvent.ADDED,
                e -> handleEventPresent(dc, item));
        dc.addEventHandler(EventsChangedEvent.DELETED,
                e -> handleEventNoPresent(dc, item));
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
        Alert alert = createAlert("Save changes to database?");
        if (alert.getResult() == ButtonType.YES) {
            try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
                db.delete(EventManager.getInstance().getEventsDeleted());
                db.write(EventManager.getInstance().getEventsAdded());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Alert createAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                msg, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Exit");
        Label img = new Label();
        img.getStyleClass().addAll("alert", "error", "dialog-pane");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass()
                .getResource("/assets/calendar-icon.png").toString()));
        alert.showAndWait();
        return alert;
    }

    private void createEventMenu(DateCell dc) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/fxml/EventView.fxml"));
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

    @FXML
    private void createEventFilter() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/fxml/EventFilterView.fxml"));
            fxmlLoader.setControllerFactory(
                    c -> new EventFilterController(this));
            GridPane root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            Stage stage = new Stage();
            stage.setTitle("Event filter");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/assets/calendar-icon.png"));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public List<DateCell> getDateCells() {
        return dps.getPopupContent()
                .lookupAll(".day-cell")
                .stream()
                .map(n -> (DateCell) n)
                .collect(Collectors.toList());
    }
}
