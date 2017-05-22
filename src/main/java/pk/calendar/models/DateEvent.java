package pk.calendar.models;

import java.time.LocalDate;

/**
 * Created on 5/21/2017.
 */
public class DateEvent {

    private LocalDate date;
    private String description;

    public DateEvent(LocalDate date, String description) {
        setDate(date);
        setDescription(description);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
