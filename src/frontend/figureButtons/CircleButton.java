package frontend.figureButtons;

import backend.CanvasState;
import backend.model.Circle;
import backend.model.Point;
import frontend.PaintPane;

public class CircleButton extends EllipseButton{

    public CircleButton(PaintPane paintPane, CanvasState canvasState){
        super(paintPane,canvasState);
    }

    @Override
    public Circle createFigure(Point startPoint, Point endPoint){
        return Circle.createFrom(startPoint, endPoint);
    }
}
