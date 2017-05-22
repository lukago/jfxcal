package pk.calendar.models;

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

    private EventManager() {
        this.events = new ArrayList<>();
    }

    public static EventManager getInstance() {
        return instance;
    }

    public List<DateEvent> getEventsByDate(LocalDate date) {
        return events.stream()
                .filter(e -> e.getDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public void addEvent(DateEvent event) {
        events.add(event);
    }

}
