/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.project;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;
import net.minisumo.bridges.Bridge;
import net.minisumo.bridges.BridgeLoader;
import net.minisumo.world.component.Component;
import net.minisumo.world.component.ComponentLoader;
import net.minisumo.world.robot.Recorder;
import net.minisumo.world.robot.Robot;
import net.minisumo.world.robot.RobotDataObject;
import net.minisumo.world.sensors.Sensor;
import net.minisumo.world.sensors.SensorsLoader;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = ProjectFactory.class)
public class RobotProjectFactory implements ProjectFactory {

    public static final String PROPERTY_DIR = "RobotProperty";

    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROPERTY_DIR) != null;
    }

    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new RobotProject(dir, state) : null;
    }

    @Override
    public void saveProject(Project prjct) throws IOException, ClassCastException {
        ObjectOutputStream outStrem;
        DataObject dataRobot = ((RobotProject) prjct).getDataRobot();
        FileObject primaryFile = dataRobot.getPrimaryFile();
        Robot robot = prjct.getLookup().lookup(Robot.class);
        //Chiusura log
        if(robot.isLog()){
            Recorder lookup = prjct.getLookup().lookup(Recorder.class);
            lookup.closeWriter();
        }
        //Salvataggio Bridge
        Bridge bridge = robot.getBridge();
        if (bridge != null) {
            FileObject bridgeFolder = ((RobotProject) prjct).getFolder(RobotProject.BRIDGE_DIR);
            BridgeLoader.save(bridge, bridgeFolder);
            bridge.stop();
        }
        //Salvataggio componenti
        FileObject componentFolder = ((RobotProject) prjct).getFolder(RobotProject.COMPONENT_DIR);
        for(Entry<String, Component> component : robot.getComponents().entrySet()){
            ComponentLoader.save(component, componentFolder);
        }
        for(String component : robot.getComponents().keySet()){
            robot.removeComponent(component);
        }
        for(Entry<String, Sensor> sensorName : robot.getSensors().entrySet()){
            SensorsLoader.save(sensorName, componentFolder);
        }
        //Get the file from the FileObject obtained above:
        outStrem = new ObjectOutputStream(primaryFile.getOutputStream());
        outStrem.writeObject(robot);
        outStrem.close();
    }
}
