/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component.drive;

import java.awt.Color;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
public class Drive implements Component, PropertyChangeListener {

    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private HashMap<String, DataInformation> parameters;
    private transient double velocity;
    public static final String PROP_VELOCITY = "velocity";
    private transient double steering;
    public static final String PROP_STEERING = "steering";
    private DataInformation velocityRifer, steeringRifer, velocityData, steeringData;

    @Override
    public Component load(ObjectInputStream objectInputStream) {
        try {
            Drive drive = (Drive) objectInputStream.readObject();
            drive.propertyChangeSupport = new PropertyChangeSupport(this);
            return drive;
        } catch (Exception ex) {
            return null;
        }
    }

    public enum Variables {

        VelocityRifer, Velocity, SteeringRifer, Steering
    };

    public Drive() {
        parameters = new HashMap<String, DataInformation>();
        velocityRifer = new DataInformation(Variables.VelocityRifer.name(), DataInformation.RIFER, "[m/s]", true);
        velocityRifer.addPropertyChangeListener(this);
        parameters.put(Variables.VelocityRifer.name(), velocityRifer);
        steeringRifer = new DataInformation(Variables.Velocity.name(), DataInformation.MEASURE, "[m/s]", true);
        steeringRifer.addPropertyChangeListener(this);
        parameters.put(Variables.Velocity.name(), steeringRifer);
        parameters.put(Variables.SteeringRifer.name(), new DataInformation(Variables.SteeringRifer.name(), DataInformation.RIFER, "[rad/s]", true));
        parameters.put(Variables.Steering.name(), new DataInformation(Variables.Steering.name(), DataInformation.MEASURE, "[rad/s]", true));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == velocityRifer) {
            if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
                velocity = (Double) evt.getNewValue();
            }
        } else if (evt.getSource() == steeringRifer) {
            if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
                steering = (Double) evt.getNewValue();
            }
        }
//        if (evt.getPropertyName().equals(PROP_VELOCITY)) {
//            parameters.get(Variables.VelocityRifer).setData((Double) evt.getNewValue());
//        } else if (evt.getPropertyName().equals(PROP_STEERING)) {
//            parameters.get(Variables.SteeringRifer).setData((Double) evt.getNewValue());
//        }
    }

    /**
     * Get the value of velocity
     *
     * @return the value of velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Set the value of velocity
     *
     * @param velocity new value of velocity
     */
    public void setVelocity(double velocity) {
        double oldVelocity = this.velocity;
        this.velocity = velocity;
        propertyChangeSupport.firePropertyChange(PROP_VELOCITY, oldVelocity, velocity);
        parameters.get(Variables.VelocityRifer.name()).setData(velocity);
    }

    /**
     * Get the value of steering
     *
     * @return the value of steering
     */
    public double getSteering() {
        return steering;
    }

    /**
     * Set the value of steering
     *
     * @param steering new value of steering
     */
    public void setSteering(double steering) {
        double oldSteering = this.steering;
        this.steering = steering;
        propertyChangeSupport.firePropertyChange(PROP_STEERING, oldSteering, steering);
        parameters.get(Variables.SteeringRifer.name()).setData(steering);
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
        return "Drive";
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
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
