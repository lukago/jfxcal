package pk.calendar.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created on 5/21/2017.
 */
public class DateEvent implements Comparable<DateEvent> {

    private LocalDateTime dateTime;
    private LocalDate date;
    private String description;
    private String place;

    public DateEvent(LocalDate date, int hh, int mm, String place, String description) {
        String hour = (hh < 10 ? "0" : "") + hh;
        String minute = (mm < 10 ? "0" : "") + mm;
        LocalTime time = LocalTime.parse(hour + ":" + minute + ":00");
        setDateTime(LocalDateTime.of(date, time));
        setDate(date);
        setDescription(description);
        setPlace(place);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int compareTo(DateEvent o) {
        return dateTime.compareTo(o.getDateTime());
    }
}
