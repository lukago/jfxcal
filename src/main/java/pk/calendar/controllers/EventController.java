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

/**
 * Created on 5/21/2017.
 */
public class EventController {

    @FXML
    private Label dateLabel;

    @FXML
    private TextField descField;

    @FXML
    private TableView<DateEvent> eventTable;

    @FXML
    private TableColumn<DateEvent, LocalDate> dateColumn;

    @FXML TableColumn<DateEvent, String> descColumn;

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
        dateLabel.textProperty().bind(eventDateStr);
        data = FXCollections.observableArrayList((eventManager.getEventsByDate(eventDate)));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        eventTable.setItems(data);
    }

    @FXML
    public void addEvent() {
        String desc = descField.getText();
        eventManager.addEvent(new DateEvent(eventDate, desc));
        data.add(new DateEvent(eventDate, desc));
        Event e = new EventsChangedEvent(EventsChangedEvent.ADDED);
        Event.fireEvent(dc, e);
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
        eventDateStr.setValue(eventDate.toString());
    }
}
