package pk.calendar.models;

import pk.calendar.models.storage.DBDateEventDao;
import pk.calendar.models.storage.DateEventDaoFactory;

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
    private List<DateEvent> eventsAdded;
    private List<DateEvent> eventsDeleted;

    private EventManager() {
        this.events = getEventsFromDb();
        eventsAdded = new ArrayList<>();
        eventsDeleted = new ArrayList<>();
    }

    public static EventManager getInstance() {
        return instance;
    }

    private List<DateEvent> getEventsFromDb() {
        try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
            return db.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DateEvent> getEventsByDate(LocalDate date) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public List<DateEvent> getEventsBetween(LocalDate start, LocalDate end) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate()
                        .isAfter(start.minusDays(1))
                        && e.getDateTime().toLocalDate()
                        .isBefore(end.plusDays(1)))
                .collect(Collectors.toList());
    }

    public void deleteEvents(List<DateEvent> eventsToDelete) {
        for (DateEvent eDel : eventsToDelete) {
            events.remove(eDel);
            eventsDeleted.add(eDel);
        }
    }

    public void deleteEvents(DateEvent... eventsToDelete) {
        for (DateEvent eDel : eventsToDelete) {
            events.remove(eDel);
            eventsDeleted.add(eDel);
        }
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

    public List<DateEvent> getEventsDeleted() {
        return eventsDeleted;
    }

}
