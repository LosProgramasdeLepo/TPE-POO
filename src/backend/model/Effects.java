package backend.model;

import javafx.scene.canvas.GraphicsContext;

public interface Effects {

    void addShadow(GraphicsContext gc);

    void addBevel(GraphicsContext gc);

    void addGradient(GraphicsContext gc);

    boolean hasShadow();
    boolean hasBevel();
    boolean hasGradient();

}
