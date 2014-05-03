/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.obstacle;

import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = ProjectFactory.class)
public class ObstacleProjectFactory implements ProjectFactory {
    
    public static final String OBSTACLE_DIR = "obstacleProperty";

    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(OBSTACLE_DIR) != null;
    }

    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new ObstacleProject(dir, state) : null;
    }

    @Override
    public void saveProject(Project prjct) throws IOException, ClassCastException {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
