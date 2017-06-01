package pk.calendar.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pk.calendar.models.DateEvent;
import pk.calendar.models.EventManager;
import pk.calendar.models.EventsChangedEvent;
import pk.calendar.models.storage.DateEventDaoFactory;
import pk.calendar.models.storage.XMLDateEventDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 5/27/2017.
 */
public class EventFilterController {
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, String> dateColumn;
    @FXML
    private TableColumn<DateEvent, String> descColumn;
    @FXML
    private TableColumn<DateEvent, String> placeColumn;

    private LocalDate start;
    private LocalDate end;
    private EventManager eventManager;
    private ObservableList<DateEvent> data;
    private CallendarController cc;

    public EventFilterController(CallendarController cc) {
        eventManager = EventManager.getInstance();
        this.cc = cc;
    }

    @FXML
    public void initialize() {
        datePickerStart.setValue(LocalDate.now());
        datePickerEnd.setValue(LocalDate.now());

        datePickerStart.setOnAction(e -> updateEventTable());
        datePickerEnd.setOnAction(e -> updateEventTable());

        start = datePickerStart.getValue();
        end = datePickerEnd.getValue();

        data = FXCollections.observableArrayList(
                eventManager.getEventsBetween(start, end));
        dateColumn.setCellValueFactory(o -> new SimpleStringProperty(
                o.getValue().getDateTime().format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd -> HH:mm"))));
        descColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        placeColumn.setCellValueFactory(
                new PropertyValueFactory<>("place"));

        eventTable.setOnKeyPressed(e -> handleDelete(e));
        eventTable.setItems(data);
    }

    private void handleDelete(KeyEvent e) {
        if (e.getCode().equals(KeyCode.DELETE)
                && eventTable.getSelectionModel() != null) {
            DateEvent de = eventTable.getSelectionModel().getSelectedItem();
            data.remove(de);
            eventManager.deleteEvents(de);
            for (DateCell dc : cc.getDateCells()) {
                Event.fireEvent(dc,
                        new EventsChangedEvent(EventsChangedEvent.DELETED));
            }

        }
    }

    @FXML
    public void deleteEvents() {
        eventManager.deleteEvents(new HashSet<>(data));
        data.clear();
        List<DateCell> cells = cc.getDateCells();
        for (DateCell c : cells) {
            Event.fireEvent(c, new EventsChangedEvent(EventsChangedEvent.DELETED));
        }
    }

    @FXML
    public void saveToXML() {
        String path = "src/main/resources/data/cal.xml";
        try (XMLDateEventDao xml = DateEventDaoFactory.getXMLDao(path)) {
            Set<DateEvent> set = eventManager.getEventsBetween(start, end);
            xml.write(set);
        }
    }

    private void updateEventTable() {
        start = datePickerStart.getValue();
        end = datePickerEnd.getValue();
        data = FXCollections.observableArrayList(
                eventManager.getEventsBetween(start, end));
        eventTable.setItems(data);
    }
}
