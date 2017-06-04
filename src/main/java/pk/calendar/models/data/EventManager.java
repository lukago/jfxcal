package pk.calendar.models.data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 5/22/2017.
 * Manager for local storage of DateEvents.
 * Uses Singleton pattern.
 */
public class EventManager {

    /**
     * Singleton
     */
    private final static EventManager instance = new EventManager();

    /**
     * All DateEvents (loaded + added) - deleted.
     */
    private final Set<DateEvent> events;

    /**
     * Added events during running of application.
     */
    private final Set<DateEvent> eventsAdded;

    /**
     * Deleted events during running of application.
     */
    private final Set<DateEvent> eventsDeleted;

    /**
     * Ctor. Initializes sets with HashSets.
     */
    private EventManager() {
        events = new HashSet<>();
        eventsAdded = new HashSet<>();
        eventsDeleted = new HashSet<>();
    }

    /**
     * Gets singleton instance
     *
     * @return EvemtManager instance
     */
    public static EventManager getInstance() {
        return instance;
    }

    /**
     * Gets DateEvents at certain date.
     *
     * @param date date of events to search.
     * @return set of events matching given date.
     */
    public Set<DateEvent> getEventsByDate(LocalDate date) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.toSet());
    }

    /**
     * Gets DateEvents beetween dates [start, end]
     *
     * @param start start date - it includes this date too
     * @param end   end date - it includes this date too
     * @return set of events matching given dates.
     */
    public Set<DateEvent> getEventsBetween(LocalDate start, LocalDate end) {
        return events.stream()
                .filter(e -> e.getDateTime().toLocalDate()
                        .isAfter(start.minusDays(1))
                        && e.getDateTime().toLocalDate()
                        .isBefore(end.plusDays(1)))
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Delets given set of events from events set and adds it to eventsToDelete.
     *
     * @param eventsToDelete set of events to delete
     */
    public void deleteEvents(Set<DateEvent> eventsToDelete) {
        events.removeAll(eventsToDelete);
        eventsDeleted.removeAll(eventsToDelete);
    }

    /**
     * Delets given set of events from events set and adds it to eventsToDelete.
     *
     * @param eventsToDelete comma separated events to delete.
     */
    public void deleteEvents(DateEvent... eventsToDelete) {
        for (DateEvent eDel : eventsToDelete) {
            events.remove(eDel);
            eventsDeleted.add(eDel);
        }
    }

    /**
     * Adds given set of events to events set and adds it to eventsAdded.
     *
     * @param eventsToAdd comma separated events to add.
     */
    public void addEvents(DateEvent... eventsToAdd) {
        for (DateEvent e : eventsToAdd) {
            events.add(e);
            eventsAdded.add(e);
        }
    }

    /**
     * Adds given set of events to events set and adds it to eventsAdded.
     *
     * @param eventsToAdd set of events to add.
     */
    public void addEvents(Set<DateEvent> eventsToAdd) {
        events.addAll(eventsToAdd);
        eventsAdded.addAll(eventsToAdd);
    }

    /**
     * Initializes events with given set of events
     *
     * @param eventsToAdd adds all to set of events only.
     */
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
