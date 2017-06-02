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

import java.io.File;
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
 */
public class ICSDateEventDao implements Dao<Set<DateEvent>> {

    private String filepath;
    private SimpleDateFormat sdf;

    public ICSDateEventDao(String filepath) {
        this.filepath = filepath;
        sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    }

    @Override
    public Set<DateEvent> read() {
        Set<DateEvent> readSet = new HashSet<>();

        try (FileInputStream fin = new FileInputStream(filepath)) {

            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fin);

            LocalDateTime dateTime = null, notifyTime;
            String place = "", desc = "";

            for (CalendarComponent cc : calendar.getComponents("VEVENT")) {
                VEvent ev = (VEvent) cc;

                for (Property p : ev.getProperties()) {
                    if (p.getName().equals("DTSTART")) {
                        dateTime = LocalDateTime.ofInstant(
                                sdf.parse(p.getValue()).toInstant(),
                                ZoneId.systemDefault());
                    }

                    if (p.getName().equals("SUMMARY")) {
                        desc = p.getValue();
                    }

                    if (p.getName().equals("LOCATION")) {
                        place = p.getValue();
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return readSet;
    }

    @Override
    public void write(Set<DateEvent> in) {

        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//pk//calendar//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        TimeZoneRegistry registry =
                TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone(ZoneId.systemDefault().toString());
        VTimeZone tz = timezone.getVTimeZone();

        try (FileWriter fw = new FileWriter(new File(filepath))) {
            for (DateEvent event : in) {

                // parse LocalDateTime to Date
                Date dateEv = Date.from(event.
                        getDateTime().atZone(ZoneId.systemDefault()).toInstant());
                Date dateNotify = Date.from(event.
                        getNotifyTime().atZone(ZoneId.systemDefault()).toInstant());

                // initialize data for VEvent ctor
                String dateEvStr = sdf.format(dateEv);
                String dateNotifyStr = sdf.format(dateNotify);
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
            fw.write(calendar.toString());

        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
