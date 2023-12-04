package backend;

import backend.model.Figure;

import java.util.HashSet;

public class FigureGroups extends HashSet<FigureSelection> {

    public void group(FigureSelection figureSelection) {
        for(Figure figure : figureSelection){
            if(this.findGroup(figure) != null) {
                return;
            }
        }
        this.add(figureSelection);
    }

    public void ungroup(FigureSelection figureSelection) {
        this.remove(figureSelection);
    }

    //todo no guarda bien la selección; se supone que tiene que incluir todas
    public FigureSelection findGroup(Figure figure) {
        for(FigureSelection figureSelection : this){
            if(figureSelection.contains(figure)) {
                System.out.println("aaaaaaaaaaaaaaaa");
                return figureSelection;
            }
        }
        return null;
    }

}
