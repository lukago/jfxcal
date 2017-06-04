package pk.calendar.models.storage;

import pk.calendar.models.adapters.SetWrapper;
import pk.calendar.models.data.DateEvent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Created on 5/30/2017.
 */
public class XMLDateEventDao implements Dao<Set<DateEvent>>, AutoCloseable {

    private final String filename;
    private BufferedWriter bufferedWriter;

    public XMLDateEventDao(String filename) {
        this.filename = filename;
    }

    @Override
    public Set<DateEvent> read() throws JAXBException {
        File file = new File(filename);

        JAXBContext jaxbContext = JAXBContext.newInstance(SetWrapper.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SetWrapper wrapper = (SetWrapper) jaxbUnmarshaller.unmarshal(file);
        return wrapper.getSetCol();
    }

    @Override
    public void write(Set<DateEvent> in) throws JAXBException, IOException {
        SetWrapper wrapper = new SetWrapper();
        bufferedWriter = new BufferedWriter(new FileWriter(filename));
        wrapper.setSetCol(in);

        JAXBContext jaxbContext = JAXBContext.newInstance(SetWrapper.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(wrapper, bufferedWriter);
    }

    @Override
    public void close() throws IOException {
        if (bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
