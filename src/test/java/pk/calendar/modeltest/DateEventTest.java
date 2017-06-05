package pk.calendar.modeltest;

import org.junit.Assert;
import org.junit.Test;
import pk.calendar.models.data.DateEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created on 6/5/2017.
 */
public class DateEventTest {

    @Test
    public void DateEventConstructorTest() {
        DateEvent dateEvent = new DateEvent(
                LocalDate.of(2000, 1, 1), 12, 12, 0, "a", "b");

        Assert.assertEquals(dateEvent.getDateTime(),
                LocalDateTime.of(2000, 1, 1, 12, 12, 0));
    }
}
