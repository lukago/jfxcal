package pk.calendar.models.storage;

/**
 * Created on 5/25/2017.
 * Interface for reading and writing unmanaged resources.
 */
public interface Dao<T> extends AutoCloseable {

    T read() throws Exception;

    void write(T in) throws Exception;
}
