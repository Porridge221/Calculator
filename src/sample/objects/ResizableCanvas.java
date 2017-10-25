package sample.objects;

import javafx.scene.canvas.Canvas;


import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.controllers.GraphController;

import java.text.DecimalFormat;

/**
 * Created by ivano on 15.07.2017.
 */
public class ResizableCanvas extends Canvas {

    private double ax = - 2.0 * Math.PI, bx = 2.0 * Math.PI, ay = - 2.0, by = + 2.0;

    private double bh = 25, bw = 50;

    public ResizableCanvas() {
        System.out.println("Constructor ResizeableCanvas");
        widthProperty().addListener(evt->draw());
        heightProperty().addListener(evt->draw());
    }

    public void draw() {
        double width = getLocalWidth();
        double height = getLocalHeight();
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setStroke(Color.RED);
        double sx = (bx - ax) / width, sy = (by - ay) / height, center = by / sy;	//Вычисляем масштабирующие коэффициенты;
        GraphController.parser.replaceVariable(GraphController.var, ax);
        Double y1 = center - GraphController.parser.Execute() / sy;
        for (int i = (int)bw; i < width; ++ i) {
            GraphController.parser.replaceVariable(GraphController.var, ax + (i + 1) * sx);
            Double y2 = center - GraphController.parser.Execute() / sy;
            gc.strokeLine(i, y1.intValue(), i + 1, y2.intValue());
            y1 = y2;
        }

        Point2D pointCenter = localToGlobal(new Point2D(0, 0));
        gc.strokeOval(pointCenter.getX() - 3.5, pointCenter.getY() - 3.5, 7, 7);

       /* gc.setStroke(Color.BLUE);
        for (int i = 0; i < GraphController.graphic.X.length; ++i) {
            gc.strokeOval((int)(width - bx / sx + GraphController.graphic.X[i] / sx - 3.5), (int)(center - GraphController.graphic.Y[i] / sy) - 3.5, 7, 7);
        }*/


        drawAxies(gc, width, height);
    }

    private void drawAxies(GraphicsContext gc, double width, double height) {
        gc.setStroke(Color.WHITE);
        gc.setFont(Font.font("Arial", 13));
        gc.setFill(Color.WHITE);
        gc.setLineWidth(0.0);

        gc.strokeLine(0, 0, getWidth(), 0);
        gc.strokeLine(getWidth(), 0, getWidth(), getHeight());
        gc.strokeLine(getWidth(), getHeight(), 0, getHeight());
        gc.strokeLine(0, getHeight(), 0, 0);

        Point2D globalCenter = localToGlobal(new Point2D(0, 0));
        gc.strokeLine(bw, globalCenter.getY(), width, globalCenter.getY());
        gc.strokeLine(globalCenter.getX(), bh, globalCenter.getX(), height);

        gc.clearRect(0, 0, getWidth(), bh);
        gc.clearRect(getWidth() - bw, bh, getWidth(), getHeight());
        gc.clearRect(0, 0, bw, getHeight());
        gc.clearRect(bw, getHeight() - bh, getWidth(), getHeight());

        gc.strokeLine(bw, bh, width, bh);
        gc.strokeLine(width, bh, width, height);
        gc.strokeLine(width, height, bw, height);
        gc.strokeLine(bw, height, bw, bh);
        double stepX = (getWidth() - 2 * bw) / 10;
        double stepY = (getHeight() - 2 * bh) / 10;

        DecimalFormat dv = setAxesValuesFormat(width, height, stepX, stepY);
        for (int i = 0; i < 11; ++i) {
            gc.strokeLine(bw + i * stepX, bh, bw + i * stepX, bh + 4);
            gc.strokeLine(bw + i * stepX, height - 4, bw + i * stepX, height);
            gc.strokeLine(bw, bh + i * stepY, bw + 4, bh + i * stepY);
            gc.strokeLine(width - 4, bh + i * stepY, width, bh + i * stepY);

            Point2D point = globalToLocal(new Point2D(bw + (i * stepX), height - (i * stepY)));
            gc.fillText(dv.format(point.getX()), bw - 15 + (i * stepX), height + 16);
            gc.fillText(dv.format(point.getY()), bw - 50, height + 5 - (i * stepY));
        }

        gc.fillText("x, см.", width + 10, height);
        gc.fillText("y, см.", bw, bh - 10);
    }

    private DecimalFormat setAxesValuesFormat(double width, double height, double stepX, double stepY) {
        DecimalFormat dv = new DecimalFormat("#.###");
        Point2D prevPoint = globalToLocal(new Point2D(bw, height));
        isPlusZoom = true;
        isMinusZoom = true;
        for (int i = 0; i < 11; ++i) {
            Point2D point = globalToLocal(new Point2D(bw + (i * stepX), height - (i * stepY)));
            if (Math.abs(point.getX()) > maxZoom || Math.abs(point.getY()) > maxZoom)
                isMinusZoom = false;
            if (Math.abs(point.getX()) > 1e+10 || Math.abs(point.getY()) > 1e+10) {
                dv = new DecimalFormat("#.#E0");
                break;
            }
            if (i != 0 && (dv.format(point.getX()).length() > 7 || dv.format(point.getY()).length() > 7)) {
                dv = new DecimalFormat("#.##E0");
                break;
            }
            if (i != 0 && ((Math.abs(point.getX() - prevPoint.getX())) < minZoom || (Math.abs(point.getY() - prevPoint.getY())) < minZoom)) {
                isPlusZoom = false;
                dv = new DecimalFormat("#.#E0");
                break;
            }
            prevPoint = point;
        }

        return dv;
    }


    public void setLocalBorders(Point2D start, Point2D end) {
        double diffX = (start.getX() - end.getX()) / (getLocalWidth() / (bx - ax));
        double diffY = (end.getY() - start.getY()) / (getLocalHeight() / (by - ay));

        ax += diffX; bx += diffX;
        ay += diffY; by += diffY;
        draw();
    }

    public void setLocalBorders(Point2D point, double zoom) {
        double Lx = bx - ax;
        double tx = (point.getX() - ax) / Lx;
        ax = point.getX() - tx * Lx * zoom;
        bx = ax + zoom * Lx;

        double Ly = by - ay;
        double ty = (point.getY() - ay) / Ly;
        ay = point.getY() - ty * Ly * zoom;
        by = ay + zoom * Ly;


        /*ax = ax + ax * zoom;
        bx = bx + bx * zoom;
        ay = ay + ay * zoom;
        by = by + by * zoom;*/
        draw();
    }

    private final double maxZoom = 1e+100, minZoom = 1e-3;
    private boolean isPlusZoom = true, isMinusZoom = true;

    public boolean isZooming(double zoom) {
        if (zoom < 1)
            return isPlusZoom;
        else
            return isMinusZoom;

    }


    public Point2D globalToLocal(Point2D point) {
        double sx = (bx - ax) / getLocalWidth(), sy = (by - ay) / getLocalHeight(), centerY = by / sy, centerX = getLocalWidth() - bx / sx;
        return new Point2D(ax + point.getX() * sx, by - point.getY() * sy);
    }

    public Point2D localToGlobal(Point2D point) {
        double sx = (bx - ax) / getLocalWidth(), sy = (by - ay) / getLocalHeight(), centerY = by / sy, centerX = getLocalWidth() - bx / sx;
        return new Point2D(centerX + point.getX() / sx, centerY - point.getY() / sy);
    }

    private double getLocalWidth() {
        return getWidth() - bw;
    }

    private double getLocalHeight() {
        return getHeight() - bh;
    }



    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return 580.0;
    }

    @Override
    public double prefHeight(double width) {
        return 290.0;
    }

}
