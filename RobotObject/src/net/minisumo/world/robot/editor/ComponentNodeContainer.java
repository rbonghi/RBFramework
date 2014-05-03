/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import net.minisumo.world.component.Component;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Raffaello
 */
public class ComponentNodeContainer extends Children.Keys<String> {

    private ArrayList<Node> node;
    private String genre;

    ComponentNodeContainer(String genre, Collection<? extends Class> allClasses) {
        this.genre = genre;
        node = new ArrayList<Node>();
        Pattern pattern = Pattern.compile(genre);
        for (Class classes : allClasses) {
            String simpleName = classes.getSimpleName();
            if (pattern.matcher(simpleName).find()) {
                node.add(new ComponentNode(classes));
            }
        }
    }

    @Override
    protected void addNotify() {
        setKeys(new String[] {genre});
    }

    @Override
    protected Node[] createNodes(String t) {
        return node.toArray(new ComponentNode[node.size()]);
    }
}
