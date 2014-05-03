/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial;

import java.util.EventObject;
import net.minisumo.serial.packet.Jpacket;

/**
 *
 * @author Raffaello
 */
public class EventJpacket extends EventObject {

    public EventJpacket(Object source) {
        super(source);
    }

    @Override
    public Jpacket getSource() {
        return (Jpacket) source;
    }
}
