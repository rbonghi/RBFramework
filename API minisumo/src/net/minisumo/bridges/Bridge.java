/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridges;

import java.io.ObjectInputStream;
import java.io.Serializable;
import net.minisumo.world.AbstractRobot;

/**
 *
 * @author Raffaello
 */
public interface Bridge extends Serializable {
    
    public void register(AbstractRobot robot);
    
    public Bridge loadBridge(ObjectInputStream objectInputStream);

    public void stop();
    
    public boolean sync();
    
}
