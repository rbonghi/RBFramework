/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.minisumo.bridges.Bridge;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.robot.Robot;
import net.minisumo.world.robot.editor.EditorTopComponent;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class RobotNode extends BeanNode {

    private String name;
//    private EditorTopComponent editorTopComponent;
    private Robot robot;

    public RobotNode(Robot robot) throws IntrospectionException {
        super(robot, Children.LEAF, Lookups.singleton(robot));
        this.robot = robot;
    }

    @Override
    public Action getPreferredAction() {
        return new EditorAction(robot);
//        return super.getPreferredAction();
    }

    @Override
    public String getHtmlDisplayName() {
        if (name == null) {
            return super.getHtmlDisplayName();
        } else {
            return name;
        }
    }

    private static class EditorAction extends AbstractAction {

        EditorTopComponent tc;

        public EditorAction(Robot robot) {
            putValue(NAME, "Editor");
            tc = new EditorTopComponent();
            tc.setRobot(robot);
//            MultiViewDescription[] dsc = new MultiViewDescription[]{
//                new EditorDescription(),
//                new EditorDescription(),
//            };
//            tc = MultiViewFactory.createMultiView(dsc, dsc[0]);
//            tc.setDisplayName("My static mvtc displayName");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tc.open();
        }
    }

    public static class LeafNode extends BeanNode {

        public LeafNode(Object obj) throws IntrospectionException {
            super(obj, Children.LEAF, Lookups.singleton(obj));
        }
    }

    public static class ParametersChilds extends ChildFactory implements PropertyChangeListener {

        private HashMap map;

        public ParametersChilds(HashMap map, AbstractRobot robot) {
            robot.addPropertyChangeListener(this);
            this.map = map;
        }

        @Override
        protected boolean createKeys(List list) {
            list.addAll(map.values());
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
            if (evt.getPropertyName().equals(AbstractRobot.PROP_COMPONENTS) || evt.getPropertyName().equals(AbstractRobot.PROP_PARAMETERS) || evt.getPropertyName().equals(AbstractRobot.PROP_SENSORS)) {
                refresh(true);
            }
        }
    }
}
