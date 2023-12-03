package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;

public class Ellipse extends Figure {

    protected final Point centerPoint;
    protected final double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        super(new Point[]{centerPoint});
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
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

    @Override
    public boolean figureBelongs(Point eventPoint) {
        return (((Math.pow(eventPoint.getX() - this.getCenterPoint().getX(), 2) / Math.pow(this.getsMayorAxis(), 2)) +
                (Math.pow(eventPoint.getY() - this.getCenterPoint().getY(), 2) / Math.pow(this.getsMinorAxis(), 2))) <= 0.30);
    }

    @Override
    public boolean isContainedIn(Rectangle rectangle) {
        Point topLeft = rectangle.getTopLeft();
        Point bottomRight = rectangle.getBottomRight();
        return topLeft.getX() <= centerPoint.getX() - sMayorAxis && topLeft.getY() >= centerPoint.getY() + sMinorAxis
                && bottomRight.getX() >= centerPoint.getX() + sMayorAxis && bottomRight.getY() <= centerPoint.getY() - sMinorAxis;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(hasGradient()) {
            RadialGradient radialGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, getFillColor()),
                    new Stop(1, getFillColor().invert()));
            gc.setFill(radialGradient);
        }
        else{
            gc.setFill(this.getFillColor());
        }
        gc.fillOval(getCenterPoint().getX() - (getsMayorAxis() / 2), getCenterPoint().getY() - (getsMinorAxis() / 2), getsMayorAxis(), getsMinorAxis());

        if(!this.hasBevel()) {
            gc.strokeOval(getCenterPoint().getX() - (getsMayorAxis() / 2), getCenterPoint().getY() - (getsMinorAxis() / 2), getsMayorAxis(), getsMinorAxis());
        }
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
        gc.fillOval(this.getCenterPoint().getX() - this.getsMayorAxis()/2 + 10.0,
                this.getCenterPoint().getY() - this.getsMinorAxis()/2 + 10.0, this.sMayorAxis, this.sMinorAxis );
    }

    @Override
    public void addBevel(GraphicsContext gc) {
        double arcX = this.getCenterPoint().getX() - this.getsMayorAxis()/2;
        double arcY = this.getCenterPoint().getY() - this.getsMinorAxis()/2;
        gc.setLineWidth(10);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeArc(arcX, arcY, this.getsMayorAxis(), this.getsMinorAxis() , 45, 180, ArcType.OPEN);
        gc.setStroke(Color.BLACK);
        gc.strokeArc(arcX, arcY, this.getsMayorAxis(), this.getsMinorAxis() , 225, 180, ArcType.OPEN);

    }

}
