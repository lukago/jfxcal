package pk.calendar.views;

import javafx.scene.control.DatePicker;
import pk.calendar.controllers.CallendarController;

import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
public class DatePickerExt extends DatePicker {

    public DatePickerExt(LocalDate date, CallendarController cc) {
        super(date);
        setDayCellFactory(param -> new DateCellExt(cc));
    }

}
