package backend;

import backend.model.Figure;

import java.util.HashSet;

public class FigureSelection extends HashSet<Figure> {

    public void modifyShadow(boolean status) {
        for (Figure figure : this) {
            figure.modifyShadow(status);
        }
    }

    public void modifyGradient(boolean status) {
        for (Figure figure : this) {
            figure.modifyGradient(status);
        }
    }

    public void modifyBevel(boolean status) {
        for (Figure figure : this) {
            figure.modifyBevel(status);
        }
    }
    //todo MAL MAL SACARLO
    public String toString(){
        String result= "Resultado:" ;
        for(Figure figure: this ){
            result += figure.toString();
        }
        return result;
    }


}
