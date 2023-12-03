package backend;

import backend.model.*;

import java.util.*;

public class CanvasState extends ArrayList<Figure> {

    public Figure getTopFigureAt(Point point) {
        for (int i = this.size() - 1; i >= 0; i--) {
            Figure f = this.get(i);
            if (f.figureBelongs(point))
                return f;
        }
        return null;
    }

    public void figuresContainedIn(Rectangle rectangle, FigureSelection figureSelection) {
        for(Figure figure : this) {
            if(figure.isContainedIn(rectangle)) {
                figureSelection.add(figure);
            }
        }
    }

}
