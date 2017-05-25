package pk.calendar.controllers.storage;

import pk.calendar.models.DateEvent;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created on 5/25/2017.
 */
class SQLDateEventListParser implements SQLParser<List<DateEvent>> {

    private String eventTable;

    public SQLDateEventListParser(String eventTable) {
        this.eventTable = eventTable;
    }

    @Override
    public String createInsertQuery(List<DateEvent> in) {
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
    public String createSelectAllQuery() {
        return "SELECT * FROM " + eventTable;
    }
}
