/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component.sensors;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ObjectInputStream;
import net.minisumo.world.sensors.Sensor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Sensor.class)
public class InfraredSensor implements Sensor {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private transient double distance;
    public static final String PROP_DISTANCE = "distance";
    private double minDistance;
    public static final String PROP_MINDISTANCE = "minDistance";
    private double maxDistance;
    public static final String PROP_MAXDISTANCE = "maxDistance";
    private Color color;
    public static final String PROP_COLOR = "color";
    private double open;
    public static final String PROP_OPEN = "open";
    private double position;
    public static final String PROP_POSITION = "position";
    private double orientation;
    public static final String PROP_ORIENTATION = "orientation";
    private GeneralPath polygon;

    public InfraredSensor() {
        this(Color.CYAN, 0.1, 0);
    }

    public InfraredSensor(Color color, double position, double orientation) {
        this(0.04, 0.3, color, 0.04, position, orientation);
    }

    public InfraredSensor(double minDistance, double maxDistance, Color color, double open, double position, double orientation) {
        this.distance = maxDistance;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.color = color;
        this.open = open;
        this.position = position;
        this.orientation = orientation;
    }

    @Override
    public GeneralPath polygon(double scale) {
        if (distance > maxDistance || distance < minDistance) {
            distance = maxDistance;
        }
        polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);

        polygon.moveTo(minDistance * scale, -open * scale * minDistance / (2 * maxDistance));
        polygon.lineTo(distance * scale, -open * scale * distance / (2 * maxDistance));
        polygon.lineTo(distance * scale, open * scale * distance / (2 * maxDistance));
        polygon.lineTo(minDistance * scale, open * scale * minDistance / (2 * maxDistance));

        polygon.closePath();
        return polygon;
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
     * Get the value of open
     *
     * @return the value of open
     */
    public double getOpen() {
        return open;
    }

    /**
     * Set the value of open
     *
     * @param open new value of open
     */
    public void setOpen(double open) {
        double oldOpen = this.open;
        this.open = open;
        propertyChangeSupport.firePropertyChange(PROP_OPEN, oldOpen, open);
    }

    /**
     * Get the value of position
     *
     * @return the value of position
     */
    @Override
    public double getPosition() {
        return position;
    }

    /**
     * Set the value of position
     *
     * @param position new value of position
     */
    public void setPosition(double position) {
        double oldPosition = this.position;
        this.position = position;
        propertyChangeSupport.firePropertyChange(PROP_POSITION, oldPosition, position);
    }

    /**
     * Get the value of orientation
     *
     * @return the value of orientation
     */
    @Override
    public double getOrientation() {
        return orientation;
    }

    /**
     * Get the value of distance
     *
     * @return the value of distance
     */
    @Override
    public double getDistance() {
        return distance;
    }

    /**
     * Set the value of distance
     *
     * @param distance new value of distance
     */
    @Override
    public void setDistance(double distance) {
        double oldDistance = this.distance;
        this.distance = distance;
        propertyChangeSupport.firePropertyChange(PROP_DISTANCE, oldDistance, distance);
    }

    /**
     * Get the value of minDistance
     *
     * @return the value of minDistance
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * Set the value of minDistance
     *
     * @param minDistance new value of minDistance
     */
    public void setMinDistance(double minDistance) {
        double oldMinDistance = this.minDistance;
        this.minDistance = minDistance;
        propertyChangeSupport.firePropertyChange(PROP_MINDISTANCE, oldMinDistance, minDistance);
    }

    /**
     * Get the value of maxDistance
     *
     * @return the value of maxDistance
     */
    public double getMaxDistance() {
        return maxDistance;
    }

    /**
     * Set the value of maxDistance
     *
     * @param maxDistance new value of maxDistance
     */
    public void setMaxDistance(double maxDistance) {
        double oldMaxDistance = this.maxDistance;
        this.maxDistance = maxDistance;
        propertyChangeSupport.firePropertyChange(PROP_MAXDISTANCE, oldMaxDistance, maxDistance);
    }

    /**
     * Set the value of orientation
     *
     * @param orientation new value of orientation
     */
    public void setOrientation(double orientation) {
        double oldOrientation = this.orientation;
        this.orientation = orientation;
        propertyChangeSupport.firePropertyChange(PROP_ORIENTATION, oldOrientation, orientation);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public Sensor load(ObjectInputStream objectInputStream) {
        try {
            return (InfraredSensor) objectInputStream.readObject();
        } catch (Exception ex) {
            return null;
        }
    }
}
