package pk.calendar.models.storage;

import pk.calendar.models.data.DateEvent;
import pk.calendar.models.adapters.SetWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;

/**
 * Created on 5/30/2017.
 */
public class XMLDateEventDao implements Dao<Set<DateEvent>> {

    private String filename;

    public XMLDateEventDao(String filename){
        this.filename = filename;
    }

    @Override
    public Set<DateEvent> read() {
        File file = new File(filename);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SetWrapper.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SetWrapper wrapper = (SetWrapper) jaxbUnmarshaller.unmarshal(file);
            return wrapper.getSetCol();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(Set<DateEvent> in) {
        try {
            SetWrapper s = new SetWrapper();
            s.setSetCol(in);

            JAXBContext jaxbContext = JAXBContext.newInstance(SetWrapper.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(s, new BufferedWriter(new FileWriter(filename)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
