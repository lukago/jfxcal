package pk.calendar.models.storage;

import java.sql.SQLException;

/**
 * Created on 5/25/2017.
 * Factory to instantiate Dao implementations.
 */
public class DateEventDaoFactory {

    /**
     * DBDateEventDao factory.
     * @return DBDateEventDao object
     * @throws SQLException if cannot connect to db
     */
    public static DBDateEventDao getDBDao() throws SQLException {
        String table = "date_events";
        String[] columns =
                new String[] {"date_time", "notify_time", "description", "place"};
        return new DBDateEventDao(new SQLParser(table, columns));
    }

    /**
     * XMLDateEventDao factory.
     * @return XMLDateEventDao object
     */
    public static XMLDateEventDao getXMLDao(String path) {
        return new XMLDateEventDao(path);
    }

    /**
     * ICSDateEventDao factory.
     * @return ICSDateEventDao object
     */
    public static ICSDateEventDao getICSDao(String path) {
        return new ICSDateEventDao(path);
    }
}
