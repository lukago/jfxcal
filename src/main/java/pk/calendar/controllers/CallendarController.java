package pk.calendar.controllers;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pk.calendar.views.DatePickerExt;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created on 5/21/2017.
 */
public class CallendarController {

    @FXML
    BorderPane root;

    @FXML
    public void initialize() {
        DatePickerExt dp = new DatePickerExt(LocalDate.now(), this);
        DatePickerSkin dps = new DatePickerSkin(dp);
        Node popupContent = dps.getPopupContent();
        root.setCenter(popupContent);
    }

    public void createEventMenu(LocalDate date) {
        System.out.println("bb");
        try {
            BorderPane root = FXMLLoader.load(getClass().getResource("/fxml/EventView.fxml"));
            Scene scene = new Scene(root, 500, 500);
            Stage stage = new Stage();
            stage.setTitle("Events of " + date);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/assets/calendar-icon.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDateCell(DateCell dc, LocalDate item) {
        if (item.isEqual(LocalDate.now())) {
            dc.setId("date-cell-today");
            dc.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> dc.setId("date-cell-entered"));
            dc.addEventHandler(MouseEvent.MOUSE_EXITED, event -> dc.setId("date-cell-today"));
        } else {
            dc.setId(null);
        }
    }
}
