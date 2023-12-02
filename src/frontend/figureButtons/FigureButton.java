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
    public Figure createAndAddFigure(Point startPoint, Point endPoint){
        try {
            //todo falta lo de agregar al canvas y settear el color.
        }
        catch(Exception e){
            paintPane.getStatusPane().updateStatus(e.getMessage());
        }
    }
    public abstract Figure createFigure(Point startPoint, Point endPoint);

}
