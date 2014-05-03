/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

public class RobotDataObject extends MultiDataObject {

    public RobotDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException {
        super(pf, loader);
//        ObjectInputStream obj = null;
//        try {
//            obj = new ObjectInputStream(pf.getInputStream());
//            try {
//                robot = (Robot) obj.readObject();
//                robot.initRobot();
//            } catch (ClassNotFoundException ex) {
//                Exceptions.printStackTrace(ex);
//                robot = new Robot();
//            }
//            obj.close();
//        } catch (IOException ex) {
//        }
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF, getLookup());
//        try {
//            return new RobotNode(robot);
//        } catch (IntrospectionException ex) {
//            return Node.EMPTY;
//        }
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }
}
