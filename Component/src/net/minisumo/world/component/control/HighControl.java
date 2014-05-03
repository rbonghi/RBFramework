/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component.control;

import java.awt.Color;
import java.awt.Shape;
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
public class HighControl implements Component {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private HashMap<String, DataInformation> parameters;
    private double[] kControl;
    public static final String PROP_KCONTROL = "kControl";
    private double minRadius;
    public static final String PROP_MINRADIUS = "minRadius";

    @Override
    public Component load(ObjectInputStream objectInputStream) {
        try {
            return (HighControl) objectInputStream.readObject();
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public enum Variables {

        Gain1, Gain2, Gain3, riferX, riferY, riferTh, Precision
    };

    public HighControl() {
        parameters = new HashMap<String, DataInformation>();
        kControl = new double[2];
        parameters.put(Variables.Precision.name(), new DataInformation(Variables.Precision.name(), Color.BLACK, "", false));
        parameters.put(Variables.Gain1.name(), new DataInformation(Variables.Gain1.name(), Color.BLACK, "", false));
        parameters.put(Variables.Gain2.name(), new DataInformation(Variables.Gain2.name(), Color.BLACK, "", false));
//        parameters.put(Variables.Gain3, new DataInformation(Variables.Gain3.name(), Color.BLACK, "", false));
        parameters.put(Variables.riferX.name(), new DataInformation(Variables.riferX.name(), DataInformation.RIFER, "[m]", true));
        parameters.put(Variables.riferY.name(), new DataInformation(Variables.riferY.name(), DataInformation.RIFER, "[m]", true));
//        parameters.put(Variables.riferTh, new DataInformation(Variables.riferTh.name(), DataInformation.RIFER, "[m]", true));
    }
    
    private void updateParameters() {
        parameters.get(Variables.Gain1.name()).setData(kControl[0]);
        parameters.get(Variables.Gain2.name()).setData(kControl[1]);
    }

    /**
     * Get the value of minRadius
     *
     * @return the value of minRadius
     */
    public double getMinRadius() {
        return minRadius;
    }

    /**
     * Set the value of minRadius
     *
     * @param minRadius new value of minRadius
     */
    public void setMinRadius(double minRadius) {
        double oldMinRadius = this.minRadius;
        this.minRadius = minRadius;
        parameters.get(Variables.Precision.name()).setData(minRadius);
        propertyChangeSupport.firePropertyChange(PROP_MINRADIUS, oldMinRadius, minRadius);
    }

    /**
     * Get the value of kControl
     *
     * @return the value of kControl
     */
    public double[] getKControl() {
        return kControl;
    }

    /**
     * Set the value of kControl
     *
     * @param kControl new value of kControl
     */
    public void setKControl(double[] kControl) {
        double[] oldKControl = this.kControl;
        this.kControl = kControl;
        propertyChangeSupport.firePropertyChange(PROP_KCONTROL, oldKControl, kControl);
        updateParameters();
    }

    /**
     * Get the value of kControl at specified index
     *
     * @param index
     * @return the value of kControl at specified index
     */
    public double getKControl(int index) {
        return this.kControl[index];
    }

    /**
     * Set the value of kControl at specified index.
     *
     * @param index
     * @param newKControl new value of kControl at specified index
     */
    public void setKControl(int index, double newKControl) {
        double oldKControl = this.kControl[index];
        this.kControl[index] = newKControl;
        propertyChangeSupport.fireIndexedPropertyChange(PROP_KCONTROL, index, oldKControl, newKControl);
        updateParameters();
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color color) {
    }

    @Override
    public String getName() {
        return "HighControl";
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
