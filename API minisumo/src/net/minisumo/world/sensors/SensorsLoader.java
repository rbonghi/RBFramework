/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.sensors;

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
@ServiceProvider(service = SensorsLoader.class)
public class SensorsLoader {

    private HashMap<String, Class<? extends Sensor>> sensors;
    public static final String EXTENSION_SNS = "sns";

    public SensorsLoader() {
        sensors = new HashMap<String, Class<? extends Sensor>>();
        Lookup.Result<Sensor> componentResult = Lookup.getDefault().lookupResult(Sensor.class);
        for (Class<? extends Sensor> classes : componentResult.allClasses()) {
            sensors.put(classes.getSimpleName(), classes);
        }
    }

    public Collection<Class<? extends Sensor>> getSensors() {
        return sensors.values();
    }

    public static SensorsLoader getDefault() {
        SensorsLoader sensors = Lookup.getDefault().lookup(SensorsLoader.class);
        if (sensors == null) {
            sensors = new SensorsLoader();
        }
        return sensors;
    }
    
    public Entry<String, Sensor> load(FileObject file) {
        ObjectInputStream objectInputStream = null;
        if (!file.getExt().equals(EXTENSION_SNS)) {
            return null;
        }
        String name = file.getName();
        Class<? extends Sensor> classSensors = sensors.get(name.split("-")[0]);
        try {
            objectInputStream = new ObjectInputStream(file.getInputStream());
            Sensor load = classSensors.newInstance().load(objectInputStream);
            objectInputStream.close();
            if (load != null) {
                return new HashMap.SimpleEntry<String, Sensor>(name, load);
            }
        } catch (Exception ex) {
//            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    public static void save(Entry<String, Sensor> component, FileObject folder) {
        try {
            FileObject componentFile = folder.getFileObject(component.getKey() + "." + EXTENSION_SNS);
            if (componentFile == null) {
                componentFile = folder.createData(component.getKey() + "." + EXTENSION_SNS);
            }
            ObjectOutputStream outStream = new ObjectOutputStream(componentFile.getOutputStream());
            outStream.writeObject(component.getValue());
            outStream.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
