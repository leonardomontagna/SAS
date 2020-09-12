package ui;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import ui.kitchen.KitchenManagement;
import ui.kitchen.SheetContent;
import ui.menu.MenuManagement;

import java.io.IOException;

public class Main {

    @FXML
    AnchorPane paneContainer;

    @FXML
    FlowPane startPane;

    @FXML
    Start startPaneController;

    BorderPane menuManagementPane;
    BorderPane kitchenManagementPane;

    MenuManagement menuManagementPaneController;
    KitchenManagement kitchenManagementPaneController;
    SheetContent sheetContentPaneController;

    public void initialize() {
        startPaneController.setParent(this);


    }

    public void startMenuManagement() {
        paneContainer.getChildren().remove(startPane);
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu/menu-management.fxml"));
            menuManagementPane = loader.load();
            menuManagementPaneController = loader.getController();
            menuManagementPaneController.setMainPaneController(this);
            menuManagementPaneController.initialize();
            paneContainer.getChildren().add(menuManagementPane);
            AnchorPane.setTopAnchor(menuManagementPane, 0.0);
            AnchorPane.setBottomAnchor(menuManagementPane, 0.0);
            AnchorPane.setLeftAnchor(menuManagementPane, 0.0);
            AnchorPane.setRightAnchor(menuManagementPane, 0.0);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showStartPane() {
        startPaneController.initialize();
        paneContainer.getChildren().remove(menuManagementPane);
        paneContainer.getChildren().remove(kitchenManagementPane);
        paneContainer.getChildren().add(startPane);
    }

    public void showListPane() {

    }

    public void startKitchenManagement() {
        paneContainer.getChildren().remove(startPane);
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("kitchen/kitchen-management.fxml"));
            kitchenManagementPane = loader.load();
            kitchenManagementPaneController = loader.getController();
            kitchenManagementPaneController.setMainPaneController(this);
            kitchenManagementPaneController.initialize();
            paneContainer.getChildren().add(kitchenManagementPane);
            AnchorPane.setTopAnchor(kitchenManagementPane, 0.0);
            AnchorPane.setBottomAnchor(kitchenManagementPane, 0.0);
            AnchorPane.setLeftAnchor(kitchenManagementPane, 0.0);
            AnchorPane.setRightAnchor(kitchenManagementPane, 0.0);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}