public class Triangle extends Set {

    private String name;
    private Point point1 , point2 , point3;
    private Line line1 , line2;

    public Triangle(String name, Point point1, Point point2, Point point3) {
        this.name = name;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.line1 = new Line(point1 , point2);
        this.line2 = new Line(point2 , point3);
    }

    public Triangle(String name, Line line1, Line line2) {
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.point1 = line1.getPoint1();
        this.point2 = line1.getPoint2();
        this.point3 = line2.getPoint2();
    }

    @Override
    public double getMembership(int x) {
        if(line1.inRange(x))
            return line1.getY(x);
        else if(line2.inRange(x))
            return line2.getY(x);
        else
            return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCentroid() {
        int numberOfPoints = 3;
        int x[] = {point1.getX() , point2.getX() , point3.getX()};
        int y[] = {point1.getY() , point2.getY() , point3.getY()};
        return calcCentroid(numberOfPoints , x ,y);
    }

}
