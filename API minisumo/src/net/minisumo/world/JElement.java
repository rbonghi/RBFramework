/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world;

import java.awt.Color;
import java.awt.Shape;
import java.io.Serializable;
import java.util.Date;
/**
 *
 * @author Raffaello
 */
public interface JElement extends Serializable {
    
    public String getName();
    
    public Color getColor();
    
    public Date getDate();
    
    public Shape getPolygon();
}
