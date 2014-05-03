/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.component.Component;
import net.minisumo.world.robot.logicalview.RobotNode.LeafNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class ComponentNode extends AbstractNode {

    private String name;

    public ComponentNode(AbstractRobot robot, String name) {
        super(Children.create(new ComponentChilds(robot), true), Lookups.singleton(robot.getComponents()));
        this.name = name;
    }

    @Override
    public String getHtmlDisplayName() {
        return name;
    }

    private static class ComponentChilds extends ChildFactory implements PropertyChangeListener, Comparator<Component> {
        
        private HashMap<String, Component> map;

        public ComponentChilds(AbstractRobot robot) {
            this.map = robot.getComponents();
            robot.addPropertyChangeListener(this);
        }

        @Override
        protected boolean createKeys(List list) {
            ArrayList<Component> arrayList = new ArrayList<Component>(map.values());
            Collections.sort(arrayList, this);
            list.addAll(arrayList);
            return true;
        }

        @Override
        protected Node createNodeForKey(Object key) {
            try {
                return new LeafNode(key);
            } catch (IntrospectionException ex) {
                return Node.EMPTY;
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(AbstractRobot.PROP_COMPONENTS)) {
                refresh(true);
            }
        }

        @Override
        public int compare(Component o1, Component o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}