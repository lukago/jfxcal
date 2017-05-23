package pk.calendar.controllers;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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

    private void handleToday(DateCell dc, LocalDate item) {
        if (item.isEqual(LocalDate.now())) {
            dc.setId("date-cell-today");
            dc.setOnMouseEntered(event -> dc.setId("date-cell-entered"));
            dc.setOnMouseExited(event -> dc.setId("date-cell-today"));
        }
    }

    private void handleEventPresent(DateCell dc, LocalDate item) {
        if (EventManager.getInstance().getEventsByDate(item).size() > 0) {
            dc.setId("date-cell-event");
            dc.setVisible(false);
            dc.setVisible(true);
        }
    }

    private void handleEventsAdded(DateCell dc, LocalDate item) {
        dc.addEventHandler(EventsChangedEvent.ADDED,
                e -> handleEventPresent(dc, item));
    }

    public void createEventMenu(DateCell dc, MouseEvent e) {
        if (e.getClickCount() == 2) {
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
}
