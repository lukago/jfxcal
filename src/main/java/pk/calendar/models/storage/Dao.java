package pk.calendar.models.storage;

/**
 * Created on 5/25/2017.
 */
interface Dao<T> {
    T read() throws Exception;

    void write(T in) throws Exception;
}
