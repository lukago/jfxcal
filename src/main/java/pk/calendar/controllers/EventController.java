package pk.calendar.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pk.calendar.models.data.DateEvent;
import pk.calendar.models.data.EventManager;
import pk.calendar.models.data.EventsChangedEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created on 5/21/2017.
 * Controller for EventView.fxml. This window is created after click on datcell.
 */
public class EventController {

    private final EventManager eventManager;
    private final DateCell dc;

    /**
     * String property binded to dateLabel. DateLabel cannot be initialized
     * in ctor so it has to use this property to be initialized properly with
     * dateCell date.
     */
    private final SimpleStringProperty eventDateStr;

    @FXML
    private Label dateLabel;
    @FXML
    private TextArea descField;
    @FXML
    private TextField placeField;
    @FXML
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, String> dateColumn;
    @FXML
    private TableColumn<DateEvent, String> descColumn;
    @FXML
    private TableColumn<DateEvent, String> placeColumn;
    @FXML
    private Spinner<Integer> hourSpinner;
    @FXML
    private Spinner<Integer> minSpinner;
    @FXML
    private Spinner<String> notifySpinner;
    private ObservableList<DateEvent> data;
    private LocalDate eventDate;

    /**
     * Ctor. This class has no zero argument ctor so it has to be set by
     * setControllerFactory with fxml loader when creating this window.
     *
     * @param dc DateCell which called this ctor.
     */
    public EventController(DateCell dc) {
        eventDateStr = new SimpleStringProperty();
        eventManager = EventManager.getInstance();
        setEventDate(dc.getItem());
        this.dc = dc;
    }

    /**
     * Initializes spinners and tableView factories and properties.
     */
    @FXML
    public void initialize() {
        int currHour = LocalDateTime.now().getHour();
        int currMin = LocalDateTime.now().getMinute();

        ObservableList<String> notify =
                FXCollections.observableArrayList("Never", "10 sec");
        notifySpinner.setValueFactory(
                new SpinnerValueFactory.ListSpinnerValueFactory<>(notify));
        hourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, currHour));
        minSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, currMin, 1));


        descField.setWrapText(true);
        dateLabel.textProperty().bind(eventDateStr);

        data = FXCollections.observableArrayList(
                eventManager.getEventsByDate(eventDate));

        dateColumn.setCellValueFactory(o -> new SimpleStringProperty(
                o.getValue().getDateTime().format(DateTimeFormatter
                        .ofPattern("HH:mm"))));
        descColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        placeColumn.setCellValueFactory(
                new PropertyValueFactory<>("place"));

        eventTable.setItems(data);
        eventTable.setOnKeyPressed(this::handleDelete);
    }

    /**
     * Handles deleting events with TableView by DEL key.
     * It notifies dateCell about it to update it.
     * @param e pressed key
     */
    private void handleDelete(KeyEvent e) {

        if (e.getCode().equals(KeyCode.DELETE)) {
            if (eventTable.getSelectionModel() != null) {
                DateEvent de = eventTable.getSelectionModel().getSelectedItem();
                data.remove(de);
                eventManager.deleteEvents(de);
                Event.fireEvent(dc,
                        new EventsChangedEvent(EventsChangedEvent.DELETED));
            }
        }
    }

    /**
     * Handles "Add event" button.
     */
    @FXML
    public void addEvent() {
        String desc = descField.getText();
        String place = placeField.getText();
        int hour = hourSpinner.getValue();
        int min = minSpinner.getValue();
        int secMinus = parseNotifySpinner(notifySpinner.getValue());
        DateEvent newDateEvent =
                new DateEvent(eventDate, hour, min, secMinus, place, desc);
        eventManager.addEvents(newDateEvent);
        data.add(newDateEvent);
        placeField.clear();
        descField.clear();

        Event.fireEvent(dc, new EventsChangedEvent(EventsChangedEvent.ADDED));
    }

    /**
     * Sets event date and its string representation property used by view.
     */
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
        eventDateStr.setValue(eventDate.toString());
    }

    /**
     * Parses spinner value to seconds
     *
     * @param value string value of spinner
     * @return int number of seconds
     */
    private int parseNotifySpinner(String value) {
        if (value.equals("10 sec")) {
            return 10;
        }
        return 0;
    }
}
