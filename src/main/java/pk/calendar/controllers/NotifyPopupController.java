package pk.calendar.controllers;

import javafx.application.Platform;
import javafx.util.Duration;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.controlsfx.control.Notifications;
import pk.calendar.models.data.DateEvent;
import pk.calendar.models.data.EventManager;
import pk.calendar.utils.NotifyPopup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created on 6/3/2017.
 */
public class NotifyPopupController {

    private final NotifyPopup notifyPopup;
    private final Set<PopupTask> popupTasks;
    private final EventManager eventManager;

    public NotifyPopupController() {
        popupTasks = new HashSet<>();
        notifyPopup = new NotifyPopup();
        eventManager = EventManager.getInstance();
    }

    public void initialize() {
        for (DateEvent dateEvent : eventManager.getEvents()) {
            PopupTask task = new PopupTask(dateEvent);
            notifyPopup.schedule(task, task.date);
            popupTasks.add(task);
        }
    }

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

        toRemove.forEach(o -> popupTasks.remove(o));
    }

    public void handleEventsAdded(LocalDate item) {
        for (DateEvent dateEvent : eventManager.getEventsByDate(item)) {
            PopupTask task = new PopupTask(dateEvent);
            if (popupTasks.add(task)) {
                notifyPopup.schedule(task, task.date);
            }
        }
    }

    public void cancelAll() {
        notifyPopup.cancel();
    }

    private class PopupTask extends TimerTask {
        final DateEvent dateEvent;
        Date date;

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

        @Override
        public void run() {
            Platform.runLater(() -> Notifications.create()
                    .title(dateEvent.getDateTime().toString())
                    .text(dateEvent.getDescription())
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
