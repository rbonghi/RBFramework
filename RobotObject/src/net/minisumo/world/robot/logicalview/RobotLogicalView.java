/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.robot.Robot;
import net.minisumo.world.robot.editor.EditorTopComponent;
import net.minisumo.world.robot.project.RobotProject;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Raffaello
 */
public class RobotLogicalView implements LogicalViewProvider {

    private final RobotProject project;

    public RobotLogicalView(RobotProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        try {
            return new RobotFilter(new RobotNode(project.getRobot()), project);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return Node.EMPTY;
        }
    }

    @Override
    public Node findPath(Node node, Object o) {
        return null;
    }

    private static class RobotFilter extends FilterNode {

        public RobotFilter(Node node, RobotProject project) {
            super(node, new RobotChildren(node, project),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both:
                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                        node.getLookup()}));
        }

        @Override
        public Action[] getActions(boolean context) {
            Action[] oldActions = super.getActions(context);
            Action[] actions = new Action[oldActions.length + 4];
            System.arraycopy(oldActions, 0, actions, 0, oldActions.length);
            actions[actions.length - 4] = CommonProjectActions.setAsMainProjectAction();
            actions[actions.length - 3] = CommonProjectActions.deleteProjectAction();
            actions[actions.length - 2] = CommonProjectActions.setProjectConfigurationAction();
            actions[actions.length - 1] = CommonProjectActions.closeProjectAction();
            return actions;
        }
    }

    private static class RobotChildren extends FilterNode.Children.Keys<Node> implements PropertyChangeListener {

        private final String PROP_COMP = "Component";
        private final String PROP_PARAMS = "Parameters";
        private final String PROP_SENSORS = "Sensors";
        private final String PROP_BRIDGE = "Bridge";
        private final String PROP_LOG = "Log data";
        private RobotProject project;
        private TreeMap<String, Node> childs = new TreeMap<String, Node>();

        public RobotChildren(Node or, RobotProject project) {
            this.project = project;
            Robot robot = project.getRobot();
            robot.addPropertyChangeListener(this);
            ComponentNode componentNode = new ComponentNode(robot, PROP_COMP);
            componentNode.setIconBaseWithExtension("net/minisumo/world/robot/logicalview/Brick.png");
            childs.put(PROP_COMP, componentNode);
            ParametersNode parametersNode = new ParametersNode(robot, PROP_PARAMS);
            parametersNode.setIconBaseWithExtension("net/minisumo/world/robot/logicalview/Plot.gif");
            childs.put(PROP_PARAMS, parametersNode);
            SensorNode sensorNode = new SensorNode(robot, PROP_SENSORS);
            sensorNode.setIconBaseWithExtension("net/minisumo/world/robot/logicalview/Radar.png");
            childs.put(PROP_SENSORS, sensorNode);
            try {
                DataObject find = DataObject.find(project.getFolder(RobotProject.LOG_DIR));
                childs.put("Z" + PROP_LOG, find.getNodeDelegate());
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        protected Node[] createNodes(Node t) {
            return new Node[]{t};
        }

        @Override
        protected void addNotify() {
            setKeys(childs.values());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(AbstractRobot.PROP_BRIDGE)) {
                try {
                    childs.put(PROP_BRIDGE, new RobotNode.LeafNode(evt.getNewValue()));
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    addNotify();
                }
            });
        }
    }
}
