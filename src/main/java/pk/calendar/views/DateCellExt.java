package pk.calendar.views;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.MenuItem;
import pk.calendar.controllers.CallendarController;

import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
class DateCellExt extends DateCell {

    private ContextMenu contextMenu;
    private CallendarController cc;

    public DateCellExt(CallendarController cc) {
        super();
        this.cc = cc;
        initalize();
    }

    private void initalize() {
        setPrefHeight(200);
        getStylesheets().add(getClass().getResource("/css/DateCellExt.css").toExternalForm());
        initContextMenu();
    }

    private void initContextMenu() {
        MenuItem menu = new MenuItem("Events");
        menu.setOnAction(e -> cc.createEventMenu(this));
        contextMenu = new ContextMenu(menu);
        setContextMenu(contextMenu);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        cc.updateDateCell(this, item);
    }
}
