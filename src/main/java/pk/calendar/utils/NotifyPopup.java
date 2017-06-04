package pk.calendar.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 6/3/2017.
 */
public class NotifyPopup {

    private final Timer timer;

    public NotifyPopup() {
        this.timer = new Timer();
    }

    public void schedule(TimerTask task, Date date) {
        LocalDateTime ldt = LocalDateTime.now();
        Date now = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        if (date.after(now)) {
            timer.schedule(task, date);
        }
    }

    public void cancel() {
        timer.cancel();
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    protected void finalize() throws Throwable {
        timer.cancel();
    }
}
