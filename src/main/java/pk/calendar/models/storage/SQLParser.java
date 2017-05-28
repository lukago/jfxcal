package pk.calendar.models.storage;

/**
 * Created on 5/25/2017.
 */
public interface SQLParser<T> {

    String createInsertQuery(T in);

    String createDeleteQuery(T in);

    String createSelectAllQuery();

}
