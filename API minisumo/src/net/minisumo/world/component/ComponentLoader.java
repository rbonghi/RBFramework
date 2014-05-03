/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = ComponentLoader.class)
public class ComponentLoader {

    private HashMap<String, Class<? extends Component>> components;
    public static final String EXTENSION_PRT = "prt";

    public ComponentLoader() {
        components = new HashMap<String, Class<? extends Component>>();
        Lookup.Result<Component> componentResult = Lookup.getDefault().lookupResult(Component.class);
        for (Class<? extends Component> classes : componentResult.allClasses()) {
            components.put(classes.getSimpleName(), classes);
        }
    }

    public Collection<Class<? extends Component>> getComponents() {
        return components.values();
    }

    public static ComponentLoader getDefault() {
        ComponentLoader components = Lookup.getDefault().lookup(ComponentLoader.class);
        if (components == null) {
            components = new ComponentLoader();
        }
        return components;
    }

    public Entry<String, Component> load(FileObject file) {
        ObjectInputStream objectInputStream = null;
        if (!file.getExt().equals(EXTENSION_PRT)) {
            return null;
        }
        String name = file.getName();
        Class<? extends Component> classComponents = components.get(name.split("-")[0]);
        try {
            objectInputStream = new ObjectInputStream(file.getInputStream());
            Component load = classComponents.newInstance().load(objectInputStream);
            objectInputStream.close();
            if (load != null) {
                return new HashMap.SimpleEntry<String, Component>(name, load);
            }
        } catch (Exception ex) {
//            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static void save(Entry<String, Component> component, FileObject folder) {
        try {
            FileObject componentFile = folder.getFileObject(component.getKey() + "." + EXTENSION_PRT);
            if (componentFile == null) {
                componentFile = folder.createData(component.getKey() + "." + EXTENSION_PRT);
            }
            ObjectOutputStream outStream = new ObjectOutputStream(componentFile.getOutputStream());
            outStream.writeObject(component.getValue());
            outStream.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
