package backend.model;

//We don't use F in this interface, but the methods here do require Movable
public interface Transformable<F extends Movable> {

    //Rotates figure 90 degrees to the right
    void rotateRight();

    //Rotates the figure on its y axis
    void flipHorizontally();

    //Rotates the figure on its x axis
    void flipVertically();

    //Increases/decreases the width and height of the figure a certain percent
    void scale(double percent);

}
