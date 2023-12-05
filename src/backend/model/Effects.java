package backend.model;

import javafx.scene.canvas.GraphicsContext;

public interface Effects {

    // aplies a shadow effect on elements of a GraphicsContext
    void addShadow(GraphicsContext gc);

    //applies a beveled effect on elements of a GraphicsContext
    void addBevel(GraphicsContext gc);

    //applies a gradient effect on elements of a graphics context
    void addGradient(GraphicsContext gc);

    //asks whether an element has a shadow effect applied
    boolean hasShadow();

    //asks whether an element has a bevel effect applied
    boolean hasBevel();
    //asks whether an element has a gradient effect applied
    boolean hasGradient();

}
