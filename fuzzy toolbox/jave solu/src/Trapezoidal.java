public class Trapezoidal extends Set {

    private String name;
    private Point point1 , point2 , point3 , point4;
    private Line line1 , line2 , line3;

    public Trapezoidal(String name, Point point1, Point point2, Point point3 , Point point4) {
        this.name = name;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.line1 = new Line(point1 , point2);
        this.line2 = new Line(point2 , point3);
        this.line3 = new Line(point3 , point4);
    }

    public Trapezoidal(String name, Line line1, Line line2 , Line line3) {
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.point1 = line1.getPoint1();
        this.point2 = line1.getPoint2();
        this.point3 = line2.getPoint2();
        this.point4 = line3.getPoint2();
    }

    @Override
    public double getMembership(int x) {
        if(line1.inRange(x))
            return line1.getY(x);
        else if(line2.inRange(x))
            return line2.getY(x);
        else if(line3.inRange(x))
            return  line3.getY(x);
        else
            return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCentroid() {
        int numberOfPoints = 4;
        int x[] = {point1.getX() , point2.getX() , point3.getX() , point4.getX()};
        int y[] = {point1.getY() , point2.getY() , point3.getY() , point4.getY()};
        return calcCentroid(numberOfPoints , x ,y);
    }
}
