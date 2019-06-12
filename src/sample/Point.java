package sample;

public class Point {
    public static Point Zero = new Point(0, 0, 0);
    protected double[] e;
    private static int width = 600;
    private static int height = 600;
    private static double xMax = 2;
    private static double xMin = -2;
    private static double yMax = 5;
    private static double yMin = -5;

    public Point(double x, double y, double z) {
        e = new double[3];
        e[0] = x;
        e[1] = y;
        e[2] = z;
    }

    public Point(double x, double y) {
        e = new double[3];
        e[0] = x;
        e[1] = y;
        e[2] = 0;
    }

    public Point(Point p) {
        e = new double[3];
        e[0] = p.getX();
        e[1] = p.getY();
        e[2] = p.getZ();
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Point.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Point.height = height;
    }

    public static double getxMax() {
        return xMax;
    }

    public static void setxMax(double xMax) {
        Point.xMax = xMax;
    }

    public static double getxMin() {
        return xMin;
    }

    public static void setxMin(double xMin) {
        Point.xMin = xMin;
    }

    public static double getyMax() {
        return yMax;
    }

    public static void setyMax(double yMax) {
        Point.yMax = yMax;
    }

    public static double getyMin() {
        return yMin;
    }

    public static void setyMin(double yMin) {
        Point.yMin = yMin;
    }

    public double getX() {
        return e[0];
    }

    public void setX(double x) {
        e[0] = x;
    }

    public double getY() {
        return e[1];
    }

    public void setY(double y) {
        e[1] = y;
    }

    public double getZ() {
        return e[2];
    }

    public void setZ(double z) {
        e[2] = z;
    }

    public Point add(Point p) {
        return new Point(this.e[0] + p.e[0], this.e[1] + p.e[1],
                this.e[2] + p.e[2]);
    }

    public Vector sub(Point p) {
        return new Vector(this.e[0] - p.e[0], this.e[1] - p.e[1],
                this.e[2] - p.e[2]);
    }

    public Point mul(Point p) {
        return new Point(this.e[0] * p.e[0], this.e[1] * p.e[1],
                this.e[2] * p.e[2]);
    }

    public Point div(Point p) {
        return new Point(this.e[0] / p.e[0], this.e[1] / p.e[1],
                this.e[2] / p.e[2]);
    }

    public Point add(double s) {
        return new Point(e[0] + s, e[1] + s, e[2] + s);
    }

    public Point sub(double s) {
        return new Point(e[0] - s, e[1] - s, e[2] - s);
    }

    public Point mul(double s) {
        return new Point(e[0] * s, e[1] * s, e[2] * s);
    }

    public Point div(double s) {
        double inv = 1.0f / s;
        return new Point(e[0] * inv, e[1] * inv, e[2] * inv);
    }

    public Point midPoint(Point p) {
        Point m = this.add(p);
        return m.mul(0.5f);
    }

    public void rotateOX(double theta) {
        theta = Math.toRadians(theta);
        double buf = e[1];
        e[1] = e[1] * Math.cos(theta) - e[2] * Math.sin(theta);
        e[2] = e[2] * Math.cos(theta) - buf * Math.sin(theta);
    }

    public void rotateOY(double theta) {
        theta = Math.toRadians(theta);
        double buf = e[0];
        e[0] = e[0] * Math.cos(theta) - e[2] * Math.sin(theta);
        e[2] = e[2] * Math.cos(theta) - buf * Math.sin(theta);
    }

    public void rotateOZ(double theta) {
        theta = Math.toRadians(theta);
        double buf = e[0];
        e[0] = e[0] * Math.cos(theta) - e[1] * Math.sin(theta);
        e[1] = e[1] * Math.cos(theta) - buf * Math.sin(theta);
    }

    public Vector toVector() {
        return new Vector(e[0], e[1], e[2]);
    }

    @Override
    public String toString() {
        return e[0] + ", " + e[1];
    }

    public void transform(double absX, double absY, double absZ) {
//        double k = 100;
//        double centX = (double) width / 2;
//        double centY = (double) height / 2;
        rotateOX(absX);
        rotateOY(absY);
        rotateOZ(absZ);
//        e[0] = (e[0] * k + centX);
//        e[1] = (e[1] * k + centY);
        e[0] = width / (xMax - xMin) * (e[0] - xMin);
        e[1] = height - height / (yMax - yMin) * (e[1] - yMin);
    }
}