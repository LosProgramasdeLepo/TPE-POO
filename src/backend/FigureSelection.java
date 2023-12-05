package backend;

import backend.model.Figure;
import backend.model.Movable;
import backend.model.Transformable;

import java.util.HashSet;

public class FigureSelection extends HashSet<Figure> implements Movable, Transformable<FigureSelection> {

    public void modifyShadow(boolean status) {
        for(Figure figure : this) {
            figure.modifyShadow(status);
        }
    }

    public void modifyGradient(boolean status) {
        for(Figure figure : this) {
            figure.modifyGradient(status);
        }
    }

    public void modifyBevel(boolean status) {
        for(Figure figure : this) {
            figure.modifyBevel(status);
        }
    }

    @Override
    public void rotateRight() {
        for(Figure figure : this) {
            figure.rotateRight();
        }
    }

    @Override
    public void flipHorizontally() {
        for(Figure figure : this) {
            figure.flipHorizontally();
        }
    }

    @Override
    public void flipVertically() {
        for(Figure figure : this) {
            figure.flipVertically();
        }
    }

    @Override
    public void scale(double percent){
        for(Figure figure : this) {
            figure.scale(percent);
        }
    }

    @Override
    public void move(double deltaX,double deltaY){
        for(Figure figure : this) {
            figure.move(deltaX, deltaY);
        }
    }

    public boolean hasShadow() {
        for(Figure figure : this) {
            if(!figure.hasShadow()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBevel() {
        for(Figure figure : this) {
            if(!figure.hasBevel()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasGradient() {
        for(Figure figure : this) {
            if(!figure.hasBevel()) {
                return false;
            }
        }
        return true;
    }

    public boolean atLeastOneHasShadow() {
        for(Figure figure : this) {
            if(figure.hasShadow()) {
                return true;
            }
        }
        return false;
    }

    public boolean atLeastOneHasBevel() {
        for(Figure figure : this) {
            if(figure.hasBevel()) {
                return true;
            }
        }
        return false;
    }

    public boolean atLeastOneHasGradient() {
        for(Figure figure : this) {
            if(figure.hasGradient()) {
                return true;
            }
        }
        return false;
    }

}
