package pk.calendar.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pk.calendar.models.DateEvent;
import pk.calendar.models.EventManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created on 5/21/2017.
 */
public class EventController {

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
    private SimpleStringProperty eventDateStr;
    private LocalDate eventDate;
    private EventManager eventManager;
    private DateCell dc;

    public EventController(DateCell dc) {
        eventDateStr = new SimpleStringProperty();
        eventManager = EventManager.getInstance();
        setEventDate(dc.getItem());
        this.dc = dc;
    }

    @FXML
    public void initialize() {
        ObservableList<String> notify = FXCollections.observableArrayList("Never", "10 sec");
        notifySpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(notify));
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        minSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5));

        descField.setWrapText(true);
        dateLabel.textProperty().bind(eventDateStr);

        data = FXCollections.observableArrayList((eventManager.getEventsByDate(eventDate)));
        dateColumn.setCellValueFactory(o -> new SimpleStringProperty(o.getValue()
                .getDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd -> HH:mm"))));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        eventTable.setItems(data);
    }

    @FXML
    public void addEvent() {
        String desc = descField.getText();
        String place = placeField.getText();
        int hour = hourSpinner.getValue();
        int min = minSpinner.getValue();
        eventManager.addEvent(new DateEvent(eventDate, hour, min, desc, place));
        data.add(eventManager.getLastEventByDate(eventDate));

        placeField.clear();
        descField.clear();
        Event e = new EventsChangedEvent(EventsChangedEvent.ADDED);
        Event.fireEvent(dc, e);
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
        eventDateStr.setValue(eventDate.toString());
    }
}
