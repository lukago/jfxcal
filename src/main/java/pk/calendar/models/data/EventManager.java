package pk.calendar.models.data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 5/22/2017.
 */
public class EventManager {

    private final static EventManager instance = new EventManager();
    private final Set<DateEvent> events;
    private final Set<DateEvent> eventsAdded;
    private final Set<DateEvent> eventsDeleted;

    private EventManager() {
        events = new HashSet<>();
        eventsAdded = new HashSet<>();
        eventsDeleted = new HashSet<>();
    }

    public static EventManager getInstance() {
        return instance;
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

    public void addAllEvents(Set<DateEvent> eventsToAdd) {
        for (DateEvent e : eventsToAdd) {
            events.add(e);
            eventsAdded.add(e);
        }
    }

    public void initEvents(Set<DateEvent> eventsToAdd) {
        events.addAll(eventsToAdd);
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
