package pk.calendar.models.storage;

/**
 * Created on 6/4/2017.
 * Interface for CRUD DataBase operations.
 */
public interface DaoDB<T> extends Dao<T> {
    void delete(T in) throws Exception;
}
