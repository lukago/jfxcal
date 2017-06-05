package pk.calendar.controllers;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.fortuna.ical4j.data.ParserException;
import pk.calendar.models.data.DateEvent;
import pk.calendar.models.data.EventManager;
import pk.calendar.models.data.EventsChangedEvent;
import pk.calendar.models.data.Settings;
import pk.calendar.models.storage.*;
import pk.calendar.views.DatePickerExt;
import pk.calendar.views.WindowUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 5/21/2017.
 * Controller for MainView.fxml. It also creates DatePickerExt and uses
 * its skin which is not included in fxml file.
 */
public class CallendarController {

    private final EventManager eventManager;
    private final NotifyPopupController notifyController;

    /**
     * View component not included in fxml
     */
    private DatePickerSkin dps;

    @FXML
    private BorderPane root;

    /**
     * Ctor. Loads data from db and initalizes popup controller.
     */
    public CallendarController() {
        eventManager = EventManager.getInstance();

        try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
            eventManager.initEvents(db.read());
        } catch (SQLException e) {
            WindowUtils.createErrorAlert("Could not connect to databse!");
            eventManager.initEvents(new HashSet<>());
        }

        notifyController = new NotifyPopupController();
        notifyController.initialize();
    }

    /**
     * Initializes fxml view.
     * Creates DatePickerExt and puts its skin to root.
     */
    @FXML
    public void initialize() {
        DatePicker dp = new DatePickerExt(LocalDate.now(), this);
        dps = new DatePickerSkin(dp);
        Node popupContent = dps.getPopupContent();
        root.setCenter(popupContent);
    }

    /**
     * Called when cell is created or updated.
     *
     * @param dc   DateCell to update
     * @param item date of dateCell
     */
    public void updateDateCell(DateCell dc, LocalDate item) {
        dc.setId(null);
        handleToday(dc, item);
        handleEventAdded(dc, item);
        handleEventDeleted(dc, item);
        addHandlers(dc, item);
    }

    /**
     * Sets close handling for stage.
     *
     * @param stage stage to handle
     */
    public void initStageActions(Stage stage) {
        stage.setOnCloseRequest(e -> handleSaveEventsToDB());
    }

    /**
     * Handle color chaning of DateCell.
     *
     * @param dc   DateCell to handle
     * @param item date of DateCell
     */
    private void handleToday(DateCell dc, LocalDate item) {
        if (item.isEqual(LocalDate.now())) {
            if (!eventManager.getEventsByDate(item).isEmpty()) {
                dc.setId("date-cell-today-event-"+Settings.getData().cellColor);
            } else {
                dc.setId("date-cell-today");
            }
        }
    }

    /**
     * Handles updating DateCell after adding new DateEvent.
     *
     * @param dc   DateCell to handle
     * @param item date of DateCell
     */
    private void handleEventAdded(DateCell dc, LocalDate item) {
        if (!eventManager.getEventsByDate(item).isEmpty()) {
            dc.setId("date-cell-event-"+ Settings.getData().cellColor);
        }

        notifyController.handleEventsAdded(item);
    }

    /**
     * Handles updating DateCell after deleting DateEvent.
     *
     * @param dc   DateCell to handle
     * @param item date of DateCell
     */
    private void handleEventDeleted(DateCell dc, LocalDate item) {
        if (eventManager.getEventsByDate(item).isEmpty()) {
            if (item.isEqual(LocalDate.now())) {
                dc.setId("date-cell-today");
            } else {
                dc.setId("date-cell-default");
            }
        }

        notifyController.handleEventsDeleted();
    }

    /**
     * Add handlers for custom EventsChangedEvent, click and key pressed.
     *
     * @param dc   DateCell to handle
     * @param item date of DateCell
     */
    private void addHandlers(DateCell dc, LocalDate item) {
        dc.addEventHandler(EventsChangedEvent.ADDED,
                e -> handleEventAdded(dc, item));
        dc.addEventHandler(EventsChangedEvent.DELETED,
                e -> handleEventDeleted(dc, item));
        dc.setOnMouseClicked(e -> handleCellEvent(dc, e));
        dc.setOnKeyPressed(e -> handleCellEvent(dc, e));
    }

    /**
     * Handles mouse click on cell.
     *
     * @param dc DateCell to handle
     * @param e  MouseEvent of click
     */
    public void handleCellEvent(DateCell dc, MouseEvent e) {
        if (e.getClickCount() == 2) {
            createEventMenu(dc);
        }
    }

    /**
     * Handles keyboard enter pressed on cell.
     *
     * @param dc DateCell to handle
     * @param e  KeyEvent of press
     */
    public void handleCellEvent(DateCell dc, KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            createEventMenu(dc);
        }
    }

    /**
     * Method called on close. Cleans up notifyController and saves
     * added DateEvents to db if user wanted.
     */
    private void handleSaveEventsToDB() {
        // cancel all TimerTasks
        notifyController.close();

        // ask user to save events to db
        Alert alert = WindowUtils.createAlert("Save event changes to database?");
        if (alert.getResult() == ButtonType.YES) {
            try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
                db.delete(eventManager.getEventsDeleted());
                db.write(eventManager.getEventsAdded());
            } catch (SQLException e) {
                String msg = "Could not connect to databse!. No changes saved.";
                WindowUtils.createErrorAlert(msg);
            }
        }
    }

    /**
     * Handler for load from xml submenu.
     */
    @FXML
    void loadFromXML() {
        File file = WindowUtils.createPathPicker();

        if (file != null) {
            String path = file.getPath();
            try (XMLDateEventDao xml = DateEventDaoFactory.getXMLDao(path)) {
                Set<DateEvent> xmlnew = xml.read();
                eventManager.addEvents(xmlnew);

                EventsChangedEvent event =
                        new EventsChangedEvent(EventsChangedEvent.ADDED);
                getDateCells().forEach(o -> o.fireEvent(event));
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handler for load from xml submenu.
     */
    @FXML
    void loadFromICS() {
        File file = WindowUtils.createPathPicker();

        if (file != null) {
            String path = file.getPath();
            try (ICSDateEventDao ics = DateEventDaoFactory.getICSDao(path)) {
                EventsChangedEvent event =
                        new EventsChangedEvent(EventsChangedEvent.ADDED);
                Set<DateEvent> icsnew = ics.read();

                eventManager.addEvents(icsnew);
                getDateCells().forEach(o -> o.fireEvent(event));
            } catch (IOException | ParserException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to create "Events of" window by handleCellEvent method.
     *
     * @param dc clicked DateCell to pass it to EventController
     */
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

    /**
     * Used to create "Events filer" window. Called after submenu click.
     */
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

    /**
     * Used to create "Settings" window. Called after submenu click.
     */
    @FXML
    private void createSettingsWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/fxml/SettingsView.fxml"));
            fxmlLoader.setControllerFactory(
                    c -> new SettingsController(this));
            GridPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/assets/calendar-icon.png"));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void createAboutWindow() {
        WindowUtils.createAboutWindow();
    }

    /**
     * Uses css to lookup for DatePickerSkin DateCells.
     *
     * @return list of currently displayed DateCells
     */
    public List<DateCell> getDateCells() {
        return dps.getPopupContent()
                .lookupAll(".day-cell")
                .stream()
                .map(n -> (DateCell) n)
                .collect(Collectors.toList());
    }
}
