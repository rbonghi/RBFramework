/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.project;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.minisumo.bridges.Bridge;
import net.minisumo.bridges.BridgeLoader;
import net.minisumo.world.AbstractRobot;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectConfigurationProvider;

/**
 *
 * @author Raffaello
 */
public class ProjectConfigurationProviderImp implements ProjectConfigurationProvider<BridgeProjectConfiguration> {

    private TreeMap<String, BridgeProjectConfiguration> mapBridge = new TreeMap<String, BridgeProjectConfiguration>();
    private TreeMap<String, Class<? extends Bridge>> comunication;
    private final RobotProject project;

    public ProjectConfigurationProviderImp(RobotProject project) {
        this.project = project;
        comunication = BridgeLoader.getDefault().getComunication();
        for (Entry<String, Class<? extends Bridge>> entry : comunication.entrySet()) {
            mapBridge.put(entry.getKey(), new BridgeProjectConfiguration(entry.getKey()));
        }
    }

    @Override
    public Collection<BridgeProjectConfiguration> getConfigurations() {
        return mapBridge.values();
    }

    @Override
    public BridgeProjectConfiguration getActiveConfiguration() {
        AbstractRobot robot = project.getLookup().lookup(AbstractRobot.class);
        if (robot.getBridge() != null) {
            String simpleName = robot.getBridge().getClass().getSimpleName();
            return mapBridge.get(simpleName);
        } else {
            return null;
        }
    }

    @Override
    public void setActiveConfiguration(BridgeProjectConfiguration c) throws IllegalArgumentException, IOException {
        AbstractRobot robot = project.getLookup().lookup(AbstractRobot.class);
        Bridge load = BridgeLoader.getDefault().load(c.getDisplayName(), project.getFolder(RobotProject.BRIDGE_DIR));
        c.setBridge(load);
        if (robot.getBridge() != null) {
            robot.getBridge().stop();
        }
        robot.setBridge(load);
    }

    @Override
    public boolean hasCustomizer() {
        //TODO quando sarà possibile creare simulatori e bridge dinamici
        return false;
    }

    @Override
    public void customize() {
        System.out.println();
    }

    @Override
    public boolean configurationsAffectAction(String string) {
        return string.equals(ActionProvider.COMMAND_RUN);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
    }
}
