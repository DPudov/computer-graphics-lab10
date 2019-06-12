package sample;

public class Functions {
    public static final int CLASSIC = 0;
    public static final int SINC1 = 1;
    public static final int SINC2 = 2;
//    public static final String CLASSIC = "cos^2(x) - sin^2(z)";

    public static double classic(double x, double z) {
        double a = Math.cos(x);
        double b = Math.sin(z);
        return a * a - b * b;
    }

    private static double sinc(double arg) {
        if (arg == 0) {
            return 1;
        }
        return Math.sin(arg) / arg;
    }

    public static double sinc1(double x, double z) {
        double a = Math.sqrt(x * x + z * z);
        return a * sinc(a);
    }

    public static double sinc2(double x, double z) {
        double a = Math.sqrt(x * x + z * z);
        return sinc(Math.sin(a));
    }

    public static double function(int f, double x, double z) {
        switch (f) {
            case CLASSIC:
                return classic(x, z);

            case SINC1:
                return sinc1(x, z);

            case SINC2:
                return sinc2(x, z);
            default:
                return classic(x, z);
        }
    }
}
