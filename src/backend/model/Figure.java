package backend.model;

public abstract class Figure implements Movable {

    private final Point[] points;

    protected Figure(Point[] points) {
        this.points = points;
    }

    public abstract boolean figureBelongs(Point eventPoint);

    @Override
    public void move(double deltaX, double deltaY) {
        for(Point point : points) {
            point.move(deltaX, deltaY);
        }
    }

}
