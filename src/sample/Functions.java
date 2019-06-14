package sample;

public class Functions {
    public static final int CLASSIC = 0;
    public static final int SINC1 = 1;
    public static final int SINC2 = 2;
    public static final int STAIRS = 3;
    public static final int PYRAMID = 4;
    public static final int LETTERA = 5;
    public static final int BEACON = 6;
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

    private static double sign(double x) {
        if (x > 0) {
            return 1;

        }
        if (x < 0) {
            return -1;
        }
        return 0;
    }

    public static double beacon(double x, double z) {
        double arg = Math.sqrt(x * x + z * z);
        return Math.tan(arg / 3) + arg;
    }


    public static double stairs(double x, double z) {
        return (sign(-.65 - x) + sign(-.35 - x) + sign(-.05 - x) + sign(.25 - x) + sign(.55 - x)) / 7;
    }

    public static double pyramid(double x, double z) {
        return 1 - Math.abs(x + z) - Math.abs(z - x);
    }

    public static double letterA(double x, double z) {
        return ((1 - sign(-x - .9 + Math.abs(z * 2))) / 3 * (sign(.9 - x) + 1) / 3) * (sign(x + .65) + 1) / 2 - ((1 - sign(-x - .39 + Math.abs(z * 2))) / 3 * (sign(.9 - x) + 1) / 3) + ((1 - sign(-x - .39 + Math.abs(z * 2))) / 3 * (sign(.6 - x) + 1) / 3) * (sign(x - .35) + 1) / 2;
    }

    public static double function(int f, double x, double z) {
        switch (f) {
            case CLASSIC:
                return classic(x, z);

            case SINC1:
                return sinc1(x, z);

            case SINC2:
                return sinc2(x, z);

            case STAIRS:
                return stairs(x, z);
            case PYRAMID:
                return pyramid(x, z);

            case LETTERA:
                return letterA(x, z);

            case BEACON:
                return beacon(x, z);

            default:
                return classic(x, z);
        }
    }
}
