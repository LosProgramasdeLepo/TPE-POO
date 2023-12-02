package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Figure implements Movable, Drawable, Effects {
    private boolean shadow, bevel, gradient;
    private final Point[] points;
    private Color fillColor;

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

    @Override
    public void move(double deltaX, double deltaY) {
        for(Point point : points) {
            point.move(deltaX, deltaY);
        }
    }
    @Override
    public abstract void draw(GraphicsContext gc);

    public boolean hasShadow(){
        return shadow;
    }

    public boolean hasBevel() {
        return bevel;
    }

    public boolean hasGradient() {
        return gradient;
    }

    public void modifyShadow(boolean bool){
        shadow = bool;
    }
    public void modifyGradient(boolean bool){
        gradient = bool;
    }
    public void modifyBevel(boolean bool){
        bevel = bool;
    }

    @Override
    public abstract void addShadow(GraphicsContext gc);
    @Override
    public abstract void addBevel(GraphicsContext gc);
    public void addGradient(GraphicsContext gc){
        draw(gc);
    }

}
