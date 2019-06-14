package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static sample.LineDrawer.DrawLine;

public class FloatHorizon {
    private static final int INVISIBLE = 0;
    private static final int ABOVE = 1;
    private static final int BELOW = -1;
    private double xMax = 2;
    private double xMin = -2;
    private double yMax = 5;
    private double yMin = -5;
    private int width = 600;
    private int height = 600;
    private double absX = 0;
    private double absY = 0;
    private double absZ = 0;
    private double xStep = 1;
    private double zStep = 1;
    private double zMin = 0;
    private double zMax = 1;

    public FloatHorizon(double xMax, double xMin, double yMax, double yMin, int width, int height) {
        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
        this.width = width;
        this.height = height;
    }

    public void floatHorizon(GraphicsContext gc, int func,
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
        setxMax(xMax);
        setxMin(xMin);
        setWidth(width);
        setHeight(height);
        setAbsX(absX);
        setAbsY(absY);
        setAbsZ(absZ);
        setxStep(xStep);
        setzStep(zStep);
        setzMax(zMax);
        setzMin(zMin);
        Point.setHeight(height);
        Point.setWidth(width);
        Point.setxMax(xMax);
        Point.setxMin(xMin);
        Point.setyMax(yMax);
        Point.setyMin(yMin);
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
            if (xLeft != BELOW) {
                DrawLine(gc.getCanvas(), xLeft, yLeft, xPrev, yPrev, color);
                horizon(xLeft, yLeft, xPrev, yPrev, upHorizon, lowHorizon);
            }
            xLeft = xPrev;
            yLeft = yPrev;


            int prevFlag = visible(xPrev, yPrev, upHorizon, lowHorizon);
            double x = xMin;
            double y = yPrev;
            for (; x < xMax; x += xStep) {
                double curYTmp = Functions.function(func, x, z);
                Point p = new Point(x, curYTmp, z);
                p.transform(absX, absY, absZ);
                int curX = (int) p.getX();
                int curY = (int) p.getY();
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
                DrawLine(gc.getCanvas(), xRight, yRight, xPrev, yPrev, color);
                horizon(xRight, yRight, xPrev, yPrev, upHorizon, lowHorizon);
            }
            xRight = xPrev;
            yRight = yPrev;
        }
    }

    private int visible(int x, int y, int[] upHorizon, int[] lowHorizon) {
        if (x < 0 || x >= upHorizon.length) {
            return INVISIBLE;
        }
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

    private void horizon(int x1, int y1, int x2, int y2, int[] up, int[] low) {
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

    private int sign(double x) {
        if (x > 0) {
            return 1;
        } else if (x == 0) {
            return 0;
        }
        return -1;
    }

    private Point intersection(int x1, int y1, int x2, int y2, int[] horizon) {
        if (x1 < 0 || x1 >= horizon.length || x2 < 0 || x2 >= horizon.length) {
            return new Point(x1, y1);
        }
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
            return new Point(xi, yi);
        } else {
            tan = (double) deltaY / (double) deltaX;
            xi = x1 - (int) (((deltaX * (y1 - horizon[x1]))) / (double) (deltaY - deltaH));
            yi = (int) ((xi - x1) * tan + y1);
        }

        return new Point(xi, yi);

    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getAbsX() {
        return absX;
    }

    public void setAbsX(double absX) {
        this.absX = absX;
    }

    public double getAbsY() {
        return absY;
    }

    public void setAbsY(double absY) {
        this.absY = absY;
    }

    public double getAbsZ() {
        return absZ;
    }

    public void setAbsZ(double absZ) {
        this.absZ = absZ;
    }

    public double getxStep() {
        return xStep;
    }

    public void setxStep(double xStep) {
        this.xStep = xStep;
    }

    public double getzStep() {
        return zStep;
    }

    public void setzStep(double zStep) {
        this.zStep = zStep;
    }

    public double getzMin() {
        return zMin;
    }

    public void setzMin(double zMin) {
        this.zMin = zMin;
    }

    public double getzMax() {
        return zMax;
    }

    public void setzMax(double zMax) {
        this.zMax = zMax;
    }
}
