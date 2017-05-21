package pk.calendar.controllers;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
public class DatePickerExt extends DatePicker {

    public DatePickerExt(LocalDate date) {
        super(date);
        setDayCellFactory(param -> new DateCellExt());
    }

}
