package pk.calendar.models.data;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 6/3/2017.
 * Class for schedulting Timer Tasks
 */
public class TimerScheduler implements Closeable {

    private final Timer timer;

    /**
     * Creates new Timer thread.
     */
    public TimerScheduler() {
        this.timer = new Timer();
    }

    /**
     * Schedules Timer task.
     * @param task TimerTask to run
     * @param date when to run TimerTask
     */
    public void schedule(TimerTask task, Date date) {
        LocalDateTime ldt = LocalDateTime.now();
        Date now = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        if (date.after(now)) {
            timer.schedule(task, date);
        }
    }

    /**
     * Cancels timer to stop this thread. Should be before application
     * exit.
     */
    @Override
    public void close() {
        timer.cancel();
    }

    @Override
    protected void finalize() throws Throwable {
        timer.cancel();
    }
}
