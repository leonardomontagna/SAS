package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.Optional;

public class NewTaskDialog {

    Stage myStage;
    private Recipe selectedRecipe;

    @FXML
    ComboBox<Recipe> recipeCombo;

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }

    public void initialize() {
        recipeCombo.setItems(CatERing.getInstance().getRecipeManager().getRecipes());
    }

    @FXML
    public void recipeComboSelection() {
        Recipe sel = recipeCombo.getValue();
    }

    @FXML
    public void aggiungiButtonPressed() {
        selectedRecipe = recipeCombo.getValue();
        myStage.close();
    }

    @FXML
    public void annullaButtonPressed() {
        myStage.close();
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }
}
