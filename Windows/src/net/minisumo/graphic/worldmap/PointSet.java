/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.worldmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Raffaello
 */
public class PointSet {

    private Graphics2D g2;
    private double x, y;
    private Color color;

    public PointSet(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    void draw(Graphics g, double originXrel, double originYrel, double scale) {
        g2 = (Graphics2D) g;
        Ellipse2D point = new Ellipse2D.Double(-(originXrel - x) * scale - 5, (originYrel - y) * scale - 5, 10, 10);
        // Draw Circle
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
        g2.fill(point);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(point);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
