/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator.parts;

/**
 *
 * @author Raffaello
 */
public interface NonLinearControl<O, I> extends Control<O, I> {
    
    public O updateControl(I rifer, I state);
}
