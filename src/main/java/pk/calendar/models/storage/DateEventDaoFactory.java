package pk.calendar.models.storage;

/**
 * Created on 5/25/2017.
 */
public class DateEventDaoFactory {

    public static DBDateEventDao getDBDao() throws Exception {
        return new DBDateEventDao(new SQLDateEventListParser("date_events"));
    }

    public static XMLDateEventDao getXMLDao(String path) {
        return new XMLDateEventDao(path);
    }

    public static ICSDateEventDao getICSDao(String path) {
        return new ICSDateEventDao(path);
    }
}
