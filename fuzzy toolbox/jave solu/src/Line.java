public class Line {

    private Point point1, point2;
    private double slope , bias;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        slope = (double)(point1.getY() - point2.getY()) / (double)(point1.getX() - point2.getX());
        bias = point1.getY() - slope * point1.getX();
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public boolean inRange(int x)
    {
        return x >= point1.getX() && x <= point2.getX();
    }

    public double getY(int x)
    {
        return x * slope + bias;
    }

}
