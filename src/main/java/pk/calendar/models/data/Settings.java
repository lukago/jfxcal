package pk.calendar.models.data;

import pk.calendar.models.storage.SettingsDao;

import java.io.IOException;

/**
 * Created on 6/4/2017.
 * Settings Manager class.
 */
public class Settings {

    private static SettingsStruct data = load();

    /**
     * path to settings file
     */
    public static final String PATH = "prefs.bin";

    /**
     * Loads SettingsStruct form PATH
     * @return read SettingsStruct or default if cannot acces file.
     */
    public static SettingsStruct load() {
        try (SettingsDao dao = new SettingsDao(PATH)) {
            return dao.read();
        } catch (IOException | ClassNotFoundException e) {
            return new SettingsStruct("ACER\\SQLEXPRESS", "green", 600, 960);
        }
    }

    /**
     * Writes settings to file.
     * @throws IOException if cannot access file
     */
    public static void save() throws IOException {
        try (SettingsDao dao = new SettingsDao(PATH)) {
            dao.write(data);
        }
    }

    public static SettingsStruct getData() {
        return data;
    }
}
