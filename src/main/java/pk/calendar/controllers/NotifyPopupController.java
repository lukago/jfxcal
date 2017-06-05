package pk.calendar.controllers;

import javafx.application.Platform;
import javafx.util.Duration;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.controlsfx.control.Notifications;
import pk.calendar.models.data.DateEvent;
import pk.calendar.models.data.EventManager;
import pk.calendar.models.data.TimerScheduler;

import java.io.Closeable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created on 6/3/2017.
 * Controller for TimerScheduler. It uses this model to
 * create popup view at notificationTime taken from dateEvents.
 */
public class NotifyPopupController implements Closeable {

    private final EventManager eventManager;

    /**
     * TimerScheduler model. It creates timer thread and shoud call
     * cancelAll after it is no longer used to stop this thread.
     */
    private final TimerScheduler timerScheduler;

    /**
     * Scheduled TimerTasks
     */
    private final Set<PopupTask> popupTasks;

    /**
     * Ctor. Initializes fields.
     */
    public NotifyPopupController() {
        popupTasks = new HashSet<>();
        timerScheduler = new TimerScheduler();
        eventManager = EventManager.getInstance();
    }

    /**
     * Schedules all PopupTask tasks from DateEvents.
     */
    public void initialize() {
        for (DateEvent dateEvent : eventManager.getEvents()) {
            PopupTask task = new PopupTask(dateEvent);
            timerScheduler.schedule(task, task.date);
            popupTasks.add(task);
        }
    }

    /**
     * Safe delete PopupTasks of deleted DateEvents.
     */
    public void handleEventsDeleted() {
        Set<PopupTask> toRemove = new HashSet<>();

        for (DateEvent dateEvent : eventManager.getEventsDeleted()) {
            for (PopupTask popupTask : popupTasks) {
                if (dateEvent.equals(popupTask.dateEvent)) {
                    popupTask.cancel();
                    toRemove.add(popupTask);
                }
            }
        }

        toRemove.forEach(popupTasks::remove);
    }

    /**
     * Safe add PopupTasks of added DateEvents in certain Date.
     */
    public void handleEventsAdded(LocalDate item) {
        for (DateEvent dateEvent : eventManager.getEventsByDate(item)) {
            PopupTask task = new PopupTask(dateEvent);
            if (popupTasks.add(task)) {
                timerScheduler.schedule(task, task.date);
            }
        }
    }

    /**
     * Cancels Timer used by TimerScheduler so it also cancels all PopupTasks.
     * This should be called before exit of fxml application
     * beacuse Timer is running on separate thread.
     */
    @Override
    public void close() {
        timerScheduler.close();
    }

    /**
     * Inner class. PopupTask wtih DateEvent proprety.
     * On run cerates controlsFX popup.
     */
    private class PopupTask extends TimerTask {
        /**
         * DateEvent to notify about.
         */
        final DateEvent dateEvent;
        /**
         * Date type used by Timer.
         */
        Date date;

        /**
         * Initializes fileds. Perses LocalDateTime from Date Event to Date.
         * @param dateEvent DateEvent to notify about.
         */
        public PopupTask(DateEvent dateEvent) {
            this.dateEvent = dateEvent;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .parse(dateEvent.getNotifyTime().toString());
            } catch (ParseException e) {
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
                            .parse(dateEvent.getNotifyTime().toString());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }

        /**
         * Creates controlsFX popup on JavaFX UI thread.
         */
        @Override
        public void run() {
            String text = dateEvent.getDescription()
                    + " - " + dateEvent.getPlace();
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            Platform.runLater(() -> Notifications.create()
                    .title(dateEvent.getDateTime().format(formatter))
                    .text(text)
                    .hideAfter(Duration.seconds(10))
                    .showInformation());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            PopupTask popupTask = (PopupTask) o;

            return new EqualsBuilder()
                    .append(dateEvent, popupTask.dateEvent)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(dateEvent)
                    .toHashCode();
        }
    }
}
