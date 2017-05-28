package pk.calendar.models;


import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created on 5/22/2017.
 */
public class EventsChangedEvent extends Event {

    public static final EventType<EventsChangedEvent> ADDED =
            new EventType<>(Event.ANY, "ADDED");
    public static final EventType<EventsChangedEvent> DELETED =
            new EventType<>(Event.ANY, "DELETED");

    public EventsChangedEvent(EventType<EventsChangedEvent> eventType) {
        super(null, null, eventType);
    }

}
