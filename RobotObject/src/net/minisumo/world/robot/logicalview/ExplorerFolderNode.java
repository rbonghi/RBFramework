/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author Raffaello
 */
public class ExplorerFolderNode extends AbstractNode {

    public ExplorerFolderNode(FileObject node) {
        super(new ExplorerNodeContainer(node));
        setDisplayName(node.getName());
        String iconBase = (String) node.getAttribute("icon");
        if (iconBase != null) {
            setIconBaseWithExtension(iconBase);
        }
    }
}
