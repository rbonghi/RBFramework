/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = World.class)
public class World implements PropertyChangeListener, LookupListener {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private HashMap<Project, JElement> world;
    public static final String PROP_WORLD = "world";
    private HashMap<JElement, Project> project;
    private Project thisProject;
    private JElement thisElement;
    private Lookup.Result result = null;

    public World() {
        world = new HashMap<Project, JElement>();
        project = new HashMap<JElement, Project>();
        OpenProjects.getDefault().addPropertyChangeListener(this);
        result = Utilities.actionsGlobalContext().lookupResult(JElement.class);
        result.addLookupListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(OpenProjects.PROPERTY_OPEN_PROJECTS)) {
            Project[] openProjects = OpenProjects.getDefault().getOpenProjects();
            ArrayList<Project> prjNew = new ArrayList<Project>(Arrays.asList((Project[]) evt.getNewValue()));
            ArrayList<Project> prjOld = new ArrayList<Project>(Arrays.asList((Project[]) evt.getOldValue()));
            if (prjNew.size() > prjOld.size()) {
                for (Project prj : openProjects) {
                    JElement element = prj.getLookup().lookup(JElement.class);
                    if (element != null) {
                        world.put(prj, element);
                        if (project.put(element, prj) == null) {
                            propertyChangeSupport.firePropertyChange(PROP_WORLD, null, element);
                        }
                    }
                }
            } else {
                boolean retainAll = prjOld.removeAll(prjNew);
                for(Project prj : prjOld){
                    JElement remove = world.remove(prj);
                    propertyChangeSupport.firePropertyChange(PROP_WORLD, remove, null);
                    project.remove(remove);
                }
            }
        }
    }

    public HashMap<Project, JElement> getWorld() {
        return world;
    }

    public static World getDefault() {
        World world = Lookup.getDefault().lookup(World.class);
        if (world == null) {
            world = new World();
        }
        return world;
    }

    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
        Collection<AbstractRobot> allInstances = r.allInstances();
        for (AbstractRobot i : allInstances) {
            thisProject = project.get(i);
            thisElement = world.get(thisProject);
        }
    }

    public JElement getThisElement() {
        return thisElement;
    }

    public Project getThisProject() {
        return thisProject;
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
