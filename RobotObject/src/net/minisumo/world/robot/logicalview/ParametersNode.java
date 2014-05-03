/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import net.minisumo.bridges.Updater;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.AbstractRobot;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class ParametersNode extends AbstractNode implements PropertyChangeListener {

    private String name;
    private AbstractRobot robot;

    public ParametersNode(AbstractRobot robot, String name) {
        super(Children.create(new ParametersChilds(robot.getParameters(), robot), true), Lookups.singleton(robot.getParameters()));
        this.name = name;
        this.robot = robot;
        robot.addPropertyChangeListener(this);
        for (DataInformation data : robot.getParameters().values()) {
            data.addPropertyChangeListener(this);
        }
    }

    @Override
    public String getHtmlDisplayName() {
        return name;
    }

    private Sheet createSet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        for (final Entry<String, DataInformation> data : Finder.parametersMap(robot.getParameters()).entrySet()) {
            Property<String> nameParameters = new PropertySupport.ReadWrite<String>(data.getKey() + "Name", String.class, data.getKey(), data.getKey()) {

                @Override
                public String getValue() throws IllegalAccessException, InvocationTargetException {
                    return data.getValue().getName();
                }

                @Override
                public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    data.getValue().setName(t);
                }
            };
            Property<Double> value = new PropertySupport.ReadWrite<Double>(data.getKey() + "Value", Double.class, data.getKey() + "-Value", data.getKey()) {

                @Override
                public Double getValue() throws IllegalAccessException, InvocationTargetException {
                    return data.getValue().getLastData();
                }

                @Override
                public void setValue(Double t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    data.getValue().setData(t);
                }
            };
            set.put(nameParameters);
            set.put(value);
        }
        sheet.put(set);
        return sheet;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = createSet();
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        firePropertySetsChange(null, createSet().toArray());
    }

    private static class ParametersChilds extends ChildFactory implements PropertyChangeListener {

        private ArrayList<Entry<Finder, DataInformation>> childs;
        private HashMap<Finder, DataInformation> map;

        public ParametersChilds(HashMap<Finder, DataInformation> map, AbstractRobot robot) {
            this.map = map;
            childs = new ArrayList();
            updateChild();
            robot.addPropertyChangeListener(this);
        }

        private void updateChild() {
            Set<Entry<Finder, DataInformation>> entrySet = map.entrySet();
            for (Entry<Finder, DataInformation> data : entrySet) {
                if (data.getValue().isVariable()) {
                    childs.add(data);
                }
                if (data.getKey().getComponent() != null) {
                    data.getKey().getComponent().addPropertyChangeListener(this);
                }
            }
        }

        @Override
        protected boolean createKeys(List list) {
            Collections.sort(childs, new FinderComparator());
            list.addAll(childs);
            return true;
        }

        @Override
        protected Node createNodeForKey(Object key) {
            try {
                return new DataInformationNode((Entry<Finder, DataInformation>) key);
            } catch (IntrospectionException ex) {
                return Node.EMPTY;
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(AbstractRobot.PROP_COMPONENTS) || evt.getPropertyName().equals(AbstractRobot.PROP_PARAMETERS)) {
                childs.clear();
                updateChild();
                refresh(true);
            }
        }
    }

    private static class DataInformationNode extends BeanNode implements PropertyChangeListener {

        private DataInformation dataInformation;
        private Finder finder;

        public DataInformationNode(Entry<Finder, DataInformation> entry) throws IntrospectionException {
            super(entry.getValue(), Children.LEAF, Lookups.singleton(entry.getValue()));
            this.finder = entry.getKey();
            this.dataInformation = entry.getValue();
            if (finder.getComponent() != null) {
                finder.getComponent().addPropertyChangeListener(this);
            }
        }

        @Override
        public String getHtmlDisplayName() {
            if (finder.getComponent() != null) {
                return finder.getComponent().getName() + " - " + dataInformation.getName();
            } else {
                return dataInformation.getName();
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("name")) {
                fireDisplayNameChange(null, getHtmlDisplayName());
            }
        }
    }
}