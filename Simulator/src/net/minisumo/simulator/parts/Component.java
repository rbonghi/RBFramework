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
public abstract class Component<O, I> implements Serializable {

    public abstract void resetModel();

    public abstract O updateModel(I input);
}
