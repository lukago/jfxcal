package pk.calendar.models.storage;

import pk.calendar.models.data.DateEvent;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Created on 5/25/2017.
 * Parser for creating SQL queries.
 */
class SQLParser {

    final String table;
    final String[] columns;

    /**
     * Ctor.
     * @param table table name with DateEvents
     * @param columns table columns names
     */
    public SQLParser(String table, String[] columns) {
        this.table = table;
        this.columns = columns;
    }

    /**
     * Creates SQL insert query.
     * @param in DateEvents to include in insert query
     * @return String SQL insert query
     */
    public String createInsertQuery(Set<DateEvent> in) {
        StringBuilder query = new StringBuilder();
        Timestamp tsDateTime;
        Timestamp tsNotify;

        for (DateEvent e : in) {
            query.append("\nINSERT INTO ").append(table).append(" VALUES ");
            tsDateTime = Timestamp.valueOf(e.getDateTime());
            tsNotify = Timestamp.valueOf(e.getNotifyTime());
            query.append("('").append(tsDateTime)
                    .append("', '").append(tsNotify)
                    .append("', '").append(e.getDescription())
                    .append("', '").append(e.getPlace()).append("')");

        }
        return query.toString();
    }

    /**
     * Creates SQL delete query.
     * @param in DateEvents to include in delete query
     * @return String SQL delete query
     */
    public String createDeleteQuery(Set<DateEvent> in) {
        StringBuilder query = new StringBuilder();
        Timestamp tsDateTime;
        Timestamp tsNotify;

        for (DateEvent e : in) {
            query.append("\nDELETE FROM ").append(table).append(" WHERE ");
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

    /**
     * Creates select all DateEvents query.
     * @return sql select all query
     */
    public String createSelectAllQuery() {
        return "SELECT * FROM " + table;
    }
}
