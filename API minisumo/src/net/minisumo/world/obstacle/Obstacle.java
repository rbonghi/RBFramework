/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.obstacle;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import net.minisumo.world.JElement;

/**
 *
 * @author Raffaello
 */
public class Obstacle implements JElement {

    Rectangle2D.Double polygon;
    private final Date date = new Date();

    public Obstacle() {
        polygon = new Rectangle2D.Double(0.5, -0.5, 0.5, 0.5);
    }

    @Override
    public String getName() {
        return "Obstacle";
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public Shape getPolygon() {
        return polygon;
    }

    @Override
    public Date getDate() {
        return date;
    }
}
