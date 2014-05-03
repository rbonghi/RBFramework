/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.obstacle;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class ObstacleDataObject extends MultiDataObject {

    private transient Obstacle obstacle;

    public ObstacleDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        ObjectInputStream obj = null;
        try {
            obj = new ObjectInputStream(pf.getInputStream());
            try {
                obstacle = (Obstacle) obj.readObject();
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
                obstacle = new Obstacle();
            }
            obj.close();
        } catch (IOException ex) {
            obstacle = new Obstacle();
        }
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}
