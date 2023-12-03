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

}
