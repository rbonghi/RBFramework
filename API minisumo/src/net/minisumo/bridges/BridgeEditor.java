/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridges;

import java.beans.PropertyEditorSupport;
import java.util.Set;
import java.util.TreeMap;
import org.openide.util.Lookup;

/**
 *
 * @author Raffaello
 */
public class BridgeEditor extends PropertyEditorSupport {

    private static final String NO_BRIDGE = "No bridge";
    private transient String[] tags;

    public BridgeEditor() {
        TreeMap<String, Class<? extends Bridge>> comunication = BridgeLoader.getDefault().getComunication();
        Set<String> keySet = comunication.keySet();
        tags = keySet.toArray(new String[keySet.size()]);
    }

    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!text.equals(NO_BRIDGE)) {
            Bridge value = (Bridge) getValue();
            if (value != null) {
                value.stop();
                BridgeLoader.save(value, null);
            }
            Bridge load = BridgeLoader.getDefault().load(text);
            setValue(load);
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return (value != null ? value.getClass().getSimpleName() : NO_BRIDGE);
    }
}
