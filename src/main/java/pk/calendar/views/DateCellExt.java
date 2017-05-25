package pk.calendar.views;

import javafx.scene.control.DateCell;
import pk.calendar.controllers.CallendarController;

import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
class DateCellExt extends DateCell {

    private final CallendarController cc;

    public DateCellExt(CallendarController cc) {
        super();
        this.cc = cc;
        initalize();
    }

    private void initalize() {
        setPrefHeight(200);
        getStylesheets().add(getClass().getResource("/css/DateCellExt.css").toExternalForm());
        setOnMouseClicked(e -> cc.handleCellEvent(this, e));
        setOnKeyPressed(e -> cc.handleCellEvent(this, e));
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        cc.updateDateCell(this, item);
    }
}
