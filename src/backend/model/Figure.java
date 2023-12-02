package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Figure implements Movable, Drawable {

    private final Point[] points;
    private Color fillColor;

    protected Figure(Point[] points) {
        this.points = points;
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
    public abstract Figure createFrom(Point startPoint, Point endPoint);

    @Override
    public abstract void draw(GraphicsContext gc);

}
