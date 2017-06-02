package pk.calendar.models.storage;

import pk.calendar.models.data.DateEvent;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Created on 5/25/2017.
 */
class SQLDateEventListParser implements SQLParser<Set<DateEvent>> {

    private String eventTable;

    public SQLDateEventListParser(String eventTable) {
        this.eventTable = eventTable;
    }

    @Override
    public String createInsertQuery(Set<DateEvent> in) {
        String query = "";
        Timestamp tsDateTime;
        Timestamp tsNotify;

        for (DateEvent e : in) {
            query += "\nINSERT INTO " + eventTable + " VALUES ";
            tsDateTime = Timestamp.valueOf(e.getDateTime());
            tsNotify = Timestamp.valueOf(e.getNotifyTime());
            query += "('" + tsDateTime + "', '" + tsNotify + "', '"
                    + e.getDescription() + "', '" + e.getPlace() + "')";

        }
        return query;
    }

    @Override
    public String createDeleteQuery(Set<DateEvent> in) {
        String query = "";
        Timestamp tsDateTime;
        Timestamp tsNotify;

        for (DateEvent e : in) {
            query += "\nDELETE FROM " + eventTable + " WHERE ";
            tsDateTime = Timestamp.valueOf(e.getDateTime());
            tsNotify = Timestamp.valueOf(e.getNotifyTime());
            query += "date_time = '" + tsDateTime + "' AND notify_time = '"
                    + tsNotify + "' AND description = '" + e.getDescription()
                    + "' AND place = '" + e.getPlace() + "'";
        }
        return query;
    }

    @Override
    public String createSelectAllQuery() {
        return "SELECT * FROM " + eventTable;
    }
}
