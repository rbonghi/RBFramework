/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import java.util.Collection;
import net.minisumo.world.component.Component;
import net.minisumo.world.component.ComponentLoader;
import net.minisumo.world.sensors.Sensor;
import net.minisumo.world.sensors.SensorsLoader;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Raffaello
 */
public class GenreNodeContainer extends Children.Keys<String> {

    private Collection<Class<? extends Component>> components;
    private Collection<Class<? extends Sensor>> sensors;

    public GenreNodeContainer() {
        components = ComponentLoader.getDefault().getComponents();
        sensors = SensorsLoader.getDefault().getSensors();
    }

    @Override
    protected void addNotify() {
        setKeys(new String[]{"root"});
    }

    @Override
    protected Node[] createNodes(String root) {
        return (new Node[]{
                    new GenreNode("Body", components),
                    new GenreNode("Wheel", components),
                    new GenreNode("Control", components),
                    new GenreNode("Drive", components),
                    new GenreNode("Sensor", sensors),
                    new DataNode("Data Information")});
    }

    public class GenreNode extends AbstractNode {

        public GenreNode(String genre, Collection<? extends Class> components) {
            super(new ComponentNodeContainer(genre, components));
            this.setDisplayName(genre);
        }
    }
    
    public class DataNode extends AbstractNode {

        public DataNode(String genre) {
            super(new DataInformationNodeContainer(genre));
            this.setDisplayName(genre);
        }
    }
}