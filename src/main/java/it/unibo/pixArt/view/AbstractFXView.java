package it.unibo.pixArt.view;

import javafx.stage.Stage;

/**
 * doc.
 */
public abstract class AbstractFXView extends SimpleView implements JavaFXView {
    private Stage stage;

    @Override
    public final void setStage(final Stage stage) {
        this.stage = stage;
    }

    @Override
    public final Stage getStage() {
        return this.stage;
    }
}