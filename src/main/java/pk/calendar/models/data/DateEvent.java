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
 */


@XmlAccessorType(XmlAccessType.FIELD)
public class DateEvent implements Comparable<DateEvent>, Serializable {

    private String description;
    private String place;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dateTime;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime notifyTime;

    public DateEvent(LocalDate date, int hh, int mm, int secMinus, String place,
                     String description) {
        String hour = (hh < 10 ? "0" : "") + hh;
        String minute = (mm < 10 ? "0" : "") + mm;
        LocalTime time = LocalTime.parse(hour + ":" + minute + ":00");
        setDateTime(LocalDateTime.of(date, time));
        setNotifyTime(secMinus);
        setDescription(description);
        setPlace(place);
    }

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
