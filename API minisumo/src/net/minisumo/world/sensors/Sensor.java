/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.sensors;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 *
 * @author Raffaello
 */
public interface Sensor extends Serializable {
    
    public Sensor load(ObjectInputStream input);
    
    public Color getColor();
    
    public void setColor(Color color);
    
    public double getPosition();
    
    public double getOrientation();
    
    public GeneralPath polygon(double scale);
    
    public void setDistance(double distance);
    
    public double getDistance();
    
}
