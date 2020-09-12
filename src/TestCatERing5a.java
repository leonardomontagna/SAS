

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.kitchen.Assignment;
import businesslogic.kitchen.KitchenException;
import businesslogic.kitchen.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuException;
import businesslogic.menu.Section;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TestCatERing5a {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");

            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");

            ObservableList<Recipe> recipes = CatERing.getInstance().getRecipeManager().getRecipes();
            for (int i=0; i < 4 && i < recipes.size(); i++) {
                CatERing.getInstance().getMenuManager().insertItem(recipes.get(i), sec);
            }

            for (int i=recipes.size()-1; i > recipes.size()-4 && i >= 0; i--) {
                CatERing.getInstance().getMenuManager().insertItem(recipes.get(i));
            }

            CatERing.getInstance().getMenuManager().publish();
            System.out.println("\nMENU CREATO");
            System.out.println(m.testString());

            System.out.println("\nTEST DELETE");
            CatERing.getInstance().getMenuManager().deleteMenu(m);

            ServiceInfo ser = CatERing.getInstance().getEventManager().fakeService("servizio di prova 1");
            System.out.println(ser);
            EventInfo ev = CatERing.getInstance().getEventManager().fakeEvent("evento di prova 1");
            System.out.println(ev);
            ser.setMenu(m);

            System.out.println("\nTEST CREATE SUM SHEET");

            SummarySheet ss = CatERing.getInstance().getKitchenManager().createSummarySheet(ev, ser);
            System.out.println(ss);


            ObservableList<Recipe> ricette = CatERing.getInstance().getMenuManager().getCurrentMenu().getRecipes();
            System.out.println(ricette);

            Assignment task = new Assignment(ricette.get(1));
            System.out.println(task.isReady());
            CatERing.getInstance().getKitchenManager().setReady(task, true);
            System.out.println(task.isReady());
            CatERing.getInstance().getKitchenManager().deleteTask(task);
            System.out.println(ss);

        } catch (UseCaseLogicException | MenuException | KitchenException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
