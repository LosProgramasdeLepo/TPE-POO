package backend;

import backend.model.Figure;

import java.util.HashSet;

public class FigureGroups extends HashSet<FigureSelection> {

    public void group(FigureSelection figureSelection, CanvasState canvasState) {
        //si ya está en algún grupo alguna de las figuras, no hago un nuevo grupo
        for(Figure figure : figureSelection){
            if(this.findGroup(figure) != null) {
                return;
            }
        }
        // si no existe, creo una nueva colección y las agrego
        FigureSelection newFigureGroup = new FigureSelection();
        for(Figure figure : figureSelection){
            if(canvasState.contains(figure)) {
                newFigureGroup.add(canvasState.getFigure(figure));
            }
        }
        this.add(newFigureGroup);
    }

    public void ungroup(FigureSelection figureSelection) {
        this.remove(figureSelection);
    }

    public FigureSelection findGroup(Figure figure) {
        for(FigureSelection figureSelection : this){
            if(figureSelection.contains(figure)) {
                return figureSelection;
            }
        }
        return null;
    }

}
