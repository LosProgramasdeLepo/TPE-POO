package backend.model;

public class Circle extends Ellipse {

    public Circle(Point centerPoint, double radius) {
        super(centerPoint, radius, radius);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", centerPoint, getsMayorAxis());
    }

    public double getRadius() {
        return getsMayorAxis();
    }

    @Override
    public boolean figureBelongs(Point eventPoint) {
        return (Math.sqrt(Math.pow(this.getCenterPoint().getX() - eventPoint.getX(), 2) +
                Math.pow(this.getCenterPoint().getY() - eventPoint.getY(), 2)) < this.getRadius());
    }

    @Override
    public Circle createFrom(Point startPoint, Point endPoint){
        double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
        return new Circle(startPoint, circleRadius);
    }

}
