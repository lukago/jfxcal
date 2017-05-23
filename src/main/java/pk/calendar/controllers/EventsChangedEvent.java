package pk.calendar.controllers;


import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created on 5/22/2017.
 */
public class EventsChangedEvent extends Event {

    public static final EventType<EventsChangedEvent> ADDED = new EventType<>(Event.ANY, "ADDED");

    public EventsChangedEvent(EventType<EventsChangedEvent> eventType) {
        super(null, null, eventType);
    }

}
