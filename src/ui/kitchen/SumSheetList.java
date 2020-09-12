package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.kitchen.KitchenException;
import businesslogic.kitchen.SummarySheet;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SumSheetList {

    @FXML
    ListView<SummarySheet> sheetList;

    private KitchenManagement kitchenManagementController;

    ObservableList<SummarySheet> sheetListItems;

    public void initialize(){
        if(sheetListItems == null){
            sheetListItems = CatERing.getInstance().getKitchenManager().getAllSheets();
            sheetList.setItems(sheetListItems);
        } else {
            sheetList.refresh();
        }
    }

    @FXML
    public void fineButtonPressed() {
        kitchenManagementController.endKitchenManagement();
    }

    @FXML
    public void nuovoButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new-sheet-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            NewSheetDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nuovo foglio riepilogativo");
            stage.setScene(new Scene(pane));

            stage.showAndWait();

            EventInfo event = controller.getEvent();
            ServiceInfo service = controller.getService();

            if(event != null && service != null){
                SummarySheet sheet = CatERing.getInstance().getKitchenManager().createSummarySheet(event,service);
                sheetListItems.add(sheet);
                kitchenManagementController.initialize();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UseCaseLogicException e) {
            e.printStackTrace();
        } catch (KitchenException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void apriButtonPressed(){

        try {
            SummarySheet sheet = sheetList.getSelectionModel().getSelectedItem();
            CatERing.getInstance().getKitchenManager().chooseSummarySheet(sheet);
            kitchenManagementController.showCurrentSheet();
        } catch (UseCaseLogicException | KitchenException ex) {
            ex.printStackTrace();
        }

    }

    public void setParent(KitchenManagement kitchenManagement) {
        kitchenManagementController = kitchenManagement;
    }

    @FXML
    public void tabturniButtonPressed(){
        kitchenManagementController.showTabturniSheet();

    }


}
