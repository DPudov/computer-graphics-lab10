package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static sample.LineDrawer.DrawLine;

public class FloatHorizon {
    public static final int INVISIBLE = 0;
    public static final int ABOVE = 1;
    public static final int BELOW = -1;
    private static double xMax = 2;
    private static double xMin = -2;
    private static double yMax = 5;
    private static double yMin = -5;
    private static int width = 600;
    private static int height = 600;

    public static void floatHorizon(GraphicsContext gc, int func,
                                    double xMin, double xMax, double xStep,
                                    double zMin, double zMax, double zStep, Color color,
                                    double absX, double absY, double absZ) {

        int width = (int) gc.getCanvas().getWidth();
        int height = (int) gc.getCanvas().getHeight();
        int[] upHorizon = new int[width];
        int[] lowHorizon = new int[width];
        int xLeft = BELOW;
        int yLeft = BELOW;
        int xRight = BELOW;
        int yRight = BELOW;
        FloatHorizon.xMax = xMax;
        FloatHorizon.xMin = xMin;
        FloatHorizon.width = width;
        FloatHorizon.height = height;
        for (int i = 0; i < upHorizon.length; i++) {
            upHorizon[i] = 0;
        }
        for (int i = 0; i < lowHorizon.length; i++) {
            lowHorizon[i] = height;
        }
        int xPrev, yPrev;
        for (double z = zMax; z >= zMin; z -= zStep) {
            double yPrevTmp = Functions.function(func, xMin, z);

            Point point = new Point(xMin, yPrevTmp, z);
            point.transform(absX, absY, absZ);
            xPrev = (int) point.getX();
            yPrev = (int) point.getY();
//            z = point.getZ();
            if (xLeft != -1) {
                horizon(xLeft, yLeft, xPrev, yPrev, upHorizon, lowHorizon);
            }
            xLeft = xPrev;
            yLeft = yPrev;


            int prevFlag = visible(xPrev, yPrev, upHorizon, lowHorizon);
            double x = xMin;
            double y = yPrev;
            for (; x < xMax; x += xStep) {
                double curYTmp = Functions.function(func, x, z);
                //                y = Functions.function(func, x, z);
                Point p = new Point(x, curYTmp, z);
                p.transform(absX, absY, absZ);
                int curX = (int) p.getX();
                int curY = (int) p.getY();
//                x = p.getX();
//                y = p.getY();
//                System.out.println(p);
                int currentFlag = visible(curX, curY, upHorizon, lowHorizon);
                if (prevFlag == currentFlag) {
                    if (currentFlag == ABOVE || currentFlag == BELOW) {
                        DrawLine(gc.getCanvas(), xPrev, yPrev, curX, curY, color);
                        horizon(xPrev, yPrev, curX, curY, upHorizon, lowHorizon);
                    }
                } else if (currentFlag == INVISIBLE) {
                    Point inter;
                    if (prevFlag == ABOVE) {
                        inter = intersection(xPrev, yPrev, curX, curY, upHorizon);
                    } else {
                        inter = intersection(xPrev, yPrev, curX, curY, lowHorizon);
                    }
                    double xi = inter.getX();
                    double yi = inter.getY();
                    DrawLine(gc.getCanvas(), xPrev, yPrev, xi, yi, color);
                    horizon(xPrev, yPrev, (int) xi, (int) yi, upHorizon, lowHorizon);
                } else if (currentFlag == ABOVE) {
                    if (prevFlag == INVISIBLE) {
                        Point inter = intersection(xPrev, yPrev, curX, curY, upHorizon);
                        double xi = inter.getX();
                        double yi = inter.getY();
                        DrawLine(gc.getCanvas(), xi, yi, curX, curY, color);
                        horizon((int) xi, (int) yi, curX, curY, upHorizon, lowHorizon);
                    } else {
                        Point inter = intersection(xPrev, yPrev, curX, curY, lowHorizon);
                        double xi = inter.getX();
                        double yi = inter.getY();
                        DrawLine(gc.getCanvas(), xPrev, yPrev, xi, yi, color);
                        horizon(xPrev, yPrev, (int) xi, (int) yi, upHorizon, lowHorizon);
                        inter = intersection(xPrev, yPrev, curX, curY, upHorizon);
                        xi = inter.getX();
                        yi = inter.getY();
                        DrawLine(gc.getCanvas(), xi, yi, curX, curY, color);
                        horizon((int) xi, (int) yi, curX, curY, upHorizon, lowHorizon);
                    }
                } else if (prevFlag == INVISIBLE) {
                    Point inter = intersection(xPrev, yPrev, curX, curY, lowHorizon);
                    double xi = inter.getX();
                    double yi = inter.getY();
                    DrawLine(gc.getCanvas(), xi, yi, curX, curY, color);
                    horizon((int) xi, (int) yi, curX, curY, upHorizon, lowHorizon);
                } else {
                    Point inter = intersection(xPrev, yPrev, curX, curY, upHorizon);
                    double xi = inter.getX();
                    double yi = inter.getY();
                    DrawLine(gc.getCanvas(), xPrev, yPrev, xi, yi, color);
                    horizon(xPrev, yPrev, (int) xi, (int) yi, upHorizon, lowHorizon);
                    inter = intersection(xPrev, yPrev, curX, curY, lowHorizon);
                    xi = inter.getX();
                    yi = inter.getY();
                    DrawLine(gc.getCanvas(), xi, yi, curX, curY, color);
                    horizon((int) xi, (int) yi, curX, curY, upHorizon, lowHorizon);
                }
                prevFlag = currentFlag;
                xPrev = curX;
                yPrev = curY;
            }

            if (xRight != BELOW) {
                horizon(xRight, yRight, xPrev, yPrev, upHorizon, lowHorizon);
            }
            xRight = xPrev;
            yRight = yPrev;
        }
    }

    private static int visible(int x, int y, int[] upHorizon, int[] lowHorizon) {
        if (y < upHorizon[x] && y > lowHorizon[x]) {
            return INVISIBLE;
        }
        if (y >= upHorizon[x]) {
            return ABOVE;
        }
        if (y <= lowHorizon[x]) {
            return BELOW;
        }
        return INVISIBLE;
    }

    public static void horizon(int x1, int y1, int x2, int y2, int[] up, int[] low) {
        if (x1 < 0 || x1 >= up.length || x2 < 0 || x2 >= up.length) {
            return;
        }
        if ((x2 - x1) == 0) {
            up[x2] = Math.max(up[x2], y2);
            low[x2] = Math.min(low[x2], y2);
        } else {
            double tan = (double) (y2 - y1) / (double) (x2 - x1);
            for (int x = x1; x < x2; x++) {
                double y = tan * (x - x1) + y1;
                up[x] = Math.max(up[x], (int) y);
                low[x] = Math.min(low[x], (int) y);
            }
        }
    }

    public static int screenX(int width, double xMax, double xMin, int x) {
        return (int) (width / (xMax - xMin) * (x - xMin));
    }

    public static int screenY(int height, double yMax, double yMin, int y) {
        return (int) (height - height / (yMax - yMin) * (y - yMin));
    }

    private static int sign(double x) {
        if (x > 0) {
            return 1;
        } else if (x == 0) {
            return 0;
        }
        return -1;
    }

    public static Point intersection(int x1, int y1, int x2, int y2, int[] horizon) {
        double xi = x1;
        double yi = y1;
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;
        int deltaH = horizon[x2] - horizon[x1];
        double tan;
        if (deltaX == 0) {
            xi = x2;
            yi = horizon[x2];
            return new Point(xi, yi);
        } else if (y1 == horizon[x1] && y2 == horizon[x2]) {
            xi = x1;
            yi = y1;
            return new Point(xi, yi);
        } else {
            tan = (double) deltaY / (double) deltaX;
            xi = x1 - (int) (((deltaX * (y1 - horizon[x1]))) / (double) (deltaY - deltaH));
            yi = (int) ((xi - x1) * tan + y1);
        }

        return new Point(xi, yi);

    }
}
