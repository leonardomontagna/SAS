package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class NewSheetDialog {

    Stage myStage;

    @FXML
    ComboBox<EventInfo> eventCombo;

    @FXML
    ComboBox<ServiceInfo> serviceCombo;

    private EventInfo event;
    private ServiceInfo service;

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }

    public void initialize() {

        eventCombo.setItems(CatERing.getInstance().getEventManager().getEventInfo());
        eventCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                EventInfo event = eventCombo.getSelectionModel().getSelectedItem();
                ObservableList<ServiceInfo> services = event.getServices();
                serviceCombo.setItems(services);
            }
        });
    }

    @FXML
    public void creaButtonPressed(){
        event = eventCombo.getSelectionModel().getSelectedItem();
        service = serviceCombo.getSelectionModel().getSelectedItem();
        service.setMenu(CatERing.getInstance().getMenuManager().getMenuById(service.getId_menu_approved()));
        myStage.close();
    }


    public EventInfo getEvent() {
        return event;
    }

    public ServiceInfo getService() {
        return service;
    }
}
