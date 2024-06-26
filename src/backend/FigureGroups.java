package backend;

import backend.model.Figure;

import java.util.HashSet;

public class FigureGroups extends HashSet<FigureSelection> {

    public void group(FigureSelection figureSelection, CanvasState canvasState) {
        //If it already belongs to a group, no new group is created
        for(Figure figure : figureSelection) {
            if(findGroup(figure) != null) {
                return;
            }
        }
        //If not, a new group is created and figures are added
        FigureSelection newFigureGroup = new FigureSelection();
        for(Figure figure : figureSelection) {
            if(canvasState.contains(figure)) {
                newFigureGroup.add(canvasState.getFigure(figure));
            }
        }
        add(newFigureGroup);
    }

    public void ungroup(FigureSelection figureSelection) {
        remove(figureSelection);
    }

    public FigureSelection findGroup(Figure figure) {
        for(FigureSelection figureSelection : this) {
            if(figureSelection.contains(figure)) {
                return figureSelection;
            }
        }
        return null;
    }

}
