/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.obstacle;

import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.nodes.Node;

/**
 *
 * @author Raffaello
 */
public class ObstacleLogicalView implements LogicalViewProvider {

    private ObstacleProject project;

    public ObstacleLogicalView(ObstacleProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        return project.getDataObstacle().getNodeDelegate();
    }

    @Override
    public Node findPath(Node node, Object o) {
        return null;
    }
}
