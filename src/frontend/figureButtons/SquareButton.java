package frontend.figureButtons;

import backend.CanvasState;
import backend.model.Square;
import frontend.PaintPane;

public class SquareButton extends RectangleButton{

    public SquareButton(PaintPane paintPane, CanvasState canvasState){
        super(paintPane,canvasState);
    }

    @Override
    public Square createFigure(backend.model.Point startPoint, backend.model.Point endPoint) {
        return Square.createFrom(startPoint,endPoint);
    }


}
