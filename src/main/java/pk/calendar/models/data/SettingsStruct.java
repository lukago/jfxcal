package pk.calendar.models.data;

import java.io.Serializable;

/**
 * Created on 6/4/2017.
 * Settings structure. It contains fiels only.
 */
public class SettingsStruct implements Serializable {

    public String dataBase;
    public String cellColor;
    public int winHeigth;
    public int winWidth;

    /**
     * All-field ctor.
     * @param dataBaseName name of database used for storing data
     *                     e.x MachineName\SQLExperess
     * @param cellColor color of calendar cells when event is present
     * @param winHeigth callendar stage height
     * @param winWidth callendar stage width
     */
    public SettingsStruct(String dataBaseName, String cellColor,
                          int winHeigth, int winWidth) {
        this.dataBase = dataBaseName;
        this.cellColor = cellColor;
        this.winHeigth = winHeigth;
        this.winWidth = winWidth;
    }

}
