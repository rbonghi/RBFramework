/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component.body;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minisumo.util.DataInformation;
import net.minisumo.world.component.Component;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Component.class)
public class Body implements Component, PropertyChangeListener {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Color color;
    public static final String PROP_COLOR = "color";
    private double centerX;
    public static final String PROP_CENTERX = "centerX";
    private double centerY;
    public static final String PROP_CENTERY = "centerY";
    private double phase;
    public static final String PROP_PHASE = "phase";
    private double radius;
    public static final String PROP_RADIUS = "radius";
    private int numberSides;
    public static final String PROP_NUMBERSIDES = "numberSides";
    private GeneralPath polygon;

    public Body() {
        addPropertyChangeListener(this);
        color = Color.YELLOW;
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);
        numberSides = 8;
        phase = Math.PI / 8;
        radius = 0.1;
        polygon(numberSides, centerX, centerY, phase, radius);
    }

    @Override
    public Component load(ObjectInputStream objectInputStream) {
        try {
            return (Body) objectInputStream.readObject();
        } catch (IOException ex) {
            Logger.getLogger(Body.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    @Override
    public boolean isDrawable() {
        return true;
    }

    @Override
    public HashMap<String, DataInformation> getParameters() {
        return null;
    }

    @Override
    public Shape getPolygon() {
        return polygon;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(PROP_COLOR)) {
            if (!evt.getPropertyName().equals(PROP_PHASE)) {
                setPhase(Math.PI / numberSides);
            }
            polygon(numberSides, centerX, centerY, phase, radius);
        }
    }

    private void polygon(int n, double cx, double cy, double phase, double r) {
        double angle = 360.0 / n;
        polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, n);

        polygon.moveTo(cx + r * Math.cos(phase), -cy + r * Math.sin(phase));

        for (int i = 1; i < n; i++) {
            polygon.lineTo(cx + r * Math.cos(Math.toRadians(angle * i) + phase),
                    -cy + r * Math.sin(Math.toRadians(angle * i) + phase));
        }
        polygon.closePath();
    }

    /**
     * Get the value of centerX
     *
     * @return the value of centerX
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Set the value of centerX
     *
     * @param centerX new value of centerX
     */
    public void setCenterX(double centerX) {
        double oldCenterX = this.centerX;
        this.centerX = centerX;
        propertyChangeSupport.firePropertyChange(PROP_CENTERX, oldCenterX, centerX);
    }

    /**
     * Get the value of centerY
     *
     * @return the value of centerY
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Set the value of centerY
     *
     * @param centerY new value of centerY
     */
    public void setCenterY(double centerY) {
        double oldCenterY = this.centerY;
        this.centerY = centerY;
        propertyChangeSupport.firePropertyChange(PROP_CENTERY, oldCenterY, centerY);
    }

    /**
     * Get the value of phase
     *
     * @return the value of phase
     */
    public double getPhase() {
        return phase;
    }

    /**
     * Set the value of phase
     *
     * @param phase new value of phase
     */
    public void setPhase(double phase) {
        double oldPhase = this.phase;
        this.phase = phase;
        propertyChangeSupport.firePropertyChange(PROP_PHASE, oldPhase, phase);
    }

    /**
     * Get the value of radius
     *
     * @return the value of radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Set the value of radius
     *
     * @param radius new value of radius
     */
    public void setRadius(double radius) {
        double oldRadius = this.radius;
        this.radius = radius;
        propertyChangeSupport.firePropertyChange(PROP_RADIUS, oldRadius, radius);
    }

    /**
     * Get the value of numberSides
     *
     * @return the value of numberSides
     */
    public int getNumberSides() {
        return numberSides;
    }

    /**
     * Set the value of numberSides
     *
     * @param numberSides new value of numberSides
     */
    public void setNumberSides(int numberSides) {
        int oldNumberSides = this.numberSides;
        this.numberSides = numberSides;
        propertyChangeSupport.firePropertyChange(PROP_NUMBERSIDES, oldNumberSides, numberSides);
    }

    /**
     * Get the value of color
     *
     * @return the value of color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Set the value of color
     *
     * @param color new value of color
     */
    @Override
    public void setColor(Color color) {
        Color oldColor = this.color;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);
        propertyChangeSupport.firePropertyChange(PROP_COLOR, oldColor, color);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String getName() {
        return "Body";
    }
}
