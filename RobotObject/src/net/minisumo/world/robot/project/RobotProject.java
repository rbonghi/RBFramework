/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.project;

import java.io.InputStream;
import java.util.Map.Entry;
import net.minisumo.bridges.Bridge;
import net.minisumo.world.component.Component;
import net.minisumo.world.robot.logicalview.RobotLogicalView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.component.ComponentLoader;
import net.minisumo.world.robot.Recorder;
import net.minisumo.world.robot.Robot;
import net.minisumo.world.sensors.Sensor;
import net.minisumo.world.sensors.SensorsLoader;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectConfigurationProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class RobotProject implements Project, PropertyChangeListener {

    public static final String BRIDGE_DIR = "Bridge";
    public static final String COMPONENT_DIR = "Component";
    public static final String LOG_DIR = "Log";
    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    private DataObject data;
    private Robot robot;
    private Recorder recorder;
    private LogicalViewProvider logicalView = new RobotLogicalView(this);
    private ProjectConfigurationProviderImp projectConfiguration = new ProjectConfigurationProviderImp(this);

    public RobotProject(FileObject projectDir, ProjectState state) {
        this.projectDir = projectDir;
        this.state = state;
        robot = loadRobot(getDataRobot());
        loadComponent(robot, getFolder(COMPONENT_DIR));
        robot.addPropertyChangeListener(this);
        recorder = new Recorder(robot, getFolder(LOG_DIR));
    }

    public Robot getRobot() {
        return robot;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    public FileObject getFolder(String folder) {
        FileObject result = projectDir.getFileObject(folder);
        if (result == null) {
            try {
                result = projectDir.createFolder(folder);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return result;
    }

    public DataObject getDataRobot() {
        FileObject fileObject = null;
        for (FileObject i : projectDir.getChildren()) {
            if (i.isData()) {
                fileObject = i;
                break;
            }
        }
        try {
            data = DataObject.find(fileObject);
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return data;
    }

    private Robot loadRobot(DataObject dataRobot) {
        ObjectInputStream obj = null;
        Robot newRobot = null;
        try {
            FileObject primaryFile = dataRobot.getPrimaryFile();
            InputStream inputStream = primaryFile.getInputStream();
            obj = new ObjectInputStream(inputStream);
            try {
                newRobot = (Robot) obj.readObject();
            } catch (ClassNotFoundException ex) {
                newRobot = new Robot();
            } finally {
                obj.close();
            }
            newRobot.initRobot();
            return newRobot;
        } catch (IOException ex) {
            newRobot = new Robot();
            return newRobot;
        }
    }

    private void loadComponent(Robot robot, FileObject folder) {
        ArrayList<FileObject> otherFile = new ArrayList<FileObject>();
        for (FileObject file : folder.getChildren()) {
            Entry<String, Component> load = ComponentLoader.getDefault().load(file);
            if (load != null) {
                robot.addComponent(load.getKey(), load.getValue());
                load.getValue().addPropertyChangeListener(this);
            } else {
                otherFile.add(file);
            }
        }
        for (FileObject file : otherFile) {
            Entry<String, Sensor> load = SensorsLoader.getDefault().load(file);
            if (load != null) {
                robot.addSensor(load.getKey(), load.getValue());
            }
        }
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                        this,
                        projectConfiguration,
                        new ActionProviderImpl(this),
                        new Info(), //Project information implementation
                        logicalView, //Logical view of project implementation
                        state,
                        robot,
                        recorder,});
        }
        return lkp;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        state.markModified();
    }

    private final class ActionProviderImpl implements ActionProvider {

        private String[] supported = new String[]{
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_RUN,};
        private final RobotProject project;

        public ActionProviderImpl(RobotProject project) {
            this.project = project;
        }

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lkp) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(RobotProject.this);
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(RobotProject.this);
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_RUN)) {
                ProjectConfigurationProvider<BridgeProjectConfiguration> lookup = project.getLookup().lookup(ProjectConfigurationProvider.class);
                if (lookup.configurationsAffectAction(string)) {
                    Bridge bridge = lookup.getActiveConfiguration().getBridge();
                    if (bridge != null) {
                        bridge.sync();
                    }
                }
            }
        }

        @Override
        public boolean isActionEnabled(String string, Lookup lkp) throws IllegalArgumentException {
            if ((string.equals(ActionProvider.COMMAND_DELETE))) {
                return true;
            } else if ((string.equals(ActionProvider.COMMAND_COPY))) {
                return true;
            } else if (string.equals(ActionProvider.COMMAND_RUN)) {
                return true;
            } else {
                throw new UnsupportedOperationException(string);
            }

        }
    }

    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage("net/minisumo/world/robot/robot.png"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
//            System.out.println(pcl);
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return RobotProject.this;
        }
    }
}
