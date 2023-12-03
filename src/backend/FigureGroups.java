package backend;

import backend.model.Figure;

import java.util.HashSet;

public class FigureGroups extends HashSet<FigureSelection> {

    public void group(FigureSelection figureSelection) {
        this.add(figureSelection);
    }

    public void ungroup(FigureSelection figureSelection) {
        this.remove(figureSelection);
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
