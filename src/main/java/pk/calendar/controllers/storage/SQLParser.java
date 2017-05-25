package pk.calendar.controllers.storage;

/**
 * Created on 5/25/2017.
 */
public interface SQLParser<T> {

    String createInsertQuery(T in);
    String createSelectAllQuery();

}
