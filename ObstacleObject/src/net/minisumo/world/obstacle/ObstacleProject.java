/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.obstacle;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
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
public class ObstacleProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    private DataObject data;
    private Obstacle obstacle;
    
    public ObstacleProject(FileObject projectDir, ProjectState state) {
        this.projectDir = projectDir;
        this.state = state;
        DataObject dataRobot = getDataObstacle();
        if(dataRobot instanceof ObstacleDataObject){
            obstacle = ((ObstacleDataObject)dataRobot).getObstacle();
        }
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
//                        new ActionProviderImpl(),
                        new Info(), //Project information implementation
                        new ObstacleLogicalView(this), //Logical view of project implementation
                        state,
                        obstacle,
                        this,});
        }
        return lkp;
    }

    public DataObject getDataObstacle() {
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

    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(
                    "net/minisumo/world/obstacle/wall-icon.png"));
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
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return ObstacleProject.this;
        }
    }
}
