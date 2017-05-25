package pk.calendar.controllers.storage;

import pk.calendar.models.DateEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 5/25/2017.
 */
public class DBDateEventDao implements Dao<List<DateEvent>>, AutoCloseable {

    private String url = "jdbc:sqlserver://" + "ACER"
            + "\\SQLEXPRESS;databaseName=pkcalendar;"
            + "integratedSecurity=true";
    private final String dbdriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private Connection connection;
    private Statement statement;
    private SQLParser parser;

    public DBDateEventDao(SQLParser parser) {
        try {
            Class.forName(dbdriver).newInstance();
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.parser = parser;
    }

    @Override
    public List<DateEvent> read() {
        String query = parser.createSelectAllQuery();
        List<DateEvent> events = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;

    }

    @Override
    public void write(List<DateEvent> in) {
        String query = parser.createInsertQuery(in);
        try {
            statement.executeUpdate(query);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        statement.close();
        connection.close();
    }

    @Override
    public void finalize() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

}
