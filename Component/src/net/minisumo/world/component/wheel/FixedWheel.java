/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component.wheel;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import net.minisumo.util.DataInformation;
import net.minisumo.world.component.Component;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Component.class)
public class FixedWheel implements Component, PropertyChangeListener {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String name = "Wheel";
    public static final String PROP_NAME = "name";
    private Color color;
    public static final String PROP_COLOR = "color";
    private double thicknessWheel = 0.01;
    public static final String PROP_THICKNESSWHEEL = "thicknessWheel";
    private double radius = 0.03;
    public static final String PROP_RADIUS = "radius";
    private double centerX;
    public static final String PROP_CENTERX = "centerX";
    private double centerY;
    public static final String PROP_CENTERY = "centerY";
    private Rectangle2D.Double polygon;
    private DataInformation radiusData;
    private HashMap<String, DataInformation> map;

    @Override
    public Component load(ObjectInputStream objectInputStream) {
        try {
            return (Component) objectInputStream.readObject();
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public enum Variables {

        radius
    };

    public FixedWheel() {
        map = new HashMap<String, DataInformation>();
        addPropertyChangeListener(this);
        color = Color.BLACK;
        radiusData = new DataInformation(Variables.radius.name(), color, "[m]", false);
        map.put(Variables.radius.name(), radiusData);
        polygon = new Rectangle2D.Double(centerX - radius, -centerY - thicknessWheel / 2, radius * 2, thicknessWheel);
    }

    @Override
    public boolean isDrawable() {
        return true;
    }

    @Override
    public HashMap<String, DataInformation> getParameters() {
        return map;
    }

    @Override
    public Shape getPolygon() {
        return polygon;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(PROP_COLOR) && !evt.getPropertyName().equals(PROP_NAME)) {
            polygon = new Rectangle2D.Double(centerX - radius, -centerY - thicknessWheel / 2, radius * 2, thicknessWheel);
        }
        if (evt.getSource() == radiusData) {
            if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
                radius = (Double) evt.getNewValue();
            }
        }
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange(PROP_NAME, oldName, name);
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
        this.radius = radius;
        radiusData.setData(radius);
    }

    /**
     * Get the value of thicknessWheel
     *
     * @return the value of thicknessWheel
     */
    public double getThicknessWheel() {
        return thicknessWheel;
    }

    /**
     * Set the value of thicknessWheel
     *
     * @param thicknessWheel new value of thicknessWheel
     */
    public void setThicknessWheel(double thicknessWheel) {
        double oldThicknessWheel = this.thicknessWheel;
        this.thicknessWheel = thicknessWheel;
        propertyChangeSupport.firePropertyChange(PROP_THICKNESSWHEEL, oldThicknessWheel, thicknessWheel);
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
        this.color = color;
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
}
