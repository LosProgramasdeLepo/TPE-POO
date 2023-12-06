package frontend.figureButtons;

import backend.CanvasState;
import backend.model.Ellipse;
import backend.model.Point;
import frontend.PaintPane;

public class EllipseButton extends FigureButton {

    public EllipseButton(PaintPane paintPane, CanvasState canvasState) {
        super(paintPane,canvasState);
    }

    @Override
    public Ellipse createFigure(Point startPoint, Point endPoint) {
        return Ellipse.createFrom(startPoint,endPoint);
    }

}
