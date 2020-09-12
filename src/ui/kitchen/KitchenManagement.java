package ui.kitchen;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.Main;

import java.io.IOException;


public class KitchenManagement {

    @FXML
    Label userLabel;

    @FXML
    BorderPane sumSheetListPane;

    @FXML
    BorderPane containerPane;

    @FXML
    SumSheetList sumSheetListPaneController;

    BorderPane sheetContentPane;

    Main mainPaneController;
    SheetContent sheetContentPaneController;

    public void initialize() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sheet-content.fxml"));
        try {
            sheetContentPane = loader.load();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        sheetContentPaneController = loader.getController();
        sheetContentPaneController.setKitchenManagementController(this);

        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String uname = CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
            userLabel.setText(uname);
        }

        sumSheetListPaneController.setParent(this);

    }

    public void endKitchenManagement() {
        mainPaneController.showStartPane();
    }

    public void endSheetManagement(){
        /*sumSheetListPaneController.initialize();*/
        containerPane.setCenter(sumSheetListPane);
    }

    public void setMainPaneController(Main main) { mainPaneController = main; }

    public void showCurrentSheet() {
        sheetContentPaneController.initialize();
        containerPane.setCenter(sheetContentPane);
    }

    public void showTabturniSheet() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("shift-table.fxml"));
        try {
            BorderPane pane = loader.load();
            ShiftTable controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Tabellone dei turni");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
