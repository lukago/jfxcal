package pk.calendar.models;

import pk.calendar.models.storage.DBDateEventDao;
import pk.calendar.models.storage.DateEventDaoFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 5/22/2017.
 */
public class EventManager {

    private Set<DateEvent> events;

    private final static EventManager instance = new EventManager();


    private Set<DateEvent> eventsAdded;
    private Set<DateEvent> eventsDeleted;

    private EventManager() {
        this.events = getEventsFromDb();
        eventsAdded = new HashSet<>();
        eventsDeleted = new HashSet<>();
    }

    public static EventManager getInstance() {
        return instance;
    }

    private Set<DateEvent> getEventsFromDb() {
        try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
            return db.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<DateEvent> getEventsByDate(LocalDate date) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.toSet());
    }

    public Set<DateEvent> getEventsBetween(LocalDate start, LocalDate end) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate()
                        .isAfter(start.minusDays(1))
                        && e.getDateTime().toLocalDate()
                        .isBefore(end.plusDays(1)))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void deleteEvents(Set<DateEvent> eventsToDelete) {
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

    public Set<DateEvent> getEvents() {
        return events;
    }

    public Set<DateEvent> getEventsAdded() {
        return eventsAdded;
    }

    public Set<DateEvent> getEventsDeleted() {
        return eventsDeleted;
    }

}
