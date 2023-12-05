package backend.model;

import javafx.scene.canvas.GraphicsContext;

public interface Effects {

    //Applies a shadow effect on elements of a GraphicsContext
    void addShadow(GraphicsContext gc);

    //Applies a beveled effect on elements of a GraphicsContext
    void addBevel(GraphicsContext gc);

    //Applies a gradient effect on elements of a graphics context (it isn't used, but is crucial to the interface logic)
    void addGradient(GraphicsContext gc);

    //Asks whether an element has a shadow effect applied
    boolean hasShadow();

    //Asks whether an element has a bevel effect applied
    boolean hasBevel();

    //Asks whether an element has a gradient effect applied
    boolean hasGradient();

}
