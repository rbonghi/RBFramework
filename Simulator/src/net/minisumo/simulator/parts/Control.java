/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator.parts;

import java.io.Serializable;

/**
 *
 * @author Raffaello
 */
public interface Control<O, I> extends Serializable {
    
    public O updateControl(I error);
    
}
