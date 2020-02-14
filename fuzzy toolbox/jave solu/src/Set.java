public abstract class Set {

    //is there any better design in this case?

    abstract public double getMembership(int x);
    abstract public String getName();
    abstract public double getCentroid();
    protected double calcCentroid(int numberOfPoints , int[] x , int[] y)
    {
        double a = 0;
        for (int i = 0 ; i < numberOfPoints-1 ; i++)
            a += x[i] * y[i+1] - x[i+1] * y[i];
        a /= 2;
        double centroid = 0;
        for (int i = 0 ; i < numberOfPoints-1 ; i++)
            centroid += (x[i] + x[i+1]) * (x[i] * y[i+1] - x[i+1] * y[i]);
        centroid /= 6 * a;
        return centroid;
    }
}