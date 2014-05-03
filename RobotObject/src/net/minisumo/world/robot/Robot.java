/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot;

import java.awt.Color;
import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.HashMap;
import net.minisumo.bridges.Bridge;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.component.Component;
import net.minisumo.world.sensors.Sensor;

/**
 *
 * @author Raffaello
 */
public class Robot extends AbstractRobot {

    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    //Caratteristiche generali
    private String name;
    private Color color;
    private final Date date = new Date();
    //Caratteristiche di controllo
    private transient double[] position;
    public static final String PROP_POSITION = "position";
    private transient boolean control;
    private transient boolean sensor;
    private transient boolean engine;
    private transient HashMap<String, Component> components;
    private transient HashMap<String, Sensor> sensors;
    private HashMap<Finder, DataInformation> parameters;
    private DataInformation x, y, th;
    private transient Shape body;
    //Caratteristiche di comunicazione
    private transient Bridge bridge;
    //Parametri per la registrazione dei dati
    private boolean log;
    public static final String PROP_LOG = "log";

    public Robot() {
        this.name = "Robot";
        this.color = Color.YELLOW;
        position = new double[3];
        components = new HashMap<String, Component>();
        parameters = new HashMap<Finder, DataInformation>();
        sensors = new HashMap<String, Sensor>();
        //Init parameters
        x = new DataInformation(AbstractRobot.Coordinates.x.name(), DataInformation.MEASURE, "[m]", true);
        y = new DataInformation(AbstractRobot.Coordinates.y.name(), DataInformation.MEASURE, "[m]", true);
        th = new DataInformation(AbstractRobot.Coordinates.th.name(), DataInformation.MEASURE, "[rad]", true);
        parameters.put(new Finder(AbstractRobot.Coordinates.x.name()), x);
        parameters.put(new Finder(AbstractRobot.Coordinates.y.name()), y);
        parameters.put(new Finder(AbstractRobot.Coordinates.th.name()), th);
    }

    public void addComponent(String name, Component part) {
        Component component = (Component) part;
        components.put(name, component);
        HashMap<String, DataInformation> compParameters = component.getParameters();
        if (compParameters != null) {
            for (String i : compParameters.keySet()) {
//                parameters.put(new Finder(data, i), parameters1.get(i));
                parameters.put(new Finder(component, i), compParameters.get(i));
            }
        }
        if (name.equals("Body")) {
            body = component.getPolygon();
        }
        propertyChangeSupport.firePropertyChange(PROP_COMPONENTS, null, component);
    }

    public void removeComponent(String name) {
        Component component = components.get(name);
        for (Finder find : Finder.find(parameters, component)) {
            parameters.remove(find);
        }
//        components.remove(name);
    }

    public void addSensor(String name, Sensor part) {
        Sensor newSensor = (Sensor) part;
        sensors.put(name, newSensor);
        propertyChangeSupport.firePropertyChange(PROP_SENSORS, null, newSensor);
    }

    public void addParameters(String name, DataInformation data) {
        parameters.put(new Finder(name), data);
        propertyChangeSupport.firePropertyChange(PROP_PARAMETERS, null, data);
    }

    public void initRobot() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        components = new HashMap<String, Component>();
        sensors = new HashMap<String, Sensor>();
        position = new double[3];
    }

    private void updatePosition() {
        x.setData(position[0]);
        y.setData(position[1]);
        th.setData(position[2]);
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
    public void setColor(Color color) {
        Color oldColor = this.color;
        this.color = color;
        propertyChangeSupport.firePropertyChange(PROP_COLOR, oldColor, color);
    }

    /**
     * Get the value of position
     *
     * @return the value of position
     */
    public double[] getPosition() {
        return position;
    }

    /**
     * Set the value of position
     *
     * @param position new value of position
     */
    public void setPosition(double[] position) {
        double[] oldPosition = this.position;
        this.position = position;
        propertyChangeSupport.firePropertyChange(PROP_POSITION, oldPosition, position);
        updatePosition();
    }

    /**
     * Get the value of position at specified index
     *
     * @param index
     * @return the value of position at specified index
     */
    public double getPosition(int index) {
        return this.position[index];
    }

    /**
     * Set the value of position at specified index.
     *
     * @param index
     * @param newPosition new value of position at specified index
     */
    public void setPosition(int index, double newPosition) {
        double oldPosition = this.position[index];
        this.position[index] = newPosition;
        propertyChangeSupport.fireIndexedPropertyChange(PROP_POSITION, index, oldPosition, newPosition);
        updatePosition();
    }

    /**
     * Get the value of control
     *
     * @return the value of control
     */
    @Override
    public boolean isControl() {
        return control;
    }

    /**
     * Set the value of control
     *
     * @param control new value of control
     */
    @Override
    public void setControl(boolean control) {
        boolean oldControl = this.control;
        this.control = control;
        propertyChangeSupport.firePropertyChange(PROP_CONTROL, oldControl, control);
    }

    /**
     * Get the value of sensor
     *
     * @return the value of sensor
     */
    @Override
    public boolean isSensor() {
        return sensor;
    }

    /**
     * Set the value of sensor
     *
     * @param sensor new value of sensor
     */
    @Override
    public void setSensor(boolean sensor) {
        boolean oldSensor = this.sensor;
        this.sensor = sensor;
        propertyChangeSupport.firePropertyChange(PROP_SENSOR, oldSensor, sensor);
    }

    /**
     * Get the value of engine
     *
     * @return the value of engine
     */
    @Override
    public boolean isEngine() {
        return engine;
    }

    /**
     * Set the value of engine
     *
     * @param engine new value of engine
     */
    @Override
    public void setEngine(boolean engine) {
        boolean oldEngine = this.engine;
        this.engine = engine;
        propertyChangeSupport.firePropertyChange(PROP_ENGINE, oldEngine, engine);
    }

    /**
     * Get the value of log
     *
     * @return the value of log
     */
    public boolean isLog() {
        return log;
    }

    /**
     * Set the value of log
     *
     * @param log new value of log
     */
    public void setLog(boolean log) {
        boolean oldLog = this.log;
        this.log = log;
        propertyChangeSupport.firePropertyChange(PROP_LOG, oldLog, log);
    }

    /**
     * Get the value of bridge
     *
     * @return the value of bridge
     */
    @Override
    public Bridge getBridge() {
        return bridge;
    }

    /**
     * Set the value of bridge
     *
     * @param bridge new value of bridge
     */
    @Override
    public void setBridge(Bridge bridge) {
        Bridge oldBridge = this.bridge;
        this.bridge = bridge;
        this.bridge.register(this);                        //Registrazione robot alla comunicazione
        propertyChangeSupport.firePropertyChange(PROP_BRIDGE, oldBridge, bridge);
    }

    /**
     * Get the value of components
     *
     * @return the value of components
     */
    @Override
    public HashMap<String, Component> getComponents() {
        return components;
    }

    /**
     * Set the value of components
     *
     * @param components new value of components
     */
    public void setComponents(HashMap<String, Component> components) {
        HashMap<String, Component> oldComponents = this.components;
        this.components = components;
        propertyChangeSupport.firePropertyChange(PROP_COMPONENTS, oldComponents, components);
    }

    /**
     * Get the value of parameters
     *
     * @return the value of parameters
     */
    @Override
    public HashMap<Finder, DataInformation> getParameters() {
        return parameters;
    }

    /**
     * Set the value of parameters
     *
     * @param parameters new value of parameters
     */
    public void setParameters(HashMap<Finder, DataInformation> parameters) {
        HashMap<Finder, DataInformation> oldParameters = this.parameters;
        this.parameters = parameters;
        propertyChangeSupport.firePropertyChange(PROP_PARAMETERS, oldParameters, parameters);
    }

    /**
     * Get the value of sensors
     *
     * @return the value of sensors
     */
    @Override
    public HashMap<String, Sensor> getSensors() {
        return sensors;
    }

    /**
     * Set the value of sensors
     *
     * @param sensors new value of sensors
     */
    public void setSensors(HashMap<String, Sensor> sensors) {
        HashMap<String, Sensor> oldSensors = this.sensors;
        this.sensors = sensors;
        propertyChangeSupport.firePropertyChange(PROP_SENSORS, oldSensors, sensors);
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Shape getPolygon() {
        return body;
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
