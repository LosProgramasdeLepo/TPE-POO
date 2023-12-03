package frontend;

import javafx.scene.layout.BorderPane;
import backend.CanvasState;

public class MainFrame extends BorderPane {

    public MainFrame(CanvasState canvasState) {
        AppMenuBar appMenuBar = new AppMenuBar();
        StatusPane statusPane = new StatusPane();
        PaintPane paintPane = new PaintPane(canvasState, statusPane);
        setTop(appMenuBar);
        setLeft(paintPane);
        setBottom(statusPane);
    }

}
