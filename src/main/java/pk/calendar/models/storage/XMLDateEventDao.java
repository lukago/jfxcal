package pk.calendar.models.storage;

import pk.calendar.models.DateEvent;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.List;

/**
 * Created on 5/30/2017.
 */
public class XMLDateEventDao implements Dao<List<DateEvent>>, AutoCloseable {

    private XMLEncoder encoder;
    private XMLDecoder decoder;

    public XMLDateEventDao(String out, String in) throws IOException {
        File f = new File(out);
        System.out.println(f.toPath());
        f.createNewFile();

        encoder = new XMLEncoder(new BufferedOutputStream(
                new FileOutputStream(f, false)));
        decoder =  new XMLDecoder(new FileInputStream(in));
    }

    @Override
    public List<DateEvent> read() {
        List<DateEvent> list = (List<DateEvent>) decoder.readObject();
        return list;
    }

    @Override
    public void write(List<DateEvent> in) {
        encoder.writeObject(in);
    }

    @Override
    public void close() throws IOException {
        encoder.close();
        decoder.close();
    }

    @Override
    public void finalize() throws IOException {
        encoder.close();
        decoder.close();
    }
}
