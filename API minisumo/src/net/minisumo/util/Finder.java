/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import net.minisumo.world.component.Component;

/**
 *
 * @author Raffaello
 */
public class Finder implements Serializable {

    private Component component;
    private String enumeration;

    public Finder(String enumeration) {
        this.enumeration = enumeration;
    }

    public Finder(Component component, String enumeration) {
        this.component = component;
        this.enumeration = enumeration;
    }

    public Component getComponent() {
        return component;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public static HashMap<String, DataInformation> parametersMap(HashMap<Finder, DataInformation> data) {
        HashMap<String, DataInformation> parameters = new HashMap<String, DataInformation>();
        for (Entry<Finder, DataInformation> entry : data.entrySet()) {
            if (entry.getKey().getComponent() != null) {
                parameters.put(entry.getKey().toString(), entry.getValue());
            } else {
                parameters.put(entry.getValue().getName(), entry.getValue());
            }
        }
        return parameters;
    }

    public static ArrayList<Finder> find(HashMap<Finder, ?> map, Component comp) {
        ArrayList<Finder> finders = new ArrayList<Finder>();
        Set<Finder> keySet = map.keySet();
        for (Finder key : keySet) {
            if (key.getComponent() == comp) {
                finders.add(key);
            }
        }
        return finders;
    }

    public static Finder find(HashMap<Finder, DataInformation> map, String component, String name) {
        Set<Entry<Finder, DataInformation>> entrySet = map.entrySet();
        for (Entry<Finder, DataInformation> entry : entrySet) {
            if ((component == null || component.equals("")) && entry.getKey().getComponent() == null) {
                if (entry.getValue().getName().equals(name)) {
                    return entry.getKey();
                }
            } else {
                if (entry.getKey().getComponent() != null && entry.getKey().getComponent().getName().equals(component) && entry.getValue().getName().equals(name)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Finder)) {
            return false;
        }
        Finder o = (Finder) obj;
        return (o.getComponent() == this.getComponent() && o.getEnumeration().equals(this.getEnumeration()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.component != null ? this.component.hashCode() : 0);
        hash = 37 * hash + (this.enumeration != null ? this.enumeration.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (component != null) {
            return component.getName() + "." + enumeration;
        } else {
            return enumeration;
        }
    }
}
