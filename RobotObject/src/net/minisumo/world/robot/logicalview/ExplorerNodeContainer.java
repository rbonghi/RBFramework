/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Action;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.FolderLookup;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Raffaello
 */
public class ExplorerNodeContainer extends Children.Keys<FileObject> {

    private FileObject folder = null;

    public ExplorerNodeContainer(FileObject folder) {
        this.folder = folder;
    }

    protected void addNotify() {
        setKeys(new FileObject[]{folder});
    }

    protected Node[] createNodes(FileObject f) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        /* add folder nodes */
        for(FileObject o : Collections.list(f.getFolders(false))) {
        nodes.add(new ExplorerFolderNode(o));
        }
        DataFolder df = DataFolder.findFolder(f);
        FolderLookup lkp = new ActionLookup(df);
        /* add leaf nodes, which represents an action */
        for (Action a : lkp.getLookup().lookupAll(Action.class)) {
            nodes.add(new ExplorerLeafNode(a));
        }
        return (nodes.toArray(new Node[nodes.size()]));
    }
    /* non-recursive folder lookup */

    private static final class ActionLookup extends FolderLookup {

        public ActionLookup(DataFolder df) {
            super(df);
        }

        protected InstanceCookie acceptContainer(DataObject.Container con) {
            return (null);
        }

        protected InstanceCookie acceptFolder(DataFolder df) {
            return (null);
        }
    }
    
    
}