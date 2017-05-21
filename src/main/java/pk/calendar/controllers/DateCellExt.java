package pk.calendar.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
class DateCellExt extends DateCell {

    private ContextMenu contextMenu;

    public DateCellExt() {
        super();
        initalize();
    }

    private void initalize() {
        setPrefHeight(200);
        getStylesheets().add(getClass().getResource("/css/DateCellExt.css").toExternalForm());
        addContextMenuEvents();
        setContextMenu(contextMenu);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (item.isEqual(LocalDate.now())) {
            setId("date-cell-today");
            addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setId("date-cell-entered"));
            addEventHandler(MouseEvent.MOUSE_EXITED, event -> setId("date-cell-today"));
        } else {
            setId(null);
        }
    }

    private void addContextMenuEvents() {
        MenuItem menu = new MenuItem("Events");
        menu.setOnAction(e -> handeEventMenu());
        contextMenu = new ContextMenu(menu);
    }

    private void handeEventMenu() {
        System.out.println("bb");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EventMenu.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Events of " + getItem());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
