/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.sensors.Sensor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class SensorNode extends AbstractNode {

    private String name;

    public SensorNode(AbstractRobot robot, String name) {
        super(Children.create(new RobotNode.ParametersChilds(robot.getSensors(), robot), true), Lookups.singleton(robot.getSensors()));
        this.name = name;
    }

    @Override
    public String getHtmlDisplayName() {
        return name;
    }
}