package pk.calendar.models;

import pk.calendar.controllers.storage.DBDateEventDao;
import pk.calendar.controllers.storage.DateEventDaoFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 5/22/2017.
 */
public class EventManager {

    private final static EventManager instance = new EventManager();
    private List<DateEvent> events;
    private  List<DateEvent> eventsAdded;

    private EventManager() {
        this.events = getEventsFromDb();
        eventsAdded = new ArrayList<>();
    }

    private List<DateEvent> getEventsFromDb() {
        try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
            return db.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EventManager getInstance() {
        return instance;
    }

    public List<DateEvent> getEventsByDate(LocalDate date) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public void addEvent(DateEvent event) {
        events.add(event);
        eventsAdded.add(event);
    }

    public List<DateEvent> getEvents() {
        return events;
    }

    public List<DateEvent> getEventsAdded() {
        return eventsAdded;
    }

}
