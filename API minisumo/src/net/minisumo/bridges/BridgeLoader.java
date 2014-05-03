/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridges;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;
import net.minisumo.world.World;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = BridgeLoader.class)
public class BridgeLoader {

    private TreeMap<String, Class<? extends Bridge>> comunication;
    public static final String BRIDGE_DIR = "Bridge";
    public static final String EXTENSION_BRG = "brd";

    public BridgeLoader() {
        comunication = new TreeMap<String, Class<? extends Bridge>>();
        Lookup.Result<Bridge> comunicationResult = Lookup.getDefault().lookupResult(Bridge.class);
        for (Class<? extends Bridge> classes : comunicationResult.allClasses()) {
            comunication.put(classes.getSimpleName(), classes);
        }
    }

    public static BridgeLoader getDefault() {
        BridgeLoader bridges = Lookup.getDefault().lookup(BridgeLoader.class);
        if (bridges == null) {
            bridges = new BridgeLoader();
        }
        return bridges;
    }

    public TreeMap<String, Class<? extends Bridge>> getComunication() {
        return comunication;
    }

    public Bridge load(String name, FileObject bridgeDirectory) {
        FileObject fileObject = bridgeDirectory.getFileObject(name + "." + EXTENSION_BRG);
        Class<? extends Bridge> bridgeClass = comunication.get(name);
        try {
            if (fileObject == null) {
                return bridgeClass.newInstance();
            } else {
                ObjectInputStream objectInputStream = new ObjectInputStream(fileObject.getInputStream());
                Bridge loadBridge = bridgeClass.newInstance().loadBridge(objectInputStream);
                objectInputStream.close();
                return loadBridge;
            }
        } catch (Exception ex) {
//            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    public Bridge load(String name) {
        FileObject bridgeDirectory = World.getDefault().getThisProject().getProjectDirectory().getFileObject(BRIDGE_DIR);
        return load(name, bridgeDirectory);
    }

    public static void save(Bridge bridge, FileObject file) {
        String name = bridge.getClass().getSimpleName();
        FileObject bridgeDirectory;
        if (file == null) {
            bridgeDirectory = World.getDefault().getThisProject().getProjectDirectory().getFileObject(BRIDGE_DIR);
        } else {
            bridgeDirectory = file;
        }
        FileObject bridgeData = bridgeDirectory.getFileObject(name + "." + EXTENSION_BRG);
        try {
            if (bridgeData == null) {
                bridgeData = bridgeDirectory.createData(bridge.getClass().getSimpleName() + "." + EXTENSION_BRG);
            }
            //Get the file from the FileObject obtained above:
            ObjectOutputStream outStrem = new ObjectOutputStream(bridgeData.getOutputStream());
            outStrem.writeObject(bridge);
            outStrem.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
