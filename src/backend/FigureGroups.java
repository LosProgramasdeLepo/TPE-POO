package backend;

import backend.model.Figure;

import java.util.HashSet;

public class FigureGroups extends HashSet<FigureSelection> {

    public void group(FigureSelection figureSelection) {
        for(Figure figure : figureSelection){
            for(FigureSelection figures : this){
                if(figures.contains(figure)){
                    return;
                }
            }
        }
        this.add(figureSelection);
    }

    public void ungroup(FigureSelection figureSelection) {
        this.remove(figureSelection);
    }

    //todo no guarda bien la selección; se supone que tiene que incluir todas
    // quiero saber si esta figura ya existe en algún grupo:
    public FigureSelection findGroup(Figure figure) {
        for(FigureSelection figureSelection : this) {
            if(figureSelection.contains(figure)){
                System.out.println(this.size());
                System.out.println("Esta figura está en algún grupo");
                return figureSelection;
            }
            else{
                System.out.println("Esta figura no está en ningún grupo");
            }
        }
        return null;
    }

}
