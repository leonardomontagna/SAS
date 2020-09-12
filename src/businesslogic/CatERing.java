package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.kitchen.KitchenManager;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.shift.ShiftManager;
import businesslogic.user.UserManager;
import persistence.KitchenPersistence;
import persistence.MenuPersistence;
import persistence.PersistenceManager;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private KitchenManager kitchenMgr;
    private ShiftManager shiftMgr;

    private MenuPersistence menuPersistence;
    private KitchenPersistence kitchenPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        kitchenMgr = new KitchenManager();
        shiftMgr = new ShiftManager();
        menuPersistence = new MenuPersistence();
        kitchenPersistence = new KitchenPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        kitchenMgr.addEventReceinver(kitchenPersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public KitchenManager getKitchenManager() { return kitchenMgr; }

    public ShiftManager getShiftManager() { return shiftMgr; }
}
