/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.scope;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import net.minisumo.graphic.AbstractXYScope;
import net.minisumo.util.DataInformation;
import net.minisumo.util.FlushedMap;

/**
 *
 * @author Raffaello
 */
public class XYScope extends AbstractXYScope implements MouseWheelListener {

    private final int RULE = 40;
    protected Graphics2D g2;
    protected int width, height, lineAx;
    protected double scaleH, scaleW;
    protected double originX, originY;
    protected double originXrel, originYrel;
    private double errX, errY;
    protected double dimensionMinX = -0.2, dimensionMaxX = 0.2;
    protected double dimensionMaxY = 0.2, dimensionMinY = -0.2;
    protected double accuracyX = 0.1, accuracyY = 0.1;
    protected boolean rule = true, showLine = true;
    protected int notch = 2;

    public XYScope() {
        addMouseWheelListener(this);
    }

    @Override
    protected void configRenderSize() {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        RenderingHints rh2 = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh2);
        Dimension size = getSize();
        width = size.width;
        height = size.height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        //Configura il rendering e la dimensione
        configRenderSize();
        //Disegna la griglia di sfondo
        background();
    }

    protected double[] drawLine(DataInformation xData, DataInformation yData) {
        g2.setStroke(new BasicStroke(1));
//        Collection<Double> xvals = Collections.unmodifiableMap(xData.getData().getMap()).values();
//        Collection<Double> yvals = Collections.unmodifiableMap(yData.getData().getMap()).values();
        List<Double> xvals = new ArrayList<Double>(xData.getData().getMap().values());
        List<Double> yvals = new ArrayList<Double>(yData.getData().getMap().values());
        double dimMinX = 0, dimMaxX = 0, dimMinY = 0, dimMaxY = 0;
//        Iterator<Double> listIteratorx = xvals.iterator();
//        Iterator<Double> listIteratory = yvals.iterator();
        boolean firstData = true;
        double firstx = 0, firsty = 0;
        for (int i = 0; i < xvals.size() - 1; i++) {
            double secondx = xvals.get(i);
            double secondy = yvals.get(i);
            if (i == 0) {
                firstx = secondx;
                firsty = secondy;
                firstData = false;
                dimMinX = firstx;
                dimMaxX = firstx;
                dimMinY = firsty;
                dimMaxY = firsty;
            } else {
                dimMinX = Math.min(dimMinX, secondx);
                dimMinY = Math.min(dimMinY, secondy);
                dimMaxX = Math.max(dimMaxX, secondx);
                dimMaxY = Math.max(dimMaxY, secondy);
            }
            g2.draw(new Line2D.Double(-(originXrel - firstx) * scaleH, (originYrel - firsty) * scaleW,
                    -(originXrel - secondx) * scaleH, (originYrel - secondy) * scaleW));
            firstx = secondx;
            firsty = secondy;
        }
//        while (listIteratorx.hasNext()) {
//            double secondx = listIteratorx.next();
//            double secondy = listIteratory.next();
//            if (firstData) {
//                firstx = secondx;
//                firsty = secondy;
//                firstData = false;
//                dimMinX = firstx;
//                dimMaxX = firstx;
//                dimMinY = firsty;
//                dimMaxY = firsty;
//            } else {
//                dimMinX = Math.min(dimMinX, secondx);
//                dimMinY = Math.min(dimMinY, secondy);
//                dimMaxX = Math.max(dimMaxX, secondx);
//                dimMaxY = Math.max(dimMaxY, secondy);
//            }
//            g2.draw(new Line2D.Double(-(originXrel - firstx) * scaleH, (originYrel - firsty) * scaleW,
//                    -(originXrel - secondx) * scaleH, (originYrel - secondy) * scaleW));
//            firstx = secondx;
//            firsty = secondy;
//        }
        return new double[]{dimMinX, dimMaxX, dimMinY, dimMaxY};
    }

    protected void getLine(double positionX, double positionY, Color color) {
        double posX = -(originXrel - positionX) * scaleH;
        double posY = (originYrel - positionY) * scaleW;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        //Linea su Y
        g2.draw(new Line2D.Double(0, posY, posX + lineAx, posY));
        //Linea su X
        g2.draw(new Line2D.Double(posX, posY - lineAx, posX, height));
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawString(String.format("%.2f", positionX), (int) (posX + 3), height - RULE / 2);
        g2.drawString(String.format("%.2f", positionY), RULE / 2, (int) (posY));
    }

    @Override
    protected void background() {
        scaleW = height / (dimensionMaxY - dimensionMinY);
        scaleH = width / (dimensionMaxX - dimensionMinX);
        if (width > height) {
            scaleH = height / (dimensionMaxX - dimensionMinX);
            lineAx = height / 10;
        } else {
            scaleW = width / (dimensionMaxY - dimensionMinY);
            lineAx = width / 10;
        }
        //Disegno griglia parallela ad X
        getGridX();
        //Disegno griglia parallela ad Y
        getGridY();
        //origin
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(6));
        if (originYrel * scaleW < height && originYrel * scaleW > 0 && originXrel * scaleH > -width && originXrel * scaleH < 0) {
            Point2D point = new Point2D.Double(-originXrel * scaleH, originYrel * scaleW);
            g2.draw(new Line2D.Double(point, point));
        }
        //Sfondo righello
        if (rule) {
            g2.setStroke(new BasicStroke(2));
            //Asse X
            Rectangle2D rectX = new Rectangle2D.Double(0, height - RULE, width, RULE);
            g2.setColor(new Color(0, 0, 255, 100));
            g2.draw(rectX);
            g2.setColor(new Color(0, 0, 255, 30));
            g2.fill(rectX);
            //Asse Y
            Rectangle2D rectY = new Rectangle2D.Double(0, 0, RULE, height);
            g2.setColor(new Color(0, 0, 255, 100));
            g2.draw(rectY);
            g2.setColor(new Color(0, 0, 255, 30));
            g2.fill(rectY);
        }
    }

    @Override
    protected void getGridX() {
        errY = height / scaleH - (dimensionMaxY - dimensionMinY);
        originYrel = originY + dimensionMaxY + errY / 2;
        //Assi paralleli ad X
        double errorX = Math.floor(-originYrel) + originYrel;
        for (double i = 0; (i + errorX) < height / scaleW; i += accuracyX) {
            if ((i + errorX) > 0) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(new Line2D.Double(0, (i + errorX) * scaleW, width, (i + errorX) * scaleW));
                if (rule) {
                    g2.drawString(String.format("%.1f", originYrel - (i + errorX)), RULE / 2, (int) ((i + errorX) * scaleW));
                }
            }
        }
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        //Asse X
        if (originYrel * scaleW < height && originYrel * scaleW > 0) {
            g2.draw(new Line2D.Double(0, originYrel * scaleW, width, originYrel * scaleW));
            if (rule) {
                g2.drawString("X", width - 10, (int) (originYrel * scaleW));
            }
        }
    }

    @Override
    protected void getGridY() {
        errX = width / scaleW - (dimensionMaxX - dimensionMinX);
        originXrel = originX + dimensionMinX - errX / 2;
        //Assi paralleli ad Y
        double errorY = Math.floor(originXrel) - originXrel;
        for (double i = 0; (i + errorY) < width / scaleH; i = i + accuracyY) {
            if ((i + errorY) > 0) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(new Line2D.Double((i + errorY) * scaleH, 0, (i + errorY) * scaleH, height));
                if (rule) {
                    g2.drawString(String.format("%.1f", originXrel + (i + errorY)), (int) ((i + errorY) * scaleH), height - RULE / 2);
                }
            }
        }
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        //Asse Y
        if (originXrel * scaleH > -width && originXrel * scaleH < 0) {
            g2.draw(new Line2D.Double(-originXrel * scaleH, 0, -originXrel * scaleH, height));
            if (rule) {
                g2.drawString("Y", (int) (-originXrel * scaleH), 10);
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
//            String message = "Mouse wheel moved UP " + -notches + " notch(es)";
            notch++;
        } else {
//            String message = "Mouse wheel moved DOWN " + notches + " notch(es)";
            if (notch > 2) {
                notch--;
            }
        }
    }
}
