package businesslogic.kitchen;

import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Assignment {

    private String portions;
    private Recipe recipe;
    private boolean ready;
    private int estimatedTime;
    private User cook;
    private Shift shift;
    private int id;

    public Assignment(Recipe recipe) {
        this.recipe = recipe;
        cook = new User();
        shift = new Shift(0);
        portions = "Porzioni da definire";
        estimatedTime = 0;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String toString(){
        return recipe.toString();
    }

    public void setCook(User cook) {
        this.cook = cook;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public boolean deleteAssignment() {
        setCook(new User());
        setShift(new Shift(0));
        return true;
    }

    //PERSISTENZA

    public static ObservableList<Assignment> loadTasks(int id_sheet){

        ObservableList<Assignment> result = FXCollections.observableArrayList();

        String query = "SELECT * FROM assignments WHERE sheet= '"+id_sheet+"' ORDER BY position";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Recipe recipe = Recipe.loadRecipeById(rs.getInt("recipe"));
                Assignment task = new Assignment(recipe);
                task.id = rs.getInt("id");
                User user = User.loadUserById(rs.getInt("cook"));
                task.cook = user;
                task.estimatedTime = rs.getInt("estimatedtime");
                task.portions = rs.getString("portions");
                task.ready = rs.getBoolean("terminato");
                Shift shift = Shift.loadShiftById(rs.getInt("shift"));
                task.shift = shift;
                result.add(task);
            }
        });
        return result;
    }

    public static void saveNewAssignment(int sheet_id, int recipe_id, Assignment a, int pos) {
        String itemInsert = "INSERT INTO catering.Assignments (recipe, sheet, position) VALUES (" +
                recipe_id +
                ", " +
                sheet_id +
                ", " +
                pos + ");";
        PersistenceManager.executeUpdate(itemInsert);

        a.id = PersistenceManager.getLastId();
    }

    public static void setTaskReady(int task_id, boolean ready) {
        String query = "UPDATE assignments SET terminato = " + ready + " WHERE id = " + task_id;
        PersistenceManager.executeUpdate(query);
    }

    public static void modifyTask(Assignment task) {
        String query = "UPDATE assignments SET cook = " + task.getCuoco().getId() + ", shift = " + task.getShift().getId() + ", portions = '" + task.getPortions() + "', estimatedtime = " + task.getEstimatedTime() + " WHERE id = " + task.getId();
        PersistenceManager.executeUpdate(query);
    }

    public static void removeAssignment(Assignment assignment) {
        String query = "UPDATE assignments SET cook = " + assignment.getCuoco().getId() + ", shift = " + assignment.getShift().getId() + " WHERE id = " + assignment.getId();
        PersistenceManager.executeUpdate(query);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCuoco(){
        return cook;
    }

    public String getCook() {
        return cook.getId() != 0 ? cook.getUserName() : "Cuoco da assegnare";
    }

    public Shift getShift() {
        return shift;
    }

    public String getShiftString(){
        return shift.getId() != 0 ? shift.toString() : "Cuoco da assegnare";
    }

    public static void removeTask(Assignment task) {
        String rem = "DELETE FROM Assignments WHERE id = " + task.getId();
        PersistenceManager.executeUpdate(rem);
    }
}
