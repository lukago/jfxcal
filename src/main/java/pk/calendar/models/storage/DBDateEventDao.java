package pk.calendar.models.storage;

import pk.calendar.models.data.DateEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 5/25/2017.
 */
public class DBDateEventDao implements Dao<Set<DateEvent>>, AutoCloseable {

    private final String dbdriver =
            "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private final String url = "jdbc:sqlserver://" + "ACER"
            + "\\SQLEXPRESS;databaseName=pkcalendar;"
            + "integratedSecurity=true";

    private Connection connection;
    private Statement statement;
    private SQLParser<Set<DateEvent>> parser;

    public DBDateEventDao(SQLParser<Set<DateEvent>> parser) throws Exception {
        Class.forName(dbdriver).newInstance();
        connection = DriverManager.getConnection(url);
        statement = connection.createStatement();
        this.parser = parser;
    }

    @Override
    public Set<DateEvent> read() throws SQLException {
        String query = parser.createSelectAllQuery();
        Set<DateEvent> events = new HashSet<>();
        ResultSet result = statement.executeQuery(query);

        LocalDateTime ldt, notify;
        String desc, place;

        while (result.next()) {
            ldt = result.getTimestamp("date_time").toLocalDateTime();
            notify = result.getTimestamp("notify_time").toLocalDateTime();
            desc = result.getString("description");
            place = result.getString("place");
            events.add(new DateEvent(ldt, notify, desc, place));
        }
        return events;
    }


    @Override
    public void write(Set<DateEvent> in) throws SQLException {
        String query = parser.createInsertQuery(in);
        statement.executeUpdate(query);
    }

    public void delete(Set<DateEvent> in) throws SQLException {
        String query = parser.createDeleteQuery(in);
        statement.executeUpdate(query);
    }

    @Override
    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

    @Override
    public void finalize() throws Throwable {
        close();
    }

}
