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

    private double getWidth() {
        return bottomRight.getX() - topLeft.getX();
    }

    private double getHeight() {
        return bottomRight.getY() - topLeft.getY();
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    public boolean figureBelongs(Point eventPoint){
        return (eventPoint.getX() > getTopLeft().getX() && eventPoint.getX() < getBottomRight().getX() &&
                eventPoint.getY() > getTopLeft().getY() && eventPoint.getY() < getBottomRight().getY());
    }

    @Override
    public boolean isContainedIn(Rectangle rectangle) {
        return topLeft.getX() >= rectangle.topLeft.getX() &&
                topLeft.getY() >= rectangle.topLeft.getY() &&
                bottomRight.getX() <= rectangle.bottomRight.getX() &&
                bottomRight.getY() <= rectangle.bottomRight.getY();
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
        else gc.setFill(getFillColor());
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(), getWidth(), getHeight());
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(), getWidth(), getHeight());
    }

    public static Rectangle createFrom(Point startPoint, Point endPoint) {
        return new Rectangle(startPoint, endPoint);
    }

    @Override
    public void addShadow(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(getTopLeft().getX() + 10.0, getTopLeft().getY() + 10.0,
                Math.abs(getTopLeft().getX() - getBottomRight().getX()),
                Math.abs(getTopLeft().getY() - getBottomRight().getY()));
    }

    @Override
    public void addBevel(GraphicsContext gc) {
        double x = getTopLeft().getX();
        double y = getTopLeft().getY();
        double width = Math.abs(x - getBottomRight().getX());
        double height = Math.abs(y - getBottomRight().getY());
        gc.setLineWidth(10);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeLine(x, y, x + width, y);
        gc.strokeLine(x, y, x, y + height);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x + width, y, x + width, y + height);
        gc.strokeLine(x, y + height, x + width, y + height);
        gc.setLineWidth(1);
    }

    public void rotateRight() {
        double widthAux = getWidth();
        double heightAux = getHeight();
        if(getHeight() <= getWidth()) {
            topLeft.move(widthAux/2 - heightAux/2, heightAux/2 - widthAux/2);
            bottomRight.move(-widthAux/2 + heightAux/2, -heightAux/2 + widthAux/2);
        }
        else {
            bottomRight.move(-widthAux/2 + heightAux/2, -heightAux/2 + widthAux/2);
            topLeft.move(widthAux/2 - heightAux/2, heightAux/2 - widthAux/2);
        }
    }

    @Override
    public void flipHorizontally() {
        double deltaX = this.getTopLeft().getX() - this.getBottomRight().getX();
        if(!isInvertedH) {
            isInvertedH = true;
            topLeft.move(-deltaX, 0);
            bottomRight.move(-deltaX, 0);
        }
        else {
            isInvertedH = false;
            topLeft.move(deltaX, 0);
            bottomRight.move(deltaX, 0);
        }
    }

    @Override
    public void flipVertically() {
        double deltaY = this.getTopLeft().getY() - this.getBottomRight().getY();
        if(!isInvertedV) {
            isInvertedV = true;
            topLeft.move(0, -deltaY);
            bottomRight.move(0, -deltaY);
        }
        else {
            isInvertedV = false;
            topLeft.move(0, deltaY);
            bottomRight.move(0, deltaY);
        }
    }



    @Override

    public void scale(double percent) {
        double deltaX = (percent/2) * getWidth();
        double deltaY = (percent/2) * getHeight();
        topLeft.move(-deltaX, -deltaY);
        bottomRight.move(deltaX,deltaY);
    }

}
