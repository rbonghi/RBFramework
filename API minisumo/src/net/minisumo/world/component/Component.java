/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.component;

import java.awt.Color;
import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import net.minisumo.util.DataInformation;

/**
 *
 * @author Raffaello
 */
public interface Component extends Serializable {

    /**
     * Get the value of color
     *
     * @return the value of color
     */
    public Color getColor();

    /**
     * Set the value of color
     *
     * @param color new value of color
     */
    public void setColor(Color color);
    
    public String getName();

    public boolean isDrawable();

    public Shape getPolygon();
    
    public Component load(ObjectInputStream objectInputStream);
    
    public HashMap<String, DataInformation> getParameters();
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
