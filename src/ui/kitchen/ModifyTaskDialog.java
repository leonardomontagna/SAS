package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.shift.Shift;
import businesslogic.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyTaskDialog {

    @FXML
    ComboBox<User> cuoco;

    @FXML
    ComboBox<Shift> turno;

    @FXML
    TextField tempo;

    @FXML
    TextField porzioni;

    Stage myStage;

    private User cookSel;
    private Shift shiftSel;
    private int tempoSel;
    private String porzioniSel;

    public void initialize() {
        cuoco.setItems(CatERing.getInstance().getUserManager().getAllCook());
        turno.setItems(CatERing.getInstance().getShiftManager().getShiftsNonSaturi());
    }

    public void setOwnStage(Stage stage) {
        this.myStage = stage;
    }

    @FXML
    public void annullaButtonPressed() {
        myStage.close();
    }

    @FXML
    public void modificaButtonPressed() {
        cookSel = cuoco.getSelectionModel().getSelectedItem();
        shiftSel  = turno.getSelectionModel().getSelectedItem();
        if(tempo.getText().equals("")){
            tempoSel = 0;
        } else {
            tempoSel = Integer.parseInt(tempo.getText());
        }
        if(porzioni.getText() == null || porzioni.getText().trim().isEmpty()){
            porzioniSel = "Porzioni da definire";
        } else {
            porzioniSel = porzioni.getText();
        }
        myStage.close();
    }

    public User getCookSel(){
        return cookSel;
    }

    public Shift getShiftSel() {
        return shiftSel;
    }

    public int getTempoSel() {
        return tempoSel;
    }

    public String getPorzioniSel() {
        return porzioniSel;
    }
}
