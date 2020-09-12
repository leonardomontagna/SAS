package ui.kitchen;


import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchen.Assignment;
import businesslogic.kitchen.SummarySheet;
import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Optional;

public class SheetContent {
    KitchenManagement kitchenManagementController;
    @FXML
    ListView<Assignment> taskList;
    @FXML
    BorderPane assignmentsPane;

    /*@FXML
    Label serviceTitle;*/
    @FXML
    Label cuoco;
    @FXML
    Label turno;
    @FXML
    Label ricetta;
    @FXML
    Label porzioni;
    @FXML
    Label tempoStimato;
    @FXML
    Label pronto;
    @FXML
    ToggleButton prontoButton;

    private User cuocodb;
    private Shift turnodb;
    private int tempodb = 0;
    private String porzionidb = "Porzioni da definire";


    public void initialize(){
      SummarySheet sheet = CatERing.getInstance().getKitchenManager().getCurrentSumSheet();
        if(sheet != null){
        //serviceTitle.setText(sheet.getCurrentService().getName());
        taskList.setItems(sheet.getAssignments());
    }}

    public void setKitchenManagementController(KitchenManagement kitchenManagement) {
        kitchenManagementController = kitchenManagement;

    }
    @FXML
    public void fineButtonPressed(){
        kitchenManagementController.endSheetManagement();
    }


    @FXML
    public void aggiungiButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new-task-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            NewTaskDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Aggiungi compito");
            stage.setScene(new Scene(pane));

            stage.showAndWait();

            Recipe recipe = controller.getSelectedRecipe();
            if(recipe != null){
                CatERing.getInstance().getKitchenManager().addTask(recipe);
            }
        } catch (IOException | UseCaseLogicException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void deleteTaskButtonPressed() {
        Assignment task = taskList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("cancellazione di compito");
        alert.setHeaderText("Sei cicuro di voler cancellare il compito?");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        boolean deleteTask = false;
        if (result.get() == buttonTypeOne) {
            deleteTask = true;
        }
        try {
            CatERing.getInstance().getKitchenManager().deleteTask(task);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void upItemPressed() {
        this.changeTaskPosition(-1);
    }

    @FXML
    public void downItemPressed() {
        this.changeTaskPosition(+1);
    }

    private void changeTaskPosition(int change) {
        int newpos = taskList.getSelectionModel().getSelectedIndex() + change;
        Assignment assignment = taskList.getSelectionModel().getSelectedItem();
        try {
            CatERing.getInstance().getKitchenManager().sortRecipes(assignment, newpos);
            taskList.refresh();
            taskList.getSelectionModel().select(newpos);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void showAssignment() {

        Assignment task = taskList.getSelectionModel().getSelectedItem();
        ricetta.setText(task.getRecipe().toString());
        cuoco.setText(task.getCook());
        turno.setText(task.getShift().toString());
        tempoStimato.setText(String.valueOf(task.getEstimatedTime()) + " minuti");
        porzioni.setText(task.getPortions());
        pronto.setText(task.isReady() ? "E' pronto" : "Non pronto");
        assignmentsPane.setVisible(true);
    }


    @FXML
    public void prontoButtonPressed() throws UseCaseLogicException {
        Assignment task = taskList.getSelectionModel().getSelectedItem();
        if(prontoButton.isSelected()){
            pronto.setText("E' pronto");
            CatERing.getInstance().getKitchenManager().setReady(task, true);
        } else {
            pronto.setText("Non è pronto");
            CatERing.getInstance().getKitchenManager().setReady(task, false);
        }
    }

    @FXML
    public void resetButtonPressed() throws UseCaseLogicException {
        cuoco.setText("Cuoco da assegnare");
        turno.setText("Turno da assegnare");
        Assignment assignment = taskList.getSelectionModel().getSelectedItem();
        CatERing.getInstance().getKitchenManager().deleteAssignment(assignment);
    }


    @FXML
    public void tabturniButtonPressed(){
        kitchenManagementController.showTabturniSheet();

    }

    @FXML
    public void modificaButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify-task-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            ModifyTaskDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifica compito");
            stage.setScene(new Scene(pane));

            stage.showAndWait();

            Assignment task = taskList.getSelectionModel().getSelectedItem();

            if(controller.getCookSel() != null) {
                cuoco.setText(controller.getCookSel().getUserName());
                cuocodb = controller.getCookSel();
            } else {
                cuocodb = task.getCuoco();
            }

            if(controller.getShiftSel() != null){
                turno.setText(controller.getShiftSel().toString());
                turnodb = controller.getShiftSel();
                System.out.println("SHIFT" + turnodb);
            } else {
                turnodb = task.getShift();
            }

            if(!controller.getPorzioniSel().equals("Porzioni da definire")){
                porzioni.setText(controller.getPorzioniSel());
                porzionidb = controller.getPorzioniSel();
            } else {
                porzionidb = task.getPortions();
            }

            if(controller.getTempoSel() != 0){
                tempoStimato.setText(String.valueOf(controller.getTempoSel()));
                tempodb = controller.getTempoSel();
            } else {
                tempodb = task.getEstimatedTime();
            }

            CatERing.getInstance().getKitchenManager().modifyTask(task, cuocodb, turnodb, tempodb, porzionidb);

        } catch (IOException | UseCaseLogicException e) {
            e.printStackTrace();
        }

    }
}
