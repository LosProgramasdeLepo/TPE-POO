package frontend.figureButtons;

import backend.CanvasState;
import backend.model.*;
import frontend.PaintPane;

public abstract class FigureButton {
    protected final PaintPane paintPane;
    protected final CanvasState canvasState;

    public FigureButton(PaintPane paintPane, CanvasState canvasState){
        this.canvasState= canvasState;
        this.paintPane= paintPane;
    }
    public void createAndAddFigure(Point startPoint, Point endPoint) {
        try {
            Figure newFigure = createFigure(startPoint, endPoint);
            canvasState.add(newFigure);
            newFigure.setFillColor(paintPane.getColorFromPicker());
        } catch (Exception e) {
            paintPane.getStatusPane().updateStatus(e.getMessage());
        }
    }
    public abstract Figure createFigure(Point startPoint, Point endPoint);

}
