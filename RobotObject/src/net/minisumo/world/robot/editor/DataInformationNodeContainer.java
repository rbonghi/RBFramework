/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Raffaello
 */
public class DataInformationNodeContainer extends Children.Keys<String> {

    private String genre;

    public DataInformationNodeContainer(String genre) {
        this.genre = genre;
    }

    @Override
    protected void addNotify() {
        setKeys(new String[]{genre});
    }

    @Override
    protected Node[] createNodes(String t) {
        return new Node[]{new DataInformationNode(true), new DataInformationNode(false)};
    }
}
