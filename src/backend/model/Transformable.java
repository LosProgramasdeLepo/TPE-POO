package backend.model;

public interface Transformable<F extends Movable> {

    // rotates figure 90 degrees to the right
    void rotateRight();

    // rotates the figure on its y axis
    void flipHorizontally();

    //rotates the figure on its x axis
    void flipVertically();

    //inccreases/decreases the width and height of the figure a certain percent
    void scale(double percent);

}
