package frontend.figureButtons;

import backend.CanvasState;
import backend.model.Figure;
import backend.model.Point;
import backend.model.Rectangle;
import frontend.PaintPane;

public class RectangleButton extends FigureButton{

    public RectangleButton(PaintPane paintPane, CanvasState canvasState){
        super(paintPane,canvasState);
    }
    @Override
    public Figure createFigure(Point startPoint, Point endPoint){
        return Rectangle.createFrom(startPoint,endPoint);
    }

}
