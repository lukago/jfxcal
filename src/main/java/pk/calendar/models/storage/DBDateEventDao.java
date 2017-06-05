package pk.calendar.models.storage;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import pk.calendar.models.data.DateEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 5/25/2017.
 * Dao for SQL Server database.
 */
public class DBDateEventDao implements Dao<Set<DateEvent>>, AutoCloseable {

    private Connection connection;
    private Statement statement;
    private SQLParser parser;
    private ResultSet result;

    /**
     * Ctor.
     * @param parser for creating queries
     * @throws SQLException if cannot connect to db
     */
    public DBDateEventDao(SQLParser parser, String url) throws SQLException {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setURL(url);
        connection = ds.getConnection();
        statement = connection.createStatement();
        this.parser = parser;
    }

    /**
     * Reads all elements from db table witth DateEvents.
     * @return all elements read from table
     * @throws SQLException if error during query execution
     */
    @Override
    public Set<DateEvent> read() throws SQLException {
        String query = parser.createSelectAllQuery();
        Set<DateEvent> events = new HashSet<>();
        result = statement.executeQuery(query);

        LocalDateTime ldt, notify;
        String desc, place;

        while (result.next()) {
            ldt = result.getTimestamp(parser.columns[0]).toLocalDateTime();
            notify = result.getTimestamp(parser.columns[1]).toLocalDateTime();
            desc = result.getString(parser.columns[2]);
            place = result.getString(parser.columns[3]);
            events.add(new DateEvent(ldt, notify, desc, place));
        }
        return events;
    }

    /**
     * Inserst set to database DateEvents table.
     * @param in set to insert
     * @throws SQLException if error during query execuction
     */
    @Override
    public void write(Set<DateEvent> in) throws SQLException {
        String query = parser.createInsertQuery(in);
        statement.executeUpdate(query);
    }

    /**
     * Deletes set from database DateEvents table.
     * @param in set of matching elements to delete
     * @throws SQLException if error during query execuction
     */
    public void delete(Set<DateEvent> in) throws SQLException {
        String query = parser.createDeleteQuery(in);
        statement.executeUpdate(query);
    }

    @Override
    public void close() throws SQLException {
        statement.close();
        connection.close();
        if (result != null) result.close();
    }

    @Override
    public void finalize() throws Throwable {
        close();
    }

}
