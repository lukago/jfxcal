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
import pk.calendar.models.data.DateEvent;
import pk.calendar.models.data.EventManager;
import pk.calendar.models.data.EventsChangedEvent;
import pk.calendar.models.storage.Dao;
import pk.calendar.models.storage.DateEventDaoFactory;
import pk.calendar.views.WindowUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 5/27/2017.
 */
public class EventFilterController {

    private final EventManager eventManager;
    private final CallendarController cc;

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

    /**
     * date start picked by user
     */
    private LocalDate start;
    /**
     * date end picked by user
     */
    private LocalDate end;
    /**
     * observable data for table view
     */
    private ObservableList<DateEvent> data;

    /**
     * Ctor. This class has no zero argument ctor so it has to be set by
     * setControllerFactory with fxml loader when creating this window.
     *
     * @param cc this class uses and notifies main controller
     */
    public EventFilterController(CallendarController cc) {
        eventManager = EventManager.getInstance();
        this.cc = cc;
    }

    /**
     * Initializes datePickers and tableView factories and properties.
     */
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

        eventTable.setOnKeyPressed(this::handleDelete);
        eventTable.setItems(data);
    }

    /**
     * Handles deleting events with TableView by DEL key. It notifies all
     * date cells about this forcing them to update.
     *
     * @param e pressed key
     */
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

    /**
     * Handler for delete button. It deletes all events that are in filter.
     * It notifies all date cells about this forcing them to update.
     */
    @FXML
    public void deleteEvents() {
        eventManager.deleteEvents(new HashSet<>(data));
        data.clear();
        List<DateCell> cells = cc.getDateCells();
        EventsChangedEvent event =
                new EventsChangedEvent(EventsChangedEvent.DELETED);
        cells.forEach(o -> o.fireEvent(event));
    }

    /**
     * Handler for save to XML button. It saves filtered events to
     * xml file.
     */
    @FXML
    public void saveToXML() {
        File file = WindowUtils.createPathPicker();
        String path = file.getPath();
        try (Dao<Set<DateEvent>> xml = DateEventDaoFactory.getXMLDao(path)) {
            Set<DateEvent> set = eventManager.getEventsBetween(start, end);
            xml.write(set);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler for save to ICS button. It saves filtered events to
     * ICS file.
     */
    @FXML
    public void saveToICS() {
        File file = WindowUtils.createPathPicker();
        String path = file.getPath();
        try (Dao<Set<DateEvent>> ics = DateEventDaoFactory.getICSDao(path)) {
            Set<DateEvent> set = eventManager.getEventsBetween(start, end);
            ics.write(set);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler for DatePickers actions. Updates tableView and date
     * properties.
     */
    private void updateEventTable() {
        start = datePickerStart.getValue();
        end = datePickerEnd.getValue();
        data = FXCollections.observableArrayList(
                eventManager.getEventsBetween(start, end));
        eventTable.setItems(data);
    }
}
