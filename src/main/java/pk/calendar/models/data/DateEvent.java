package pk.calendar.models.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pk.calendar.models.adapters.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created on 5/21/2017.
 * Callendar event at certain date data model.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DateEvent implements Comparable<DateEvent>, Serializable {

    private String description;
    private String place;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dateTime;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime notifyTime;

    /**
     * Ctor.
     * @param date Date of event
     * @param hour hour time of event
     * @param minute minute time of event
     * @param secMinus how many seconds before set notify time
     * @param place place description
     * @param description event description
     */
    public DateEvent(LocalDate date, int hour, int minute, int secMinus,
                     String place, String description) {
        String hh = (hour < 10 ? "0" : "") + hour;
        String mm = (minute < 10 ? "0" : "") + minute;
        LocalTime time = LocalTime.parse(hh + ":" + mm + ":00");
        setDateTime(LocalDateTime.of(date, time));
        setNotifyTime(secMinus);
        setDescription(description);
        setPlace(place);
    }

    /**
     * Standard all field ctor.
     * @param dateTime when event
     * @param notifyTime when notify
     * @param description description of event
     * @param place place description
     */
    public DateEvent(LocalDateTime dateTime, LocalDateTime notifyTime,
                     String description, String place) {
        this.dateTime = dateTime;
        this.notifyTime = notifyTime;
        this.description = description;
        this.place = place;
    }

    /**
     * Ctor for XML serialization.
     */
    @Deprecated
    public DateEvent() {
        this(LocalDate.now(), 0, 0, 0, "", "");
    }

    public LocalDateTime getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(int seconds) {
        notifyTime = dateTime.minusSeconds(seconds);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DateEvent dateEvent = (DateEvent) o;

        return new EqualsBuilder()
                .append(dateTime, dateEvent.dateTime)
                .append(notifyTime, dateEvent.notifyTime)
                .append(description, dateEvent.description)
                .append(place, dateEvent.place)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dateTime)
                .append(notifyTime)
                .append(description)
                .append(place)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("description", description)
                .append("place", place)
                .append("dateTime", dateTime)
                .append("notifyTime", notifyTime)
                .toString();
    }
}
