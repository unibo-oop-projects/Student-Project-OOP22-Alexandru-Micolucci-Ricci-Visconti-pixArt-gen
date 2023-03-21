package it.unibo.pixArt.view.pages;

import it.unibo.pixArt.controller.Controller;
import it.unibo.pixArt.controller.impl.AnimationController;
import it.unibo.pixArt.controller.impl.HistoryController;
import it.unibo.pixArt.controller.impl.HomeController;
import it.unibo.pixArt.controller.impl.WorkSpaceController;

public enum Pages {
    /**
     * menu view
     */
    MENU("menu", new HomeController()),

    /**
     * workspace view
     */
    WORKSPACE("workspace", new WorkSpaceController()),

    /**
     * animation view
     */
    ANIMATION("animation", new AnimationController()),

    /**
     * history view
     */
    HISTORY("historyproject", new HistoryController());

    private final String name;

    private final Controller controller;

    /**
     * @param name       of the view
     * @param controller the associated to the view
     */
    Pages(final String name, final Controller controller) {
        this.name = name;
        this.controller = controller;
    }

    /**
     * @return the name of the view
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the controller of the view
     */
    public Controller getController() {
        return this.controller;
    }
}
