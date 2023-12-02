package frontend;

import javafx.scene.layout.VBox;
import backend.CanvasState;

public class MainFrame extends VBox {

    public MainFrame(CanvasState canvasState) {
        getChildren().add(new AppMenuBar());
        StatusPane statusPane = new StatusPane();
        EffectsPane effectsPane = new EffectsPane();
        getChildren().add(new PaintPane(canvasState, statusPane, effectsPane));
        getChildren().add(statusPane);
        getChildren().add(effectsPane);
    }

}
