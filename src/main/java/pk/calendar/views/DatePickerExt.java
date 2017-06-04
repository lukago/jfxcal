package pk.calendar.views;

import javafx.scene.control.DatePicker;
import pk.calendar.controllers.CallendarController;

import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
public class DatePickerExt extends DatePicker {

    /**
     * Ctor. Sets custom DayCellFactory.
     * @param date Date for DatePicker super ctor
     * @param cc controller used by cell factory
     */
    public DatePickerExt(LocalDate date, CallendarController cc) {
        super(date);
        setDayCellFactory(param -> new DateCellExt(cc));
    }

}
