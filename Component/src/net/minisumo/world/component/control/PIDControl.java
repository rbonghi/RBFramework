/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component.control;

import java.awt.Color;
import java.awt.Shape;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import net.minisumo.util.DataInformation;
import net.minisumo.world.component.Component;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Component.class)
public class PIDControl implements Component, PropertyChangeListener, VetoableChangeListener {

    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private transient VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);
    private HashMap<String, DataInformation> parameters;
    private double[] kPID;
    public static final String PROP_KPID = "kPID";
    private double maxValue;
    public static final String PROP_MAXVALUE = "maxValue";
    private String name = "PID Control";
    public static final String PROP_NAME = "name";

    @Override
    public Component load(ObjectInputStream objectInputStream) {
        try {
            PIDControl pidControl = (PIDControl) objectInputStream.readObject();
            pidControl.propertyChangeSupport = new PropertyChangeSupport(this);
            return pidControl;
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public enum Variables {

        Proportional, Integrative, Derivative, Input, Output, Measure
    };

    public PIDControl() {
        kPID = new double[3];
        maxValue = 1;
        parameters = new HashMap<String, DataInformation>();
        parameters.put(Variables.Proportional.name(), new DataInformation(Variables.Proportional.name(), Color.BLACK, "", false));
        parameters.put(Variables.Integrative.name(), new DataInformation(Variables.Integrative.name(), Color.BLACK, "", false));
        parameters.put(Variables.Derivative.name(), new DataInformation(Variables.Derivative.name(), Color.BLACK, "", false));
        parameters.put(Variables.Input.name(), new DataInformation(Variables.Input.name(), DataInformation.RIFER, "[]", true));
        parameters.put(Variables.Output.name(), new DataInformation(Variables.Output.name(), DataInformation.CONTROL, "[%]", true));
        parameters.put(Variables.Measure.name(), new DataInformation(Variables.Measure.name(), DataInformation.MEASURE, "[]", true));
    }

    private void updateParameters() {
        parameters.get(Variables.Proportional.name()).setData(kPID[0]);
        parameters.get(Variables.Integrative.name()).setData(kPID[1]);
        parameters.get(Variables.Derivative.name()).setData(kPID[2]);
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    @Override
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
     * Get the value of maxValue
     *
     * @return the value of maxValue
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Set the value of maxValue
     *
     * @param maxValue new value of maxValue
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Get the value of kPID
     *
     * @return the value of kPID
     */
    public double[] getKPID() {
        return kPID;
    }

    /**
     * Set the value of kPID
     *
     * @param kPID new value of kPID
     * @throws java.beans.PropertyVetoException
     */
    public void setKPID(double[] kPID) throws java.beans.PropertyVetoException {
        double[] oldKPID = this.kPID;
        vetoableChangeSupport.fireVetoableChange(PROP_KPID, oldKPID, kPID);
        this.kPID = kPID;
        updateParameters();
        propertyChangeSupport.firePropertyChange(PROP_KPID, oldKPID, kPID);
    }

    /**
     * Get the value of kPID at specified index
     *
     * @param index
     * @return the value of kPID at specified index
     */
    public double getKPID(int index) {
        return this.kPID[index];
    }

    /**
     * Set the value of kPID at specified index.
     *
     * @param index
     * @param newKPID new value of kPID at specified index
     * @throws java.beans.PropertyVetoException
     */
    public void setKPID(int index, double newKPID) throws java.beans.PropertyVetoException {
        double oldKPID = this.kPID[index];
        vetoableChangeSupport.fireVetoableChange(PROP_KPID, oldKPID, newKPID);
        this.kPID[index] = newKPID;
        propertyChangeSupport.fireIndexedPropertyChange(PROP_KPID, index, oldKPID, newKPID);
    }

    /**
     * Get the value of color
     *
     * @return the value of color
     */
    @Override
    public Color getColor() {
        return null;
    }

    /**
     * Set the value of color
     *
     * @param color new value of color
     */
    @Override
    public void setColor(Color color) {
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

    /**
     * Add VetoableChangeListener.
     *
     * @param listener
     */
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    /**
     * Remove VetoableChangeListener.
     *
     * @param listener
     */
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    @Override
    public boolean isDrawable() {
        return false;
    }

    @Override
    public Shape getPolygon() {
        return null;
    }

    @Override
    public HashMap<String, DataInformation> getParameters() {
        return parameters;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == this) {
            if (evt.getPropertyName().equals(PROP_KPID)) {
                if (evt instanceof IndexedPropertyChangeEvent) {
                    int index = ((IndexedPropertyChangeEvent) evt).getIndex();
                    kPID[index] = (Double) evt.getNewValue();
                    switch (index) {
                        case 0:
                            parameters.get(Variables.Proportional.name()).setData(kPID[0]);
                            break;
                        case 1:
                            parameters.get(Variables.Integrative.name()).setData(kPID[1]);
                            break;
                        case 2:
                            parameters.get(Variables.Derivative.name()).setData(kPID[2]);
                            break;
                    }


                } else {
                    kPID = (double[]) evt.getNewValue();
                    parameters.get(Variables.Proportional.name()).setData(kPID[0]);
                    parameters.get(Variables.Integrative.name()).setData(kPID[1]);
                    parameters.get(Variables.Derivative.name()).setData(kPID[2]);
                }
            }
        }
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        Object value = evt.getNewValue();
        if (value.getClass().isArray()) {
            for (int i = 0; i < 3; i++) {
                if (Math.abs(Array.getDouble(value, i)) >= maxValue) {
                    throw new PropertyVetoException("Bad Value! Value: -" + maxValue + " < " + Array.getDouble(value, i) + " < " + maxValue, evt);
                }
            }
        } else {
            if (Math.abs((Double) value) >= maxValue) {
                throw new PropertyVetoException("Bad Value! Value: -" + maxValue + " < " + value + " < " + maxValue, evt);
            }
        }
    }
}
