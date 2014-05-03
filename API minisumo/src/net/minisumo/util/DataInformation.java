/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.util;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author Raffaello
 */
public class DataInformation implements Serializable {

    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public static final Color CONTROL = Color.YELLOW, MEASURE = Color.GREEN, RIFER = Color.RED;
    private String name;
    public static final String PROP_NAME = "name";
    protected String dimension;
    public static final String PROP_DIMENSION = "dimension";
    private double maxLimit;
    public static final String PROP_MAXLIMIT = "maxLimit";
    private double minLimit;
    public static final String PROP_MINLIMIT = "minLimit";
    private Color color;
    private boolean limited;
    private transient FlushedMap<Double> data;
    public static final String PROP_DATA = "data";
    private transient double lastData;
    private double parameter;
    private boolean variable;
    private long totalTime;

    public DataInformation() {
        this("Data", Color.BLACK, "", true);
    }

    public DataInformation(String name, Color color, String dimension, boolean variable) {
        this(name, color, dimension, 10000, variable);
    }

    /**
     * 
     * @param name Nome segnale
     * @param color colore funzione da disegnare
     * @param timeUpdate dimensione array valori scope
     * @param dimension Grandezza fisica misurata
     */
    public DataInformation(String name, Color color, String dimension, long totalTime, boolean variable) {
        this.name = name;
        this.color = color;
        this.dimension = dimension;
        this.totalTime = totalTime;
        this.variable = variable;
        if (variable) {
            this.data = new FlushedMap<Double>(totalTime);
        }
    }

    public FlushedMap<Double> getData() {
        return data;
    }

    public void setData(double data) {
        if (variable) {
            double tempData = this.lastData;
            this.lastData = data;
            if (this.data == null) {
                this.data = new FlushedMap<Double>(totalTime);
            }
            this.data.putData(data);
            propertyChangeSupport.firePropertyChange(PROP_DATA, tempData, data);
        } else {
           parameter = data;
           propertyChangeSupport.firePropertyChange(PROP_DATA, null, data);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
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
     * Get the value of dimension
     *
     * @return the value of dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * Set the value of dimension
     *
     * @param dimension new value of dimension
     */
    public void setDimension(String dimension) {
        String oldDimension = this.dimension;
        this.dimension = dimension;
        propertyChangeSupport.firePropertyChange(PROP_DIMENSION, oldDimension, dimension);
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        data.setStoric(totalTime);
    }

    public long getTotalTime() {
        return totalTime;
    }

    public double getLastData() {
        if (variable) {
            return lastData;
        } else {
            return parameter;
        }
    }

    public boolean isLimited() {
        return limited;
    }

    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    /**
     * Get the value of maxLimit
     *
     * @return the value of maxLimit
     */
    public double getMaxLimit() {
        return maxLimit;
    }

    /**
     * Set the value of maxLimit
     *
     * @param maxLimit new value of maxLimit
     */
    public void setMaxLimit(double maxLimit) {
        double oldMaxLimit = this.maxLimit;
        this.maxLimit = maxLimit;
        propertyChangeSupport.firePropertyChange(PROP_MAXLIMIT, oldMaxLimit, maxLimit);
    }

    /**
     * Get the value of minLimit
     *
     * @return the value of minLimit
     */
    public double getMinLimit() {
        return minLimit;
    }

    /**
     * Set the value of minLimit
     *
     * @param minLimit new value of minLimit
     */
    public void setMinLimit(double minLimit) {
        double oldMinLimit = this.minLimit;
        this.minLimit = minLimit;
        propertyChangeSupport.firePropertyChange(PROP_MINLIMIT, oldMinLimit, minLimit);
    }

    public boolean isVariable() {
        return variable;
    }

    public void setVariable(boolean variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return name + ": " + String.format("%.4f", getLastData()) + " " + dimension;
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        } else {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }
}
