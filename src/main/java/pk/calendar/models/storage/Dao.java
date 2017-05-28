package pk.calendar.models.storage;

/**
 * Created on 5/25/2017.
 */
public interface Dao<T> {
    T read();

    void write(T in);
}
