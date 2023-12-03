package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class Rectangle extends Figure {

    private final Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        super(new Point[]{topLeft, bottomRight});
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    public boolean figureBelongs(Point eventPoint){
        return (eventPoint.getX() > this.getTopLeft().getX() && eventPoint.getX() < this.getBottomRight().getX() &&
                eventPoint.getY() > this.getTopLeft().getY() && eventPoint.getY() < this.getBottomRight().getY());
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(hasGradient()) {
            LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, getFillColor()),
                    new Stop(1, getFillColor().invert()));
            gc.setFill(linearGradient);
        }
        else{
            gc.setFill(getFillColor());
        }
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(), Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(), Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
    }

    public static Rectangle createFrom(Point startPoint, Point endPoint){
        return new Rectangle(startPoint, endPoint);
    }

    @Override
    public void addShadow(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(this.getTopLeft().getX() + 10.0,
                this.getTopLeft().getY() + 10.0,
                Math.abs(this.getTopLeft().getX() - this.getBottomRight().getX()),
                Math.abs(this.getTopLeft().getY() - this.getBottomRight().getY()));

    }

    @Override
    public void addBevel(GraphicsContext gc) {
        gc.setLineWidth(10);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeLine(getTopLeft().getX(), getTopLeft().getY(), getBottomRight().getX(), getTopLeft().getY());
        gc.strokeLine(getTopLeft().getX(), getTopLeft().getY(), getTopLeft().getX(), getBottomRight().getY());
        //gc.setStroke(Color.BLACK);
        //gc.strokeLine(x + width, y, x + width, y + height);
        //gc.strokeLine(x, y + height, x + width, y + height);
    }

}
