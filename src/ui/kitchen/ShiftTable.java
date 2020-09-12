package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.shift.Shift;
import businesslogic.shift.ShiftException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ShiftTable {

    Stage myStage;

    @FXML
    ListView<Shift> table;


    //ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

    public void initialize(){
        table.setItems(CatERing.getInstance().getKitchenManager().getShiftTable());
        table.setCellFactory(param -> new ListCell<Shift>() {
            @Override
            protected void updateItem(Shift item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item.stampaTab());
                }
            }
        });
    }

    public void setOwnStage(Stage stage) {
        this.myStage = stage;
    }

    @FXML
    public void saturoButtonPressed() throws ShiftException {
        Shift shift = table.getSelectionModel().getSelectedItem();
        CatERing.getInstance().getKitchenManager().setKitchenFull(shift);
        table.refresh();
    }


}
