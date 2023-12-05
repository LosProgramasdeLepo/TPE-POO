package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;

public class Ellipse extends Figure {

    protected final Point centerPoint;
    protected double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        super(new Point[]{centerPoint});
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
    }

    private double getXRadius() {
        return sMayorAxis / 2;
    }

    private double getYRadius() {
        return sMinorAxis / 2;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    @Override
    public boolean figureBelongs(Point eventPoint) {
        return (((Math.pow(eventPoint.getX() - getCenterPoint().getX(), 2) / Math.pow(getsMayorAxis(), 2)) +
                (Math.pow(eventPoint.getY() - getCenterPoint().getY(), 2) / Math.pow(getsMinorAxis(), 2))) <= 0.30);
    }

    @Override
    public boolean isContainedIn(Rectangle rectangle) {
        Point topLeft = rectangle.getTopLeft();
        Point bottomRight = rectangle.getBottomRight();
        return topLeft.getX() <= centerPoint.getX() - getXRadius() &&
                topLeft.getY() <= centerPoint.getY() - getYRadius() &&
                bottomRight.getX() >= centerPoint.getX() + getXRadius() &&
                bottomRight.getY() >= centerPoint.getY() + getYRadius();
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(hasGradient()) {
            RadialGradient radialGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, getFillColor()),
                    new Stop(1, getFillColor().invert()));
            gc.setFill(radialGradient);
        }
        else gc.setFill(getFillColor());
        gc.fillOval(getCenterPoint().getX() - getXRadius(), getCenterPoint().getY() - getYRadius(), getsMayorAxis(), getsMinorAxis());
        gc.strokeOval(getCenterPoint().getX() - getXRadius(), getCenterPoint().getY() - getYRadius(), getsMayorAxis(), getsMinorAxis());
    }

    public static Ellipse createFrom(Point startPoint, Point endPoint) {
        Point centerPoint = new Point(Math.abs(endPoint.getX() + startPoint.getX()) / 2, (Math.abs((endPoint.getY() + startPoint.getY())) / 2));
        double sMayorAxis = Math.abs(endPoint.getX() - startPoint.getX());
        double sMinorAxis = Math.abs(endPoint.getY() - startPoint.getY());
        return new Ellipse(centerPoint, sMayorAxis, sMinorAxis);
    }

    @Override
    public void addShadow(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillOval(getCenterPoint().getX() - getXRadius() + 10.0, getCenterPoint().getY() - getYRadius() + 10.0, sMayorAxis, sMinorAxis );
    }

    @Override
    public void addBevel(GraphicsContext gc) {
        double arcX = getCenterPoint().getX() - getXRadius();
        double arcY = getCenterPoint().getY() - getYRadius();
        gc.setLineWidth(10);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeArc(arcX, arcY, getsMayorAxis(), getsMinorAxis() , 45, 180, ArcType.OPEN);
        gc.setStroke(Color.BLACK);
        gc.strokeArc(arcX, arcY, getsMayorAxis(), getsMinorAxis() , 225, 180, ArcType.OPEN);
        gc.setLineWidth(1);
    }

    @Override
    public void rotateRight() {
        double auxiliarAxis = sMayorAxis;
        sMayorAxis = sMinorAxis;
        sMinorAxis = auxiliarAxis;
    }

    @Override
    public void flipHorizontally() {
        double deltaX = this.sMayorAxis;
        if(!isInvertedH) {
            isInvertedH = true;
            centerPoint.move(deltaX, 0);
        }
        else {
            isInvertedH = false;
            centerPoint.move(-deltaX, 0);
        }
    }

    @Override
    public void flipVertically() {
        double deltaY = this.sMinorAxis;
        if(!isInvertedH) {
            isInvertedH = true;
            centerPoint.move(0, deltaY);
        }
        else {
            isInvertedH = false;
            centerPoint.move(0, -deltaY);
        }
    }
    @Override
    public void scale(double percent) {
        sMayorAxis = sMayorAxis + sMayorAxis*percent;
        sMinorAxis = sMinorAxis + sMinorAxis*percent;
    }



}
