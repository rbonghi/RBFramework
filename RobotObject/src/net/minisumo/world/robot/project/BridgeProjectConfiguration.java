/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.project;

import net.minisumo.bridges.Bridge;
import org.netbeans.spi.project.ProjectConfiguration;

/**
 *
 * @author Raffaello
 */
public class BridgeProjectConfiguration implements ProjectConfiguration {

    private final String name;
    private Bridge bridge;

    public BridgeProjectConfiguration(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public Bridge getBridge() {
        return bridge;
    }
}
