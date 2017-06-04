package pk.calendar.models.storage;

import pk.calendar.models.data.DateEvent;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Created on 5/25/2017.
 */
class SQLDateEventListParser implements SQLParser<Set<DateEvent>> {

    private final String eventTable;

    public SQLDateEventListParser(String eventTable) {
        this.eventTable = eventTable;
    }

    @Override
    public String createInsertQuery(Set<DateEvent> in) {
        StringBuilder query = new StringBuilder();
        Timestamp tsDateTime;
        Timestamp tsNotify;

        for (DateEvent e : in) {
            query.append("\nINSERT INTO ").append(eventTable).append(" VALUES ");
            tsDateTime = Timestamp.valueOf(e.getDateTime());
            tsNotify = Timestamp.valueOf(e.getNotifyTime());
            query.append("('").append(tsDateTime)
                    .append("', '").append(tsNotify)
                    .append("', '").append(e.getDescription())
                    .append("', '").append(e.getPlace()).append("')");

        }
        return query.toString();
    }

    @Override
    public String createDeleteQuery(Set<DateEvent> in) {
        StringBuilder query = new StringBuilder();
        Timestamp tsDateTime;
        Timestamp tsNotify;

        for (DateEvent e : in) {
            query.append("\nDELETE FROM ").append(eventTable).append(" WHERE ");
            tsDateTime = Timestamp.valueOf(e.getDateTime());
            tsNotify = Timestamp.valueOf(e.getNotifyTime());
            query.append("date_time = '").append(tsDateTime)
                    .append("' AND notify_time = '")
                    .append(tsNotify).append("' AND description = '")
                    .append(e.getDescription()).append("' AND place = '")
                    .append(e.getPlace()).append("'");
        }
        return query.toString();
    }

    @Override
    public String createSelectAllQuery() {
        return "SELECT * FROM " + eventTable;
    }
}
