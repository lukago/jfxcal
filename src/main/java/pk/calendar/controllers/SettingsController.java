package pk.calendar.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pk.calendar.models.data.EventsChangedEvent;
import pk.calendar.models.data.Settings;

import java.io.IOException;

/**
 * Created on 6/4/2017.
 * Controller for SettingsView.fxml.
 */
public class SettingsController {

    private CallendarController cc;

    @FXML
    private Spinner<String> colorSpinner;
    @FXML
    private TextField dbTextField;
    @FXML
    private Button exitButton;

    /**
     * Ctor. This class has no zero argument ctor so it has to be set by
     * setControllerFactory with fxml loader when creating this window.
     *
     * @param cc this class uses and notifies main controller
     */
    public SettingsController(CallendarController cc) {
        this.cc = cc;
    }

    /**
     * Initializes spinner factories and properties.
     */
    @FXML
    public void initialize() {
        dbTextField.setText(Settings.getData().dataBase);

        ObservableList<String> notify =
                FXCollections.observableArrayList("red", "green");
        colorSpinner.setValueFactory(
                new SpinnerValueFactory.ListSpinnerValueFactory<>(notify));

        colorSpinner.getValueFactory().setValue(Settings.getData().cellColor);
    }

    /**
     * Hander for cancel button. It closes the stage.
     */
    @FXML
    private void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handler for apply changes button. It updates settings data
     * and saves it to file.
     */
    @FXML
    private void applyChanges() {
        Settings.getData().dataBase = dbTextField.getText();
        Settings.getData().cellColor = colorSpinner.getValue();
        try {
            Settings.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cc.getDateCells().forEach(
                o -> o.fireEvent(new EventsChangedEvent(EventsChangedEvent.ADDED)));
    }

}
