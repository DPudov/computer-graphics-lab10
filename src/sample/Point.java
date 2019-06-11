package sample;

public class Point {
    public static Point Zero = new Point(0, 0, 0);
    protected double[] e;

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
        double k = 60;
        double centX = 300;
        double centY = 300;
        rotateOX(absX);
        rotateOY(absY);
        rotateOZ(absZ);
        e[0] = (e[0] * k + centX);
        e[1] = (e[1] * k + centY);
    }
}