package pk.calendar.models.storage;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;
import pk.calendar.models.data.DateEvent;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 6/2/2017.
 * Dao for ICS format I/O.
 */
public class ICSDateEventDao implements Dao<Set<DateEvent>>, AutoCloseable {

    private final String filepath;
    private final SimpleDateFormat dateFormatter;
    private FileInputStream fileInputStream;
    private FileWriter fileWriter;

    /**
     * Ctor.
     * @param filepath path to file to save/read.
     */
    public ICSDateEventDao(String filepath) {
        this.filepath = filepath;
        dateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    }

    /**
     * Reads DateEvents form ICS file.
     * @return Set of read DateEvents.
     * @throws IOException if file cannot be accessed
     * @throws ParseException if dateFormatter cant parse date
     * @throws ParserException if file content is not matching ICS
     */
    @Override
    public Set<DateEvent> read() throws IOException, ParseException, ParserException {
        Set<DateEvent> readSet = new HashSet<>();
        fileInputStream = new FileInputStream(filepath);
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(fileInputStream);
        LocalDateTime dateTime = null, notifyTime;
        String place = "", desc = "";

        for (CalendarComponent cc : calendar.getComponents("VEVENT")) {

            VEvent ev = (VEvent) cc;

            for (Property p : ev.getProperties()) {
                switch (p.getName()) {
                    case "DTSTART":
                        dateTime = LocalDateTime.ofInstant(
                                dateFormatter.parse(p.getValue()).toInstant(),
                                ZoneId.systemDefault());
                        break;
                    case "SUMMARY":
                        desc = p.getValue();
                        break;
                    case "LOCATION":
                        place = p.getValue();
                        break;
                }
            }

            notifyTime = dateTime;
            if (ev.getAlarms() != null) {
                if (ev.getAlarms().size() > 0) {
                    notifyTime = LocalDateTime.ofInstant(ev.getAlarms()
                                    .get(0).getTrigger().getDate().toInstant(),
                            ZoneId.systemDefault());
                }
            }
            readSet.add(new DateEvent(dateTime, notifyTime, desc, place));
        }

        return readSet;
    }

    /**
     * Writes DateEvents as ICS calendar.
     * @param in DateEvents to write.
     * @throws IOException if cannot access file
     * @throws ParseException if dateFormatter parse error
     */
    @Override
    public void write(Set<DateEvent> in) throws IOException, ParseException {

        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//pk//calendar//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        TimeZoneRegistry registry =
                TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone(ZoneId.systemDefault().toString());
        VTimeZone tz = timezone.getVTimeZone();

        fileWriter = new FileWriter(filepath);
        for (DateEvent event : in) {

            // parse LocalDateTime to Date
            Date dateEv = Date.from(event.
                    getDateTime().atZone(ZoneId.systemDefault()).toInstant());
            Date dateNotify = Date.from(event.
                    getNotifyTime().atZone(ZoneId.systemDefault()).toInstant());

            // initialize data for VEvent ctor
            String dateEvStr = dateFormatter.format(dateEv);
            String dateNotifyStr = dateFormatter.format(dateNotify);
            String eventName = event.getDescription();

            DateTime start = new DateTime(dateEvStr);
            DateTime end = new DateTime(dateEvStr);
            DateTime notify = new DateTime(dateNotifyStr);

            // create VEvent
            VEvent icsEvent = new VEvent(start, end, eventName);

            // add uid
            UidGenerator ug = new UidGenerator("uidGen");
            Uid uid = ug.generateUid();
            icsEvent.getProperties().add(uid);

            // add alarm
            VAlarm valarm = new VAlarm(notify);
            icsEvent.getAlarms().add(valarm);

            // add other properties
            icsEvent.getProperties().add(tz.getTimeZoneId());
            icsEvent.getProperties().add(new Location(event.getPlace()));

            // add to calendar
            calendar.getComponents().add(icsEvent);
        }

        // save to file
        fileWriter.write(calendar.toString());
    }

    @Override
    public void close() throws IOException {
        if (fileInputStream != null) fileInputStream.close();
        if (fileWriter != null) fileWriter.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
