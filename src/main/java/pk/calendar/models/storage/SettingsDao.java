package pk.calendar.models.storage;

import pk.calendar.models.data.SettingsStruct;

import java.io.*;

/**
 * Created on 6/4/2017.
 * Dao for serializing SettingsStruct objects.
 */
public class SettingsDao implements Dao<SettingsStruct> {

    private FileOutputStream fileOut;
    private ObjectOutputStream objectOut;
    private FileInputStream fileIn;
    private ObjectInputStream objectIn;
    private String filepath;

    /**
     * Ctor.
     * @param filepath path to save/write serialized SettingsStruct
     */
    public SettingsDao(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Serializes SettingsStruct to SettingsDao filepath.
     * @return deserialized SettingsStruct
     * @throws IOException if cannot access file
     * @throws ClassNotFoundException if filecontent is bad
     */
    @Override
    public SettingsStruct read() throws IOException, ClassNotFoundException {
        fileIn = new FileInputStream(filepath);
        objectIn = new ObjectInputStream(fileIn);
        return  (SettingsStruct) objectIn.readObject();
    }

    /**
     * Serialzes given SettingsStruct to SettingsDao filepath.
     * @param in SettingStruct to serialize
     * @throws IOException if cannot acces file
     */
    @Override
    public void write(SettingsStruct in) throws IOException {
        fileOut = new FileOutputStream(filepath);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(in);
    }

    @Override
    public void close() throws IOException {
        if (fileIn != null) fileIn.close();
        if (fileOut != null) fileOut.close();
        if (objectIn != null) objectIn.close();
        if (objectOut != null) objectOut.close();
    }
}
