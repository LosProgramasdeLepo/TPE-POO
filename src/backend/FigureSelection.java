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

    public void rotateRight() {
        for (Figure figure : this) {
            figure.rotateRight();
        }
    }
    public void flipHorizontally() {
        for (Figure figure : this) {
            figure.flipHorizontally();
        }
    }

    public void flipVertically() {
        for (Figure figure : this) {
            figure.flipVertically();
        }
    }


    public void scaleUp() {
        for (Figure figure : this) {
            figure.scaleUp();
        }
    }

    public void scaleDown() {
        for (Figure figure : this) {
            figure.scaleDown();
        }
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
