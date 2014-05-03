/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial;

import java.util.EventListener;

/**
 *
 * @author Raffaello
 */
public interface EventJpacketListener extends EventListener {

    public void eventJpacketOccurred(EventJpacket evt);
}
