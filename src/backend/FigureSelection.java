package backend;

import backend.model.Figure;

import java.util.HashSet;
import java.util.Objects;

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
    //todo MAL MAL SACARLO es para tests solamente
    public String toString(){
        String result= "Resultado:" ;
        for(Figure figure: this ){
            result += figure.toString();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FigureSelection otherSelection = (FigureSelection) obj;
        return this.containsAll(otherSelection) && otherSelection.containsAll(this);
    }
    @Override
    public int hashCode(){
        int hashCode = 1;
        for(Figure figure : this){
            hashCode = 31 * hashCode + Objects.hashCode(figure);
        }
        return hashCode;
    }

}
