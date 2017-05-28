package pk.calendar.models.storage;

/**
 * Created on 5/25/2017.
 */
public class DateEventDaoFactory {

    public static DBDateEventDao getDBDao() {
        return new DBDateEventDao(new SQLDateEventListParser("date_events"));
    }
}
