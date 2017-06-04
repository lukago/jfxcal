package pk.calendar.models.data;


import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created on 5/22/2017.
 * Custon Event. Fired when set of events is changed.
 */
public class EventsChangedEvent extends Event {

    public static final EventType<EventsChangedEvent> ADDED =
            new EventType<>(Event.ANY, "ADDED");
    public static final EventType<EventsChangedEvent> DELETED =
            new EventType<>(Event.ANY, "DELETED");

    /**
     * Ctor.
     * @param eventType see public static fields of this class.
     */
    public EventsChangedEvent(EventType<EventsChangedEvent> eventType) {
        super(null, null, eventType);
    }

}
