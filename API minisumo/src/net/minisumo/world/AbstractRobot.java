/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import net.minisumo.bridges.Bridge;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.component.Component;
import net.minisumo.world.sensors.Sensor;

/**
 *
 * @author Raffaello
 */
public abstract class AbstractRobot implements JElement {

    public static final String PROP_NAME = "name";
    public static final String PROP_COLOR = "color";
    public static final String PROP_CONTROL = "control";
    public static final String PROP_SENSOR = "sensor";
    public static final String PROP_ENGINE = "engine";
    public static final String PROP_PARAMETERS = "parameters";
    public static final String PROP_COMPONENTS = "components";
    public static final String PROP_SENSORS = "sensors";
    public static final String PROP_BRIDGE = "bridge";

    public enum Coordinates {

        x, y, z, th
    };

    public abstract void setControl(boolean control);
    
    public abstract boolean isControl();

    public abstract void setEngine(boolean engine);

    public abstract boolean isEngine();
    
    public abstract void setSensor(boolean sensor);

    public abstract boolean isSensor();

    public abstract HashMap<String, Component> getComponents();

    public abstract HashMap<Finder, DataInformation> getParameters();

    public abstract HashMap<String, Sensor> getSensors();

    public abstract Bridge getBridge();

    public abstract void setBridge(Bridge bridge);

    public abstract void addPropertyChangeListener(PropertyChangeListener listener);

    public abstract void removePropertyChangeListener(PropertyChangeListener listener);
}
