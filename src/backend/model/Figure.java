package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Objects;

public abstract class Figure implements Movable, Drawable, Effects, Transformable<Figure> {
    private boolean shadow, bevel, gradient;
    private final Point[] points;
    private Color fillColor;
    protected boolean isInvertedH, isInvertedV;

    protected Figure(Point[] points) {
        this.points = points;
        shadow = false;
        bevel = false;
        gradient = false;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public abstract boolean figureBelongs(Point eventPoint);

    public abstract boolean isContainedIn(Rectangle rectangle);

    @Override
    public void move(double deltaX, double deltaY) {
        for(Point point : points) {
            point.move(deltaX, deltaY);
        }
    }

    @Override
    public abstract void draw(GraphicsContext gc);

    public boolean hasShadow() {
        return shadow;
    }

    public boolean hasBevel() {
        return bevel;
    }

    public boolean hasGradient() {
        return gradient;
    }

    public void modifyShadow(boolean status) {
        shadow = status;
    }

    public void modifyGradient(boolean status) {
        gradient = status;
    }

    public void modifyBevel(boolean status) {
        bevel = status;
    }

    @Override
    public abstract void addShadow(GraphicsContext gc);

    @Override
    public abstract void addBevel(GraphicsContext gc);

    public void addGradient(GraphicsContext gc) {
        draw(gc);
    }

   @Override
    public int hashCode() {
      return Objects.hash(Arrays.hashCode(points), bevel, shadow, gradient, fillColor);
    }

}
