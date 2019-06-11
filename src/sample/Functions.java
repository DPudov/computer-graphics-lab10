package sample;

public class Functions {
    public static final int CLASSIC = 0;

    public static double classic(double x, double z) {
        double a = Math.cos(x);
        double b = Math.sin(z);
        return a * a - b * b;
    }

    public static double function(int f, double x, double z) {
        switch (f) {
            default:
                return classic(x, z);
        }
    }
}
